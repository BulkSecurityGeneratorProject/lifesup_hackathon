package fi.lifesup.hackathon.service;

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

@Service
@Transactional
public class ChallengeSubmissionFeedbackService {
	private final Logger log = LoggerFactory.getLogger(ChallengeService.class);
	@Inject
	private ChallengeSubmissionRepository challengeSubmissionRepository;
	@Inject
	private ChallengeSubmissionFeedbackRepository challengeSubmissionFeedbackRepository;
	@Inject
	private UserService userService;
	public void createChallengeSubmission(ChallengeSubmissionFeedbackDTO challengeSubmissionFeedbackDTO)
	{
		ChallengeSubmissionFeedback challengeSubmissionFeedback=new ChallengeSubmissionFeedback();
		ChallengeSubmission challengeSubmission= (ChallengeSubmission) challengeSubmissionRepository.findByFeeabackId(challengeSubmissionFeedbackDTO.getId());
			if(challengeSubmission!=null)
			{
				
				challengeSubmissionFeedback.setCreatedBy(userService.getCurrentUser().getLastName());
				challengeSubmissionFeedback.setCreatedDate(challengeSubmissionFeedbackDTO.getCreatedDate());
				challengeSubmissionFeedback.setFeedbackText(challengeSubmissionFeedbackDTO.getFeedbackText());
				challengeSubmissionFeedbackRepository.save(challengeSubmissionFeedback);
			}
			else
			{
				
				challengeSubmissionFeedback.setCreatedBy(userService.getCurrentUser().getLastName());
				challengeSubmissionFeedback.setCreatedDate(challengeSubmissionFeedbackDTO.getCreatedDate());
				challengeSubmissionFeedback.setFeedbackText(challengeSubmissionFeedbackDTO.getFeedbackText());
				challengeSubmissionFeedbackRepository.save(challengeSubmissionFeedback);
			}
		
	}
}
