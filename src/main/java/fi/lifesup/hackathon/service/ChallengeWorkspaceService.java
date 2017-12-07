package fi.lifesup.hackathon.service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fi.lifesup.hackathon.domain.Challenge;
import fi.lifesup.hackathon.domain.ChallengeSubmission;
import fi.lifesup.hackathon.domain.ChallengeWorkspace;
import fi.lifesup.hackathon.repository.ChallengeRepository;
import fi.lifesup.hackathon.repository.ChallengeSubmissionRepository;
import fi.lifesup.hackathon.repository.ChallengeUserApplicationRepository;
import fi.lifesup.hackathon.repository.ChallengeWorkspaceRepository;
import fi.lifesup.hackathon.service.dto.ChallengeSubmissionDTO;
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
	
	@Inject
	private ChallengeSubmissionRepository challengeSubmissionRepository;

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
			
			List<ChallengeSubmissionDTO> challengeSubmissionDTOs = new ArrayList<>();
			List<ChallengeSubmission> challengeSubmissions = challengeSubmissionRepository.findByWorkspaceId(workspace.getId());
			for (ChallengeSubmission challengeSubmission : challengeSubmissions) {
				ChallengeSubmissionDTO dto = new ChallengeSubmissionDTO(challengeSubmission);
				challengeSubmissionDTOs.add(dto);
			}
					
			workspaceDTO.setChallengeSubmissionDTOs(challengeSubmissionDTOs);
			List<ChallengeWorkspaceQuestionDTO> questionDTOs = challengeWorkspaceQuestionService
					.getQuestionByWorkspace(workspace.getId(), challengeId);
			workspaceDTO.setWorkspaceQuestions(questionDTOs);

			return workspaceDTO;
		}
	}
}
