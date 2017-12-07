package fi.lifesup.hackathon.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fi.lifesup.hackathon.domain.ChallengeWorkspace;
import fi.lifesup.hackathon.domain.ChallengeWorkspaceAnswer;
import fi.lifesup.hackathon.domain.ChallengeWorkspaceQuestion;
import fi.lifesup.hackathon.domain.enumeration.WorkspaceAnswerType;
import fi.lifesup.hackathon.repository.ChallengeUserApplicationRepository;
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

	@Inject
	private ChallengeUserApplicationRepository challengeUserApplicationRepository;

	public ChallengeWorkspaceQuestion saveChallengeWorkspaceQuestion(
			ChallengeWorkspaceQuestionDTO challengeWorkspaceQuestion) {
		if (challengeWorkspaceQuestion.getId() == null) {
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

	public List<ChallengeWorkspaceAnswerDTO> getListAnswer(Set<ChallengeWorkspaceAnswer> set) {

		List<ChallengeWorkspaceAnswerDTO> answerDTOs = set.stream().map(answer -> {
			return new ChallengeWorkspaceAnswerDTO(answer);
		}).collect(Collectors.toList());

		return answerDTOs;
	}

	public List<ChallengeWorkspaceQuestionDTO> getQuestionByWorkspace(Long workspaceId) {
		String sbQuery = "select q,"
				+ " case when q.id not in (select q.id from ChallengeWorkspaceQuestion q, ChallengeWorkspaceAnswer a"
				+ " where a.answerByType = 'BY_HOST' and q.id = a.question.id GROUP by q.id) then 'false' else 'true' end"
				+ " from ChallengeWorkspaceQuestion q where q.workspace.id = :workspaceId order by q.createdDate";

		Query query = entityManager.createQuery(sbQuery);
		query.setParameter("workspaceId", workspaceId);
		List<ChallengeWorkspaceQuestionDTO> dtos = new ArrayList<>();
		List<Object[]> list = query.getResultList();
		for (Object[] objects : list) {
			ChallengeWorkspaceQuestion question = (ChallengeWorkspaceQuestion) objects[0];
			ChallengeWorkspaceQuestionDTO dto = new ChallengeWorkspaceQuestionDTO(question);
			dto.setAnswers(getListAnswer(question.getAnswers()));
			dto.setReply(Boolean.parseBoolean((String) objects[1]));
			dtos.add(dto);
		}
		return dtos;
	}

	public List<ChallengeWorkspaceQuestionDTO> getQuestionByWorkspace(Long workspaceId, Long challengeId) {
		StringBuffer sBuffer = new StringBuffer(
				"select q, case when q.id not in (select q.id from ChallengeWorkspaceQuestion q, ChallengeWorkspaceAnswer a"
						+ " where a.answerByType = 'BY_HOST' and q.id = a.question.id GROUP by q.id) then 'false' else 'true' end"
						+ " from ChallengeWorkspaceQuestion q where q.workspace.id = :workspaceId");
		Long applicationId = null;
		if (SecurityUtils.isCurrentUserInRole("ROLE_HOST") == false
				&& SecurityUtils.isCurrentUserInRole("ROLE_ADMIN") == false) {
			applicationId = challengeUserApplicationRepository
					.getMemberByLogin(challengeId, SecurityUtils.getCurrentUserLogin()).getApplicationId();
			sBuffer.append(" and q.applicationId = :applicationId");
		}
		sBuffer.append(" order by q.createdDate ASC");
		System.err.println(sBuffer.toString());
		Query query = entityManager.createQuery(sBuffer.toString());
		query.setParameter("workspaceId", workspaceId);
		if (SecurityUtils.isCurrentUserInRole("ROLE_HOST") == false
				&& SecurityUtils.isCurrentUserInRole("ROLE_ADMIN") == false) {
			query.setParameter("applicationId", applicationId);
		}
		List<ChallengeWorkspaceQuestionDTO> dtos = new ArrayList<>();
		List<Object[]> list = query.getResultList();
		for (Object[] objects : list) {
			ChallengeWorkspaceQuestion question = (ChallengeWorkspaceQuestion) objects[0];
			ChallengeWorkspaceQuestionDTO dto = new ChallengeWorkspaceQuestionDTO(question);
			dto.setAnswers(getListAnswer(question.getAnswers()));
			dto.setReply(Boolean.parseBoolean((String) objects[1]));
			dtos.add(dto);
		}
		return dtos;
	}

	public ChallengeWorkspaceQuestionDTO getQuestionDTO(Long questionId) {
		ChallengeWorkspaceQuestion question = challengeWorkspaceQuestionRepository.findOne(questionId);
		ChallengeWorkspaceQuestionDTO questionDTO = new ChallengeWorkspaceQuestionDTO(question);
		questionDTO.setReply(false);
		List<ChallengeWorkspaceAnswerDTO> answerDTOs = question.getAnswers().stream().map(answer -> {
			if (answer.getAnswerByType() == WorkspaceAnswerType.BY_HOST) {
				questionDTO.setReply(true);
			}
			return new ChallengeWorkspaceAnswerDTO(answer);
		}).collect(Collectors.toList());
		questionDTO.setAnswers(answerDTOs);
		return questionDTO; 
	}

}
