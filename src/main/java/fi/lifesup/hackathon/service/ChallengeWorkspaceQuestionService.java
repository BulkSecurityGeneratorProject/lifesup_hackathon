package fi.lifesup.hackathon.service;

import java.time.ZonedDateTime;
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
	
//	public List<ChallengeWorkspaceQuestionDTO> getQuestionNotAnswer(Long workspaceId) {
//		String sbQuery = "select q.id, q.applicationId, q.workspace.id, q.content, q.subject, q.createdBy, q.createdDate,"
//							+" case when q.id not in (select q.id from ChallengeWorkspaceQuestion q, ChallengeWorkspaceAnswer a"
//							+" where a.answerByType = 'BY_HOST' and q.id = a.question.id GROUP by q.id) then 'false' else 'true' end"
//							+" from ChallengeWorkspaceQuestion q where q.workspace.id = :workspaceId order by q.createdDate";
//                 
//		Query query = entityManager.createQuery(sbQuery);
//		query.setParameter("workspaceId", workspaceId);
//		List<ChallengeWorkspaceQuestionDTO> dtos = new ArrayList<>();
//		List<Object[]> list = query.getResultList();
//		for (Object[] objects : list) {
//			ChallengeWorkspaceQuestionDTO dto = new ChallengeWorkspaceQuestionDTO();
//			dto.setId((Long) objects[0]);
//			dto.setApplicationId((Long) objects[1]);
//			dto.setWorkspaceId((Long) objects[2]);
//			dto.setContent((String) objects[3]);
//			dto.setSubject((String) objects[4]);
//			dto.setCreatedBy((String) objects[5]);
//			dto.setCreatedDate((ZonedDateTime) objects[6]);
//			dto.setReply(Boolean.parseBoolean((String) objects[7]));
//			dtos.add(dto);
//		}
//		return dtos;
//	}
	public List<ChallengeWorkspaceAnswerDTO> getListAnswer(Set<ChallengeWorkspaceAnswer> set) {
		
		List<ChallengeWorkspaceAnswerDTO> answerDTOs = set.stream().map(answer -> {			
			return new ChallengeWorkspaceAnswerDTO(answer);
		}).collect(Collectors.toList());
		
		return answerDTOs;
	}
	public List<ChallengeWorkspaceQuestionDTO> getQuestionNotAnswer(Long workspaceId) {
		String sbQuery = "select q,"
							+" case when q.id not in (select q.id from ChallengeWorkspaceQuestion q, ChallengeWorkspaceAnswer a"
							+" where a.answerByType = 'BY_HOST' and q.id = a.question.id GROUP by q.id) then 'false' else 'true' end"
							+" from ChallengeWorkspaceQuestion q where q.workspace.id = :workspaceId order by q.createdDate";
                 
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
		//questionDTO.setAnswers(challengeWorkspaceService.getListAnswer(question.getAnswers()));
		return questionDTO;
	}
}
