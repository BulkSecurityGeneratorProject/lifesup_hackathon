package fi.lifesup.hackathon.service;

import java.time.ZonedDateTime;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import fi.lifesup.hackathon.domain.*;
import fi.lifesup.hackathon.repository.*;
import fi.lifesup.hackathon.service.*;
import fi.lifesup.hackathon.service.dto.*;

import fi.lifesup.hackathon.repository.ChallengeWorkspaceRepository;
import fi.lifesup.hackathon.security.SecurityUtils;

@Service
@Transactional
public class ChallengeSubmissionFeedbackService {
	private final Logger log = LoggerFactory.getLogger(ChallengeService.class);
	@Inject
	private ChallengeSubmissionRepository challengeSubmissionRepository;
	@Inject
	private ChallengeSubmissionFeedbackRepository challengeSubmissionFeedbackRepository;

	public ChallengeSubmissionFeedback createChallengeSubmission(
			ChallengeSubmissionFeedbackDTO challengeSubmissionFeedbackDTO) {
		ChallengeSubmissionFeedback challengeSubmissionFeedback = new ChallengeSubmissionFeedback();
		ChallengeSubmission challengeSubmission = challengeSubmissionRepository
				.findOne(challengeSubmissionFeedbackDTO.getChallengeSubmissionId());
		if (challengeSubmissionFeedbackDTO.getId() == null) {
			challengeSubmissionFeedback.setCreatedBy(SecurityUtils.getCurrentUserLogin());
			challengeSubmissionFeedback.setCreatedDate(ZonedDateTime.now());
			challengeSubmissionFeedback.setFeedbackText(challengeSubmissionFeedbackDTO.getFeedbackText());
			ChallengeSubmissionFeedback result = challengeSubmissionFeedbackRepository
					.save(challengeSubmissionFeedback);
			challengeSubmission.setFeedback(result);
			challengeSubmissionRepository.save(challengeSubmission);
			return result;
		} else {
			challengeSubmissionFeedback.setId(challengeSubmissionFeedbackDTO.getId());
			challengeSubmissionFeedback.setCreatedBy(SecurityUtils.getCurrentUserLogin());
			challengeSubmissionFeedback.setCreatedDate(ZonedDateTime.now());
			challengeSubmissionFeedback.setFeedbackText(challengeSubmissionFeedbackDTO.getFeedbackText());
			return challengeSubmissionFeedbackRepository.save(challengeSubmissionFeedback);
		}

	}
}
