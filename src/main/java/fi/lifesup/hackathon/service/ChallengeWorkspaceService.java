package fi.lifesup.hackathon.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fi.lifesup.hackathon.domain.Challenge;
import fi.lifesup.hackathon.domain.ChallengeUserApplication;
import fi.lifesup.hackathon.domain.ChallengeWorkspace;
import fi.lifesup.hackathon.domain.ChallengeWorkspaceAnswer;
import fi.lifesup.hackathon.domain.ChallengeWorkspaceQuestion;
import fi.lifesup.hackathon.repository.ChallengeRepository;
import fi.lifesup.hackathon.repository.ChallengeUserApplicationRepository;
import fi.lifesup.hackathon.repository.ChallengeWorkspaceRepository;
import fi.lifesup.hackathon.security.SecurityUtils;
import fi.lifesup.hackathon.service.dto.ChallengeWorkspaceAnswerDTO;
import fi.lifesup.hackathon.service.dto.ChallengeWorkspaceDTO;
import fi.lifesup.hackathon.service.dto.ChallengeWorkspaceNewsDTO;
import fi.lifesup.hackathon.service.dto.ChallengeWorkspaceQuestionDTO;

@Service
@Transactional
public class ChallengeWorkspaceService {

	private final Logger log = LoggerFactory.getLogger(ChallengeWorkspaceService.class);

	@Inject
	private ChallengeRepository challengeRepository;

	@Inject
	private ChallengeWorkspaceRepository challengeWorkspaceRepository;

	@Inject
	private ChallengeUserApplicationRepository challengeUserApplicationRepository;

	@Inject
	private ChallengeWorkspaceQuestionService challengeWorkspaceQuestionService;

	public ChallengeWorkspace saveChallengeWorksapce(ChallengeWorkspaceDTO workSpace) {
		ChallengeWorkspace challengeWorkspace = new ChallengeWorkspace();
		if (workSpace.getId() != null) {
			challengeWorkspace.setId(workSpace.getId());
		}
		Challenge challenge = challengeRepository.findOne(workSpace.getChallengeId());
		if (challenge == null) {
			return null;
		}
		challengeWorkspace.setChallenge(challenge);
		challengeWorkspace.setTermsAndConditions(workSpace.getTermsAndConditions());
		challengeWorkspace.setCreatedDate(ZonedDateTime.now());
		ChallengeWorkspace result = challengeWorkspaceRepository.save(challengeWorkspace);
		challenge.setWorkspaceId(result.getId());
		challengeRepository.save(challenge);

		return result;
	}

	public List<ChallengeWorkspaceAnswerDTO> getListAnswer(Set<ChallengeWorkspaceAnswer> set) {
		List<ChallengeWorkspaceAnswerDTO> answerDTOs = set.stream().map(answer -> {
			return new ChallengeWorkspaceAnswerDTO(answer);
		}).collect(Collectors.toList());
		return answerDTOs;
	}

	public List<ChallengeWorkspaceQuestionDTO> getListQuestion(Set<ChallengeWorkspaceQuestion> set, Long challengeId) {

		List<ChallengeWorkspaceQuestionDTO> list = null;
		if (SecurityUtils.isCurrentUserInRole("ROLE_HOST") || SecurityUtils.isCurrentUserInRole("ROLE_ADMIN")) {
			list = set.stream().map(question -> {
				ChallengeWorkspaceQuestionDTO questionDTO = new ChallengeWorkspaceQuestionDTO(question);
				questionDTO.setAnswers(getListAnswer(question.getAnswers()));
				return questionDTO;
			}).collect(Collectors.toList());
			return list;
		} else {
			ChallengeUserApplication challengeUserApplication = challengeUserApplicationRepository
					.getMemberByLogin(challengeId, SecurityUtils.getCurrentUserLogin());
			list = set.stream().map(question -> {
				if (question.getApplicationId().longValue() == challengeUserApplication.getApplicationId()
						.longValue()) {
					ChallengeWorkspaceQuestionDTO questionDTO = new ChallengeWorkspaceQuestionDTO(question);
					questionDTO.setAnswers(getListAnswer(question.getAnswers()));
					return questionDTO;
				}
				return null;
			}).collect(Collectors.toList());
			return list;
		}

	}

	public ChallengeWorkspaceDTO getChallengeWorkspaceDetail(Long challengeId) {

		ChallengeWorkspace workspace = challengeWorkspaceRepository.findByChallengeId(challengeId);
		if (workspace == null) {
			return null;
		} else {
			ChallengeWorkspaceDTO workspaceDTO = new ChallengeWorkspaceDTO(workspace);
			List<ChallengeWorkspaceNewsDTO> newsDTOs = workspace.getWorkspaceNews().stream().map(n -> {
				ChallengeWorkspaceNewsDTO news = new ChallengeWorkspaceNewsDTO(n);
				return news;
			}).collect(Collectors.toList());
			workspaceDTO.setWorkspaceNews(newsDTOs);

			List<ChallengeWorkspaceQuestionDTO> questionDTOs = getListQuestion(workspace.getWorkspaceQuestions(),
					challengeId);
			workspaceDTO.setWorkspaceQuestions(questionDTOs);

			return workspaceDTO;
		}
	}
}
