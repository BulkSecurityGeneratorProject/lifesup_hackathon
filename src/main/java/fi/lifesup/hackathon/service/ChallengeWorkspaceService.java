package fi.lifesup.hackathon.service;

import java.time.ZonedDateTime;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fi.lifesup.hackathon.domain.Challenge;
import fi.lifesup.hackathon.domain.ChallengeWorkspace;
import fi.lifesup.hackathon.repository.ChallengeRepository;
import fi.lifesup.hackathon.repository.ChallengeWorkspaceRepository;
import fi.lifesup.hackathon.service.dto.ChallengeWorkspaceDTO;

@Service
@Transactional
public class ChallengeWorkspaceService {

	private final Logger log = LoggerFactory.getLogger(ChallengeWorkspaceService.class);
	
	@Inject
	private ChallengeRepository challengeRepository;
	
	@Inject 
	private ChallengeWorkspaceRepository challengeWorkspaceRepository;

	
	public ChallengeWorkspace saveChallengeWorksapce(ChallengeWorkspaceDTO workSpace){
		ChallengeWorkspace challengeWorkspace = new ChallengeWorkspace();
		if(workSpace.getId() != null){
			challengeWorkspace.setId(workSpace.getId());
		}		
		Challenge challenge = challengeRepository.findOne(workSpace.getChallengeId());
		challengeWorkspace.setChallenge(challenge);
		challengeWorkspace.setTermsAndConditions(workSpace.getTermsAndConditions());
		challengeWorkspace.setCreatedDate(ZonedDateTime.now());
        ChallengeWorkspace result = challengeWorkspaceRepository.save(challengeWorkspace);
		return result;
	}
}
