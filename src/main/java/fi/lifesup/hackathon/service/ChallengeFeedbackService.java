package fi.lifesup.hackathon.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fi.lifesup.hackathon.repository.ChallengeRepository;
import fi.lifesup.hackathon.domain.*;
import fi.lifesup.hackathon.repository.*;
import fi.lifesup.hackathon.service.*;
import fi.lifesup.hackathon.service.dto.*;

@Service
@Transactional
public class ChallengeFeedbackService {
	private final Logger log = LoggerFactory.getLogger(ChallengeService.class);
	@Inject
	private ChallengeRepository challengeRepository;
	@Inject
	private ChallengeFeedbackRepository challengeFeedbackRepository;
	@Inject
	private ApplicationRepository applicationRepository;

	public ChallengeFeedback createdChallengeFeedback(ChallengeFeedbackDTO challengeFeedbackDTO) {
		ChallengeFeedback challengeFeedback = new ChallengeFeedback();
		Challenge challenge = challengeRepository.findOne(challengeFeedbackDTO.getChallengeId());
		if (challenge != null) {
			if (challengeFeedbackDTO.getId() != null) {
				challengeFeedback.setId(challengeFeedbackDTO.getId());
			}
			challengeFeedback.setApplicationId(challengeFeedbackDTO.getApplicationId());			
			challengeFeedback.setFeedbackForChallenge(challengeFeedbackDTO.getFeedbackForChallenge());
			challengeFeedback.setFeedbackForUs(challengeFeedbackDTO.getFeedbackForUs());
			challengeFeedback.setRating(challengeFeedbackDTO.getRating());
			challengeFeedback.setPublicFeedback(challengeFeedbackDTO.getPublicFeedback());
			challengeFeedback.setChallenge(challenge);
			return challengeFeedback;
		}
		return null;

	}

	public List<ChallengeFeedbackDTO> getFeedbackByChallenge(Long challengeId) {
		Challenge challenge = challengeRepository.findOne(challengeId);
		List<ChallengeFeedbackDTO> challengeFeedbackDTOs = challenge.getFeedbacks().stream().map(feedback -> {
			return new ChallengeFeedbackDTO(feedback);
		}).collect(Collectors.toList());
		return challengeFeedbackDTOs;
	}
}
