package fi.lifesup.hackathon.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.lifesup.hackathon.domain.Application;
import fi.lifesup.hackathon.domain.Challenge;
import fi.lifesup.hackathon.domain.ChallengeUserApplication;
import fi.lifesup.hackathon.domain.User;
import fi.lifesup.hackathon.domain.enumeration.ApplicationStatus;
import fi.lifesup.hackathon.domain.enumeration.ChallengeUserApplicationStatus;
import fi.lifesup.hackathon.domain.enumeration.UserStatus;
import fi.lifesup.hackathon.repository.ApplicationRepository;
import fi.lifesup.hackathon.repository.ChallengeRepository;
import fi.lifesup.hackathon.repository.ChallengeUserApplicationRepository;
import fi.lifesup.hackathon.repository.UserRepository;
import fi.lifesup.hackathon.service.dto.ApplicationDTO;
import fi.lifesup.hackathon.service.dto.ApplicationMemberDTO;
import fi.lifesup.hackathon.service.util.RandomUtil;

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

	@Inject
	private ChallengeUserApplicationRepository challengeUserApplicationRepository;

	@Inject
	private MailService mailService;
	
	@Inject 
	private UserRepository userRepository; 

	public boolean checkApplication(ApplicationDTO applicationDTO) {

		if (applicationDTO.getTeamName() == null || applicationDTO.getDescription() == null
				|| applicationDTO.getIdeasDescription() == null || applicationDTO.getMotivation() == null) {
			return false;
		}
		return true;
	}

	public Application createApplication(ApplicationDTO applicationDTO) {

		Application application = new Application();
		application.setTeamName(applicationDTO.getTeamName());
		application.setCompanyName(applicationDTO.getCompanyName());
		application.setDescription(applicationDTO.getDescription());
		application.setIdeasDescription(applicationDTO.getIdeasDescription());
		application.setMotivation(applicationDTO.getMotivation());
		application.setStatus(ApplicationStatus.WAITING_FOR_APPROVE);

		if (!checkApplication(applicationDTO)) {
			application.setStatus(ApplicationStatus.DRAFT);
		}

		application.setChallenge(challengeRepository.findOne(applicationDTO.getChallengeId()));
		Application result = applicationRepository.save(application);
		User user = userService.getUserWithAuthorities();
		ChallengeUserApplication userApplication = new ChallengeUserApplication();
		userApplication.setApplicationId(result.getId());
		userApplication.setChallengeId(applicationDTO.getChallengeId());
		userApplication.setUserId(user.getId());
		userApplication.setStatus(ChallengeUserApplicationStatus.ACCEPT);
		challengeUserApplicationRepository.save(userApplication);
		return applicationRepository.save(result);
	}

	public Application updateApplication(ApplicationDTO applicationDTO) {

		Application application = new Application();
		application.setId(applicationDTO.getId());
		application.setTeamName(applicationDTO.getTeamName());
		application.setCompanyName(applicationDTO.getCompanyName());
		application.setDescription(applicationDTO.getDescription());
		application.setIdeasDescription(applicationDTO.getIdeasDescription());
		application.setMotivation(applicationDTO.getMotivation());
		if (checkApplication(applicationDTO)) {
			application.setStatus(ApplicationStatus.WAITING_FOR_APPROVE);
		} else {
			application.setStatus(ApplicationStatus.DRAFT);
		}

		application.setChallenge(challengeRepository.findOne(applicationDTO.getChallengeId()));
		return applicationRepository.save(application);
	}

	public ApplicationDTO getApplicationDetail(Long applicationId) {
		
		Application application = applicationRepository.findOne(applicationId);
		ApplicationDTO dto = new ApplicationDTO(application,
				challengeUserApplicationRepository.getApplicationMember(applicationId));
		return dto;
	}
	
public ApplicationDTO getApplicationDetail(String acceptKey) {
		
		Application application = applicationRepository.getapplication(acceptKey);
		ApplicationDTO dto = new ApplicationDTO(application,
				challengeUserApplicationRepository.getApplicationMember(application.getId()));
		return dto;
	}

	public ChallengeUserApplication addApplicationMember(ApplicationMemberDTO memberDTO, String baseUrl) {

		ChallengeUserApplication userApplication = new ChallengeUserApplication();
		userApplication.setApplicationId(memberDTO.getApplicationId());
		userApplication.setChallengeId(memberDTO.getChallengeId());
		userApplication.setAcceptKey(RandomUtil.generateAcceptKey());
		userApplication.setStatus(ChallengeUserApplicationStatus.DECLINE);
		userApplication.setInvitedMail(memberDTO.getUserEmail());

		mailService.sendInvitationMail(memberDTO, baseUrl, userApplication.getAcceptKey());
		return challengeUserApplicationRepository.save(userApplication);

	}

	public String finishAcceptInvitation(String key, Boolean accept) {
		if(accept.booleanValue() == true){
			ChallengeUserApplication userApplication = challengeUserApplicationRepository.findByAcceptKey(key);
		userApplication.setAcceptKey(null);
		userApplication.setUserId(userRepository.getUserByAcceptKey(key));
		userApplication.setStatus(ChallengeUserApplicationStatus.ACCEPT);
		challengeUserApplicationRepository.save(userApplication);
		return "User accepted!";
		}
		else{
			challengeUserApplicationRepository.deleteByAcceptKey(key);
			return "User declined?";
		}
		
	}
	
	public void deleteByKey(String key) {
		challengeUserApplicationRepository.deleteByAcceptKey(key);
	}

	public void deleteApplication(Long id){
		challengeUserApplicationRepository.deleteByApplicationId(id);
		applicationRepository.delete(id);
	}
	
	public Boolean[] checkApplication(Long id){
		Boolean[] list = new Boolean[10];
		for (int i = 0; i < list.length; i++) {
			list[i]=false;
		}
		
		ApplicationDTO dto =this.getApplicationDetail(id);
		
		List<ApplicationMemberDTO> memberStatus = challengeUserApplicationRepository.getMemberStatus(id);
		
		User user = userService.getUserWithAuthorities();
		if(user != null){
			list[0] = true;
		}
		
		list[1] = user.getActivated();
		if(user.getStatus() == UserStatus.PROFILE_COMPLETE){
			list[2] = true;
		}
		
		if(dto.getTeamName() != null){
			list[3] = true;
		}
		
		if(dto.getDescription() != null){
			list[4] = true;
		}
		
		if(dto.getMotivation() != null){
			list[5] = true;
		}
		
		if(dto.getIdeasDescription() != null){
			list[6] = true;
		}
		
		if(dto.getMembers().size() > 1){
			list[7] = true;
		}
		
		for (ApplicationMemberDTO member : memberStatus) {
			if(member.getStatus() == ChallengeUserApplicationStatus.ACCEPT){
				list[8] = true;
			}
			if(member.getUserStatus() == UserStatus.PROFILE_COMPLETE){
				list[9] = true;
			}
		}
		
		return list;		
	}
}
