package fi.lifesup.hackathon.service;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import fi.lifesup.hackathon.domain.UserList;
import fi.lifesup.hackathon.repository.UserInfoRepository;
import fi.lifesup.hackathon.repository.UserListRepository;
import fi.lifesup.hackathon.repository.UserRepository;

@Service
@Transactional
public class UserListService {
	private final Logger log = LoggerFactory.getLogger(UserListService.class);
	@Inject
	private UserInfoRepository userInfoRepository;
	@Inject
	private UserRepository userRepository;
	@Inject
	private UserListRepository userListRepository;
//	public UserList updateAccount(){
//		return userListRepository.updateAccount();
//	
//	}

}
