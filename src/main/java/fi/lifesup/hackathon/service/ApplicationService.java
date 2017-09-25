package fi.lifesup.hackathon.service;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.lifesup.hackathon.domain.Application;
import fi.lifesup.hackathon.domain.User;
import fi.lifesup.hackathon.domain.enumeration.ApplicationStatus;
import fi.lifesup.hackathon.domain.enumeration.UserStatus;
import fi.lifesup.hackathon.repository.ApplicationRepository;
import fi.lifesup.hackathon.repository.ChallengeRepository;
import fi.lifesup.hackathon.service.dto.ApplicationDTO;

@Service
@Transactional
public class ApplicationService {

	private final Logger log = LoggerFactory.getLogger(ApplicationService.class);
	
	@Inject 
	private ChallengeRepository challengeRepository;
	
	@Inject 
	private ApplicationRepository applicationRepository;
	
	@Inject 
	private UserService userService;
	
	public boolean checkApplication(ApplicationDTO applicationDTO){
		
		if(applicationDTO.getTeamName() == null
				|| applicationDTO.getDescription() == null
				|| applicationDTO.getIdeasDesscription() == null
				|| applicationDTO.getMotivation() == null){
			return false;
		}
		return true;
	}
	
	public boolean checkMember(List<String> members){
		
		for (String member: members) {
			User user = userService.getUserWithAuthoritiesByEmail(member);
			if(user.getStatus() != UserStatus.PROFILE_COMPLETE)
				return false;
		}
		return true;
	}
	
	public Application createApplication(ApplicationDTO applicationDTO){
		
		Application application = new Application();
		application.setTeamName(applicationDTO.getTeamName());
		application.setCompanyName(applicationDTO.getCompanyName());
		application.setDescription(applicationDTO.getDescription());
		application.setIdeasDescription(applicationDTO.getIdeasDesscription());
		application.setMotivation(applicationDTO.getMotivation());
		if(checkApplication(applicationDTO)){
			application.setStatus(ApplicationStatus.WAITING_FOR_APPROVE);
		}
		else{
			application.setStatus(ApplicationStatus.DRAFT);
		}
		application.setChallenge(challengeRepository.findOne(applicationDTO.getChallengeId()));	
		return applicationRepository.save(application);
	}
	
public Application updateApplication(ApplicationDTO applicationDTO){
		
		Application application = new Application();
		application.setId(applicationDTO.getId());
		application.setTeamName(applicationDTO.getTeamName());
		application.setCompanyName(applicationDTO.getCompanyName());
		application.setDescription(applicationDTO.getDescription());
		application.setIdeasDescription(applicationDTO.getIdeasDesscription());
		application.setMotivation(applicationDTO.getMotivation());
		if(checkApplication(applicationDTO)){
			application.setStatus(ApplicationStatus.WAITING_FOR_APPROVE);
		}
		else{
			application.setStatus(ApplicationStatus.DRAFT);
		}
		application.setChallenge(challengeRepository.findOne(applicationDTO.getChallengeId()));	
		return applicationRepository.save(application);
	}
	
}
