package fi.lifesup.hackathon.service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.lifesup.hackathon.domain.Application;
import fi.lifesup.hackathon.domain.ApplicationInviteEmail;
import fi.lifesup.hackathon.domain.ChallengeUserApplication;
import fi.lifesup.hackathon.domain.User;
import fi.lifesup.hackathon.domain.enumeration.ApplicationStatus;
import fi.lifesup.hackathon.domain.enumeration.UserStatus;
import fi.lifesup.hackathon.repository.ApplicationInviteEmailReponsitory;
import fi.lifesup.hackathon.repository.ApplicationRepository;
import fi.lifesup.hackathon.repository.ChallengeRepository;
import fi.lifesup.hackathon.repository.ChallengeUserApplicationRepository;
import fi.lifesup.hackathon.repository.UserInfoRepository;
import fi.lifesup.hackathon.repository.UserRepository;
import fi.lifesup.hackathon.service.dto.ApplicationBasicDTO;
import fi.lifesup.hackathon.service.dto.ApplicationDTO;
import fi.lifesup.hackathon.service.dto.ApplicationMemberDTO;
import fi.lifesup.hackathon.service.util.RandomUtil;
import liquibase.sqlgenerator.core.AddDefaultValueGeneratorPostgres;

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

	@Inject
	private UserInfoRepository userInfoRepository;

	@Inject
	private ApplicationInviteEmailReponsitory applicationInviteEmailReponsitory;

	public boolean checkApplication(Application application) {
		int check = 1;
		if (application.getTeamName() == null || application.getDescription() == null
				|| application.getIdeasDescription() == null || application.getMotivation() == null) {
			check = 0;
		}

		ApplicationBasicDTO basic = getApplicationBasic(application.getId());

		if (application.getChallenge().getMinTeamNumber() <= basic.getMembers().size()
				&& application.getChallenge().getMaxTeamNumber() >= basic.getMembers().size()) {
			check = 0;
		}

		for (String m : basic.getMembers()) {
			String[] s = m.split(",");
			if (!s[1].equals(UserStatus.PROFILE_COMPLETE.toString())) {
				check = 0;
				break;
			}
		}
		if (check == 0) {
			return false;
		}
		return true;
	}

	public String checkApplication(String s, Long id) {

		String s1 = challengeUserApplicationRepository.checkChallengeUserApplication(s, id);

		return s1;
	}

	public Application createApplication(ApplicationBasicDTO applicationDTO, String baseUrl) {

		Application application = new Application();
		application.setTeamName(applicationDTO.getTeamName());
		application.setCompanyName(applicationDTO.getCompanyName());
		application.setDescription(applicationDTO.getDescription());
		application.setIdeasDescription(applicationDTO.getIdeasDescription());
		application.setMotivation(applicationDTO.getMotivation());
		application.setStatus(ApplicationStatus.WAITING_FOR_APPROVE);

		application.setChallenge(challengeRepository.findOne(applicationDTO.getChallengeId()));
		Application result = applicationRepository.save(application);

		User user = userService.getUserWithAuthorities();
		ChallengeUserApplication userApplication = new ChallengeUserApplication();
		userApplication.setApplicationId(result.getId());
		userApplication.setChallengeId(applicationDTO.getChallengeId());
		userApplication.setUserId(user.getId());
		challengeUserApplicationRepository.save(userApplication);

		if (applicationDTO.getMembers() != null) {
			for (String a : applicationDTO.getMembers()) {

				ApplicationInviteEmail invited = new ApplicationInviteEmail();
				invited.setEmail(a);
				invited.setApplication(result);
				invited.setSend(true);
				invited.setSendTime(ZonedDateTime.now());
				invited.setAcceptKey(RandomUtil.generateAcceptKey());
				applicationInviteEmailReponsitory.save(invited);
				mailService.sendInvitationMail(invited, baseUrl);

			}
		}

		if (checkApplication(result)) {
			result.setStatus(ApplicationStatus.WAITING_FOR_APPROVE);
		} else {
			result.setStatus(ApplicationStatus.DRAFT);
		}

		return applicationRepository.save(result);
	}

	public Application updateApplication(ApplicationBasicDTO applicationDTO, String baseUrl) {
		
		Application application = applicationRepository.findOne(applicationDTO.getId());
		application.setTeamName(applicationDTO.getTeamName());
		application.setCompanyName(applicationDTO.getCompanyName());
		application.setDescription(applicationDTO.getDescription());
		application.setIdeasDescription(applicationDTO.getIdeasDescription());
		application.setMotivation(applicationDTO.getMotivation());

		if (!applicationDTO.getMembers().isEmpty()) {
			for (String a : applicationDTO.getMembers()) {
				ApplicationInviteEmail invited = new ApplicationInviteEmail();
				invited.setEmail(a);
				invited.setApplication(application);
				invited.setSend(true);
				invited.setSendTime(ZonedDateTime.now());
				invited.setAcceptKey(RandomUtil.generateAcceptKey());
				applicationInviteEmailReponsitory.save(invited);
				mailService.sendInvitationMail(invited, baseUrl);

			}
		}

		if (checkApplication(application)) {
			application.setStatus(ApplicationStatus.WAITING_FOR_APPROVE);
		} else {
			application.setStatus(ApplicationStatus.DRAFT);
		}
		return applicationRepository.save(application);
	}

	public ApplicationDTO getApplicationDetail(Long applicationId) {
		ApplicationDTO dto = applicationRepository.getapplicationById(applicationId);
		List<ChallengeUserApplication> members = challengeUserApplicationRepository.findByApplicationId(applicationId);
		List<ApplicationInviteEmail> invites = applicationInviteEmailReponsitory.findByApplicationId(applicationId);
		List<ApplicationMemberDTO> memberDtos = new ArrayList<>();
		for (ChallengeUserApplication m : members) {
			User u = userService.getUserWithAuthorities(m.getUserId());
			ApplicationMemberDTO d = new ApplicationMemberDTO();
			d.setId(m.getId());
			d.setUserId(m.getUserId());
			d.setInvitedMail(u.getEmail());
			d.setFirstName(u.getFirstName());
			d.setLastName(u.getLastName());
			if (u.getUserInfo() != null) {
				d.setPhone(u.getUserInfo().getPhone());
				d.setSex(u.getUserInfo().getSex());
				d.setCompanyName(u.getUserInfo().getCompanyName());
				d.setJobTitle(u.getUserInfo().getJobTitle());
				d.setLogoUrl(u.getUserInfo().getLogoUrl());
				d.setCountry(u.getUserInfo().getCountry());
				d.setCity(u.getUserInfo().getCity());
				d.setNationality(u.getUserInfo().getNationality());
				d.setBirthday(u.getUserInfo().getBirthday());
				d.setIntroduction(u.getUserInfo().getIntroduction());
				d.setTwitterUrl(u.getUserInfo().getTwitterUrl());
				d.setWebsiteUrl(u.getUserInfo().getWebsiteUrl());
				d.setLinkedInUrl(u.getUserInfo().getLinkedInUrl());
				d.setSkills(u.getUserInfo().getSkills());
				d.setWorkArea(u.getUserInfo().getWorkArea());
				d.setFeedbackFrom(u.getUserInfo().getFeedbackFrom());
			}
			memberDtos.add(d);
		}
		for (ApplicationInviteEmail a : invites) {
			ApplicationMemberDTO d = new ApplicationMemberDTO();
			d.setInvitedMail(a.getEmail());
			memberDtos.add(d);
		}
		dto.setMembers(memberDtos);
		return dto;
	}

	public ApplicationBasicDTO getApplicationBasic(Long id) {
		Application application = applicationRepository.findOne(id);
		List<String> emails = new ArrayList<>();
		ApplicationBasicDTO dto = new ApplicationBasicDTO();
		dto.setId(id);
		dto.setTeamName(application.getTeamName());
		dto.setChallengeId(application.getChallenge().getId());
		dto.setCompanyName(application.getCompanyName());
		dto.setDescription(application.getDescription());
		dto.setMotivation(application.getMotivation());
		dto.setIdeasDescription(application.getIdeasDescription());
		dto.setStatus(application.getStatus());
		List<ChallengeUserApplication> members = challengeUserApplicationRepository.findByApplicationId(id);
		List<ApplicationInviteEmail> invites = applicationInviteEmailReponsitory.findByApplicationId(id);
		for (ChallengeUserApplication m : members) {
			User user = userService.getUserWithAuthorities(m.getUserId());
			String s = user.getEmail() + "," + user.getStatus();
			emails.add(s);
		}
		for (ApplicationInviteEmail a : invites) {
			String s = a.getEmail() + ",false";
			emails.add(s);
		}
		dto.setMembers(emails);
		return dto;
	}

	public void addChallengeUserApplication(ApplicationInviteEmail a, Long userId) {
		ChallengeUserApplication userApplication = new ChallengeUserApplication();
		userApplication.setUserId(userId);
		userApplication.setApplicationId(a.getApplication().getId());
		userApplication.setChallengeId(a.getApplication().getChallenge().getId());
		challengeUserApplicationRepository.save(userApplication);
		applicationInviteEmailReponsitory.delete(a);
	}

	public void finishAcceptInvitation(String key, Boolean accept) {
		ApplicationInviteEmail inviteEmail = applicationInviteEmailReponsitory.findByAcceptKey(key);
		if (accept.booleanValue() == true) {
			User user = userService.getUserWithAuthoritiesByEmail(inviteEmail.getEmail());
			addChallengeUserApplication(inviteEmail, user.getId());

		} else {
			applicationInviteEmailReponsitory.deleteByAcceptKey(key);

		}
		if (checkApplication(inviteEmail.getApplication())) {
			inviteEmail.getApplication().setStatus(ApplicationStatus.WAITING_FOR_APPROVE);
		} else {
			inviteEmail.getApplication().setStatus(ApplicationStatus.DRAFT);
		}
		applicationRepository.save(inviteEmail.getApplication());

	}

	public void deleteApplication(Long id) {
		challengeUserApplicationRepository.deleteByApplicationId(id);
		applicationRepository.delete(id);
	}

	public List<String> checkApplication(Long id) {
		List<String> list = new ArrayList<>();
		Application a = applicationRepository.findOne(id);
		User user = userService.getUserWithAuthorities();
		List<ChallengeUserApplication> members = challengeUserApplicationRepository.findByApplicationId(id);
		List<ApplicationInviteEmail> invites = applicationInviteEmailReponsitory.findByApplicationId(id);
		int check = 0;
		for (ChallengeUserApplication challengeUserApplication : members) {
			if (challengeUserApplication.getUserId().longValue() == user.getId().longValue()) {
				check = 1;
				break;
			}
		}
		if (check == 1) {
			if (user != null) {
				list.add("Create account or login," + true);
			} else {
				list.add("Create account or login," + false);
			}

			list.add("Verify your account," + user.getActivated());
			if (user.getStatus() == UserStatus.PROFILE_COMPLETE) {
				list.add("Complete your profile," + true);
			} else {
				list.add("Complete your profile," + false);
			}
		}

		if (a.getTeamName() != null) {
			list.add("Name your team," + true);
		} else {
			list.add("Name your team," + false);
		}
		if (a.getDescription() != null) {
			list.add("Describe your team," + true);
		} else {
			list.add("Describe your team," + false);
		}
		if (a.getMotivation() != null) {
			list.add("Fill in your motivation," + true);
		} else {
			list.add("Fill in your motivation," + false);
		}
		if (a.getIdeasDescription() != null) {
			list.add("Fill in your idea," + true);
		} else {
			list.add("Fill in your idea," + false);
		}

		if (check == 1) {
			if (a.getChallenge().getMinTeamNumber() != 1 || members.size() > 1 || !invites.isEmpty()) {
				if (!invites.isEmpty() || members.size() > 1) {
					list.add("Invite other team members," + true);
				} else {
					list.add("Invite other team members," + false);
				}
			}
			if (invites.isEmpty() && members.size() == 1 && a.getChallenge().getMinTeamNumber() == 1) {
				return list;
			} else {
				if (invites.isEmpty() && members.size() > 1) {
					list.add("Other members have accepted their invitation," + true);
				} else {
					list.add("Other members have accepted their invitation," + false);
				}
				if (!invites.isEmpty() || members.size() == 1) {
					list.add("Other member have completed their profile," + false);
				} else {
					List<UserStatus> s = challengeUserApplicationRepository.checkUserStatus(id);
					for (UserStatus userStatus : s) {
						if (userStatus != UserStatus.PROFILE_COMPLETE || !invites.isEmpty()) {
							list.add("Other member have completed their profile," + false);
							return list;
						}
					}
					list.add("Other member have completed their profile," + true);
				}
			}
		} else {
			if (a.getChallenge().getMinTeamNumber() != 1 || members.size() > 1 || !invites.isEmpty()) {
				if (!invites.isEmpty() || members.size() > 1) {
					list.add("Invite other team members," + true);
				} else {
					list.add("Invite other team members," + false);
				}

				if (invites.isEmpty() && members.size() == 1 && a.getChallenge().getMinTeamNumber() == 1) {
					list.add("All member have completed their profile," + true);
					list.add("All members have accepted their invitation," + true);
					return list;
				}
				if (invites.isEmpty() && members.size() > 1) {
					list.add("All members have accepted their invitation," + true);
				} else {
					list.add("All members have accepted their invitation," + false);
				}
				if (!invites.isEmpty() || members.size() == 1) {
					list.add("All member have completed their profile," + false);
				} else {
					List<UserStatus> s = challengeUserApplicationRepository.checkUserStatus(id);
					for (UserStatus userStatus : s) {
						if (userStatus != UserStatus.PROFILE_COMPLETE || !invites.isEmpty()) {
							list.add("All member have completed their profile," + false);
							return list;
						}
					}
					list.add("All member have completed their profile," + true);
				}
			}

		}
		if(checkApplication(a)){
			a.setStatus(ApplicationStatus.WAITING_FOR_APPROVE);
		} else {
			a.setStatus(ApplicationStatus.DRAFT);
		}
		applicationRepository.save(a);
		return list;
	}

	public void deleteMember(Long applicationId, String email) {
		System.err.println(email);
		ChallengeUserApplication userApplication = challengeUserApplicationRepository.getMember(applicationId, email);
		if (userApplication == null) {
			ApplicationInviteEmail a = applicationInviteEmailReponsitory.findByApplicationIdAndEmail(applicationId,
					email);
			applicationInviteEmailReponsitory.deleteByApplicationIdAndEmailLike(applicationId, email + "%");
		} else {
			challengeUserApplicationRepository.delete(userApplication);
		}
	}

	public String check(Long id, String email) {
		return challengeUserApplicationRepository.checkChallengeUserApplication(email, id);
	}

}
