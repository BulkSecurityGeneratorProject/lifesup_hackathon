package fi.lifesup.hackathon.service;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fi.lifesup.hackathon.domain.Challenge;
import fi.lifesup.hackathon.domain.User;
import fi.lifesup.hackathon.repository.ChallengeRepository;
import fi.lifesup.hackathon.repository.UserRepository;

@Service
@Transactional
public class ChallengeService {

	private final Logger log = LoggerFactory.getLogger(ChallengeService.class);

	@Inject
	private UserRepository userRepository;

	@Inject
	private UserService userService;

	@Inject
	private ChallengeRepository challengeRepository;

	public List<Challenge> getChallenges() {
		if (userService.checkAuthories("ROLE_ADMIN")) {
			return challengeRepository.findAll();
		}

		else {
			if (userService.checkAuthories("ROLE_HOST")) {
				User user = userService.getUserWithAuthorities();
				return challengeRepository.findByCompanyId(user.getCompany().getId());
			}
		}

		return null;
	}
	
	public List<Challenge> getChallengeByUser(){
		User user = userService.getUserWithAuthorities();
		return challengeRepository.getChallengeByUser(user.getId());
	}

}
