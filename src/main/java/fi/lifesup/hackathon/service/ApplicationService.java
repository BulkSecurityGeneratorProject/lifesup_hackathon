package fi.lifesup.hackathon.service;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.lifesup.hackathon.domain.Application;
import fi.lifesup.hackathon.domain.ChallengeUserApplication;
import fi.lifesup.hackathon.domain.User;
import fi.lifesup.hackathon.domain.enumeration.ApplicationStatus;
import fi.lifesup.hackathon.domain.enumeration.UserStatus;
import fi.lifesup.hackathon.repository.ApplicationRepository;
import fi.lifesup.hackathon.repository.ChallengeRepository;
import fi.lifesup.hackathon.repository.ChallengeUserApplicationRepository;
import fi.lifesup.hackathon.service.dto.ApplicationDTO;
import fi.lifesup.hackathon.service.dto.ApplicationMemberDTO;

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

	public boolean checkApplication(ApplicationDTO applicationDTO) {

		if (applicationDTO.getTeamName() == null || applicationDTO.getDescription() == null
				|| applicationDTO.getIdeasDesscription() == null || applicationDTO.getMotivation() == null) {
			return false;
		}
		return true;
	}

	public Application createApplication(ApplicationDTO applicationDTO) {

		Application application = new Application();
		application.setTeamName(applicationDTO.getTeamName());
		application.setCompanyName(applicationDTO.getCompanyName());
		application.setDescription(applicationDTO.getDescription());
		application.setIdeasDescription(applicationDTO.getIdeasDesscription());
		application.setMotivation(applicationDTO.getMotivation());
		application.setStatus(ApplicationStatus.WAITING_FOR_APPROVE);

		if (!checkApplication(applicationDTO)) {
			application.setStatus(ApplicationStatus.DRAFT);
		}
		Application result = applicationRepository.save(application);
		
		User user = userService.getUserWithAuthorities();
		ChallengeUserApplication userApplication = new ChallengeUserApplication();
		userApplication.setApplicationId(result.getId());
		userApplication.setChallengeId(applicationDTO.getChallengeId());
		userApplication.setUserId(user.getId());
		challengeUserApplicationRepository.save(userApplication);
//		application.setChallenge(challengeRepository.findOne(applicationDTO.getChallengeId()));
//		
//		for (String member : applicationDTO.getMembers()) {
//			User user = userService.getUserWithAuthoritiesByEmail(member);
//			if (user != null) {
//				ChallengeUserApplication userApplication = new ChallengeUserApplication();
//				userApplication.setApplicationId(result.getId());
//				userApplication.setChallengeId(applicationDTO.getChallengeId());
//				userApplication.setUserId(user.getId());
//				challengeUserApplicationRepository.save(userApplication);
//				if (user.getStatus() != UserStatus.PROFILE_COMPLETE) {
//					application.setStatus(ApplicationStatus.DRAFT);
//				}
//			}
//
//		}
		return applicationRepository.save(result);
	}

	public Application updateApplication(ApplicationDTO applicationDTO) {

		Application application = new Application();
		application.setId(applicationDTO.getId());
		application.setTeamName(applicationDTO.getTeamName());
		application.setCompanyName(applicationDTO.getCompanyName());
		application.setDescription(applicationDTO.getDescription());
		application.setIdeasDescription(applicationDTO.getIdeasDesscription());
		application.setMotivation(applicationDTO.getMotivation());
		if (checkApplication(applicationDTO)) {
			application.setStatus(ApplicationStatus.WAITING_FOR_APPROVE);
		} else {
			application.setStatus(ApplicationStatus.DRAFT);
		}
//		challengeUserApplicationRepository.deleteByApplicationId(applicationDTO.getId());

//		for (String member : applicationDTO.getMembers()) {
//			User user = userService.getUserWithAuthoritiesByEmail(member);
//			if (user != null) {
//				ChallengeUserApplication userApplication = new ChallengeUserApplication();
//				userApplication.setApplicationId(applicationDTO.getId());
//				userApplication.setChallengeId(applicationDTO.getChallengeId());
//				userApplication.setUserId(user.getId());
//				challengeUserApplicationRepository.save(userApplication);
//				if (user.getStatus() != UserStatus.PROFILE_COMPLETE) {
//					application.setStatus(ApplicationStatus.DRAFT);
//				}
//			}
//
//		}
		application.setChallenge(challengeRepository.findOne(applicationDTO.getChallengeId()));
		return applicationRepository.save(application);
	}

	public ApplicationDTO getApplicationDetail(Long applicationId) {
		Application application = applicationRepository.findOne(applicationId);
		ApplicationDTO dto = new ApplicationDTO(application,
				challengeUserApplicationRepository.getApplicationMember(applicationId));
		return dto;
	}

	public ChallengeUserApplication addMemberApplicaion(ApplicationMemberDTO memberDTO){
		User user = userService.getUserWithAuthoritiesByEmail(memberDTO.getUserEmail());
		ChallengeUserApplication userApplication = new ChallengeUserApplication();
		userApplication.setApplicationId(memberDTO.getApplicatonId());
		userApplication.setChallengeId(memberDTO.getChallengeId());
		userApplication.setUserId(user.getId());
		return challengeUserApplicationRepository.save(userApplication);
	}
}
