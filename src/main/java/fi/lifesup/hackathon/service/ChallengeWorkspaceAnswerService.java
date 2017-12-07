package fi.lifesup.hackathon.service;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fi.lifesup.hackathon.domain.ChallengeWorkspaceAnswer;
import fi.lifesup.hackathon.domain.ChallengeWorkspaceQuestion;
import fi.lifesup.hackathon.domain.enumeration.WorkspaceAnswerType;
import fi.lifesup.hackathon.repository.ChallengeWorkspaceAnswerRepository;
import fi.lifesup.hackathon.repository.ChallengeWorkspaceQuestionRepository;
import fi.lifesup.hackathon.repository.UserRepository;
import fi.lifesup.hackathon.security.SecurityUtils;
import fi.lifesup.hackathon.service.dto.ChallengeWorkspaceAnswerDTO;

@Service
@Transactional
public class ChallengeWorkspaceAnswerService {

	private final Logger log = LoggerFactory.getLogger(ChallengeWorkspaceAnswerService.class);

	@Inject
	private ChallengeWorkspaceAnswerRepository challengeWorkspaceAnswerRepository;

	@Inject
	private UserRepository userReponsitory;

	@Inject
	private UserService userService;

	@Inject
	private ChallengeWorkspaceQuestionRepository challengeWorkspaceQuestionReponsitory;

	public ChallengeWorkspaceAnswer saveChallengeWorkspaceAnswer(ChallengeWorkspaceAnswerDTO challengeWorkspaceAnswer) {
		if (challengeWorkspaceAnswer.getId() == null) {

			ChallengeWorkspaceQuestion question = challengeWorkspaceQuestionReponsitory
					.findOne(challengeWorkspaceAnswer.getQuestionId());

			if (question == null) {
				return null;
			}

			ChallengeWorkspaceAnswer answer = new ChallengeWorkspaceAnswer();

			if (SecurityUtils.isCurrentUserInRole("ROLE_HOST") || SecurityUtils.isCurrentUserInRole("ROLE_ADMIN")) {
				answer.setAnswerByType(WorkspaceAnswerType.BY_HOST);
			} else {
				answer.setAnswerByType(WorkspaceAnswerType.BY_USER);
			}

			answer.setContent(challengeWorkspaceAnswer.getContent());
			answer.setQuestion(question);

			return answer;
		}
		else{
			ChallengeWorkspaceAnswer answer = challengeWorkspaceAnswerRepository.findOne(challengeWorkspaceAnswer.getId());
			answer.setContent(challengeWorkspaceAnswer.getContent());
			return answer;
		}
	}
	
}
