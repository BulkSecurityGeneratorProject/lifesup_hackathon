package fi.lifesup.hackathon.service;

import java.time.ZonedDateTime;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fi.lifesup.hackathon.domain.Challenge;
import fi.lifesup.hackathon.domain.ChallengeInfo;
import fi.lifesup.hackathon.domain.UserList;
import fi.lifesup.hackathon.domain.enumeration.ChallengeStatus;
import fi.lifesup.hackathon.repository.ChallengeRepository;
import fi.lifesup.hackathon.repository.UserListRepository;
import fi.lifesup.hackathon.security.SecurityUtils;

@Service
@Transactional
public class ChallengeService {

	private final Logger log = LoggerFactory.getLogger(ChallengeService.class);
	
	@Inject
	private UserService userService;
	
	@Inject
	private ChallengeRepository challengeRepository;
	
	@Inject
	private UserListRepository userListRepository;
	
	public void updateStatus(ChallengeInfo challengeInfo){
		
		if(challengeInfo.getActiveTime().equals(ZonedDateTime.now())){
			challengeInfo.setStatus(ChallengeStatus.ACTIVE);
		}
	}
	
	public List<Challenge> getChallenges(){
		
		if(userService.checkAuthories("ROLE_ADMIN")){
			return challengeRepository.findAll();
		}
		else{
			if(userService.checkAuthories("ROLE_HOST")){
				UserList userList = userListRepository.findByEmail(SecurityUtils.getCurrentUserLogin());
				return challengeRepository.findByCompanyId(userList.getCompany().getId());
			}
		}
		return null;
	}
	
}
