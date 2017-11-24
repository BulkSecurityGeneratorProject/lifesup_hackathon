package fi.lifesup.hackathon.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fi.lifesup.hackathon.domain.ChallengeWorkspace;
import fi.lifesup.hackathon.domain.ChallengeWorkspaceQuestion;
import fi.lifesup.hackathon.repository.ChallengeWorkspaceQuestionRepository;
import fi.lifesup.hackathon.repository.ChallengeWorkspaceRepository;
import fi.lifesup.hackathon.security.SecurityUtils;
import fi.lifesup.hackathon.service.dto.ChallengeWorkspaceAnswerDTO;
import fi.lifesup.hackathon.service.dto.ChallengeWorkspaceQuestionDTO;

@Service
@Transactional
public class ChallengeWorkspaceQuestionService {

	private final Logger log = LoggerFactory.getLogger(ChallengeWorkspaceQuestionService.class);

	@Inject
	private EntityManager entityManager;

	@Inject
	private ChallengeWorkspaceRepository challengeWorkspaceRepository;

	@Inject
	private ChallengeWorkspaceQuestionRepository challengeWorkspaceQuestionRepository;

	@Inject
	private ChallengeWorkspaceService challengeWorkspaceService;

	public List<ChallengeWorkspaceQuestionDTO> getQuestionNotAnswer(Long workspaceId) {
		String sbQuery = "select new fi.lifesup.hackathon.service.dto.ChallengeWorkspaceQuestionDTO(q.id, q.applicationId, q.workspace.id, q.content, q.subject, q.createdBy, q.createdDate) "
				+ " from ChallengeWorkspaceQuestion q where q.id not in"
				+ " (select q.id from ChallengeWorkspaceQuestion q ,ChallengeWorkspaceAnswer a where q.id = a.question.id group by q.id)"
				+ " and q.workspace.id = " + workspaceId;
		Query query = entityManager.createQuery(sbQuery);

		List<ChallengeWorkspaceQuestionDTO> list = query.getResultList();
		return list;
	}

	public ChallengeWorkspaceQuestion saveChallengeWorkspaceQuestion(
			ChallengeWorkspaceQuestionDTO challengeWorkspaceQuestion) {
		if (challengeWorkspaceQuestion.getId() != null) {
			ChallengeWorkspace challengeWorkspace = challengeWorkspaceRepository
					.findOne(challengeWorkspaceQuestion.getWorkspaceId());
			if (challengeWorkspace == null) {
				return null;
			}

			ChallengeWorkspaceQuestion question = new ChallengeWorkspaceQuestion();
			question.setApplicationId(challengeWorkspaceQuestion.getApplicationId());
			question.setWorkspace(challengeWorkspace);
			question.setContent(challengeWorkspaceQuestion.getContent());
			question.setSubject(challengeWorkspaceQuestion.getSubject());
			return question;
		} else {
			ChallengeWorkspaceQuestion question = challengeWorkspaceQuestionRepository
					.findOne(challengeWorkspaceQuestion.getId());
			question.setContent(challengeWorkspaceQuestion.getContent());
			question.setSubject(challengeWorkspaceQuestion.getSubject());
			return question;
		}
	}

	public ChallengeWorkspaceQuestionDTO getQuestionDTO(Long questionId) {

		ChallengeWorkspaceQuestion question = challengeWorkspaceQuestionRepository.findOne(questionId);
		List<ChallengeWorkspaceAnswerDTO> answerDTOs = question.getAnswers().stream().map(answer -> {
			ChallengeWorkspaceAnswerDTO answerDTO = new ChallengeWorkspaceAnswerDTO(answer);
			return answerDTO;
		}).collect(Collectors.toList());
		ChallengeWorkspaceQuestionDTO questionDTO = new ChallengeWorkspaceQuestionDTO(question);
		questionDTO.setAnswers(answerDTOs);
		return questionDTO;
	}

	public ChallengeWorkspaceQuestionDTO getQuestionDTO(ChallengeWorkspaceQuestion question) {
		ChallengeWorkspaceQuestionDTO questionDTO = new ChallengeWorkspaceQuestionDTO(question);
		questionDTO.setAnswers(challengeWorkspaceService.getListAnswer(question.getAnswers()));
		return questionDTO;
	}
}
