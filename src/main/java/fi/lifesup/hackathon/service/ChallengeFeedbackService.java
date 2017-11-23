package fi.lifesup.hackathon.service;

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
	public ChallengeFeedback convertDTOToEntity(ChallengeFeedbackDTO challengeFeedbackDTO)
	{
		ChallengeFeedback challengeFeedback=new ChallengeFeedback();
		Challenge challenge=challengeRepository.findOne(challengeFeedbackDTO.getChallengeId());
		challengeFeedback.setChallenge(challenge);
		return challengeFeedback;
	}
	public ChallengeFeedback createdChallengeFeedback(ChallengeFeedbackDTO challengeFeedbackDTO)
	{
		ChallengeFeedback challengeFeedback=new ChallengeFeedback();
		Application application=applicationRepository.findOne(challengeFeedbackDTO.getApplicationId());
		if(application!=null)
		{
			Challenge challenge=challengeRepository.findOne(challengeFeedbackDTO.getChallengeId());
			if(challenge!=null)
			{
				challengeFeedback.setApplicationId(application.getId());
				challengeFeedback.setChallenge(convertDTOToEntity(challengeFeedbackDTO).getChallenge());
				challengeFeedback.setFeedbackForChallenge(challengeFeedbackDTO.getFeedbackForChallenge());
				challengeFeedback.setFeedbackForUs(challengeFeedbackDTO.getFeedbackForUs());
				challengeFeedback.setRating(challengeFeedbackDTO.getRating());
				challengeFeedback.setPublicFeedback(challengeFeedbackDTO.getPublicFeedback());
				return challengeFeedbackRepository.save(challengeFeedback);
			}
			return null;
		}
		return null;
	}
}
