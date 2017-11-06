package fi.lifesup.hackathon.service;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.lifesup.hackathon.domain.ApplicationInviteEmail;
import fi.lifesup.hackathon.domain.Authority;
import fi.lifesup.hackathon.domain.Company;
import fi.lifesup.hackathon.domain.User;
import fi.lifesup.hackathon.domain.enumeration.UserStatus;
import fi.lifesup.hackathon.repository.ApplicationInviteEmailReponsitory;
import fi.lifesup.hackathon.repository.AuthorityRepository;
import fi.lifesup.hackathon.repository.UserInfoRepository;
import fi.lifesup.hackathon.repository.UserRepository;
import fi.lifesup.hackathon.security.AuthoritiesConstants;
import fi.lifesup.hackathon.security.SecurityUtils;
import fi.lifesup.hackathon.service.util.RandomUtil;
import fi.lifesup.hackathon.web.rest.vm.ManagedUserVM;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

	private final Logger log = LoggerFactory.getLogger(UserService.class);

	@Inject
	private PasswordEncoder passwordEncoder;

	@Inject
	private UserRepository userRepository;

	@Inject
	private AuthorityRepository authorityRepository;

	@Inject
	private UserInfoService userInfoService;
	@Inject
	private UserInfoRepository userInfoRepository;
	
	@Inject
	private ApplicationInviteEmailReponsitory applicationInviteEmailReponsitory;
	
	@Inject
	private ApplicationService applicationService;

	public Optional<User> activateRegistration(String key) {
		log.debug("Activating user for activation key {}", key);
		return userRepository.findOneByActivationKey(key).map(user -> {
			// activate given user for the registration key.
			user.setActivated(true);
			user.setActivationKey(null);
			user.setStatus(UserStatus.ACTIVATED);
			userRepository.save(user);
			log.debug("Activated user: {}", user);
			return user;
		});
	}

	public Optional<User> completePasswordReset(String newPassword, String key) {
		log.debug("Reset user password for reset key {}", key);

		return userRepository.findOneByResetKey(key).filter(user -> {
			ZonedDateTime oneDayAgo = ZonedDateTime.now().minusHours(24);
			return user.getResetDate().isAfter(oneDayAgo);
		}).map(user -> {
			user.setPassword(passwordEncoder.encode(newPassword));
			user.setResetKey(null);
			user.setResetDate(null);
			userRepository.save(user);
			return user;
		});
	}

	public Optional<User> requestPasswordReset(String mail) {
		return userRepository.findOneByEmail(mail).filter(User::getActivated).map(user -> {
			user.setResetKey(RandomUtil.generateResetKey());
			user.setResetDate(ZonedDateTime.now());
			userRepository.save(user);
			return user;
		});
	}

	public User createUser(String login, String password, String firstName, String lastName, String email,
			String langKey) {
		User newUser = new User();
		Authority authority = authorityRepository.findOne(AuthoritiesConstants.USER);
		Set<Authority> authorities = new HashSet<>();
		String encryptedPassword = passwordEncoder.encode(password);
		newUser.setLogin(login);
		// new user gets initially a generated password
		newUser.setPassword(encryptedPassword);
		newUser.setFirstName(firstName);
		newUser.setLastName(lastName);
		newUser.setEmail(email);
		newUser.setLangKey(langKey);
		// new user is not active
		newUser.setActivated(false);
		// new user gets registration key
		newUser.setActivationKey(RandomUtil.generateActivationKey());
		authorities.add(authority);
		newUser.setAuthorities(authorities);
		newUser.setStatus(UserStatus.INACTIVATED);
		newUser = userRepository.save(newUser);
		
		log.debug("Created Information for User: {}", newUser);
		return newUser;
	}

	public User createUser(ManagedUserVM managedUserVM, String password) {
		User user = new User();
		user.setLogin(managedUserVM.getLogin());
		user.setFirstName(managedUserVM.getFirstName());
		user.setLastName(managedUserVM.getLastName());
		user.setEmail(managedUserVM.getEmail());
		if(managedUserVM.getCompany() != null){
			user.setCompany(managedUserVM.getCompany());
		}
		if (managedUserVM.getLangKey() == null) {
			user.setLangKey("en"); // default language
		} else {
			user.setLangKey(managedUserVM.getLangKey());
		}
		if (managedUserVM.getAuthorities() != null) {
			Set<Authority> authorities = new HashSet<>();
			managedUserVM.getAuthorities().stream()
					.forEach(authority -> authorities.add(authorityRepository.findOne(authority)));
			user.setAuthorities(authorities);
		}	
		String encryptedPassword = passwordEncoder.encode(password);
		user.setPassword(encryptedPassword);
		user.setResetKey(RandomUtil.generateResetKey());
		user.setResetDate(ZonedDateTime.now());
		user.setActivated(true);
		user.setStatus(UserStatus.ACTIVATED);
		userRepository.save(user);
		log.debug("Created Information for User: {}", user);
		return user;
	}

	public void updateUser(String firstName, String lastName, String email, String langKey, Company company) {
		userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(u -> {
			u.setFirstName(firstName);
			u.setLastName(lastName);
			u.setEmail(email);
			u.setLangKey(langKey);
			u.setStatus(UserStatus.ACTIVATED);
			if(company != null){
				u.setCompany(company);
			}
			userRepository.save(u);
			log.debug("Changed Information for User: {}", u);
		});
	}

	public void updateUser(Long id, String login, String firstName, String lastName, String email, boolean activated,
			String langKey, Set<String> authorities) {

		Optional.of(userRepository.findOne(id)).ifPresent(u -> {
			u.setLogin(login);
			u.setFirstName(firstName);
			u.setLastName(lastName);
			u.setEmail(email);
			u.setActivated(activated);
			u.setLangKey(langKey);
			// u.setUserInfo(userInfoId);
			Set<Authority> managedAuthorities = u.getAuthorities();
			managedAuthorities.clear();
			authorities.stream().forEach(authority -> managedAuthorities.add(authorityRepository.findOne(authority)));
			log.debug("Changed Information for User: {}", u);
		});
	}

	public void deleteUser(String login) {
		userRepository.findOneByLogin(login).ifPresent(u -> {
			userRepository.delete(u);
			log.debug("Deleted User: {}", u);
		});
	}

	public void changePassword(String password) {
		userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(u -> {
			String encryptedPassword = passwordEncoder.encode(password);
			u.setPassword(encryptedPassword);
			userRepository.save(u);
			log.debug("Changed password for User: {}", u);
		});
	}

	@Transactional(readOnly = true)
	public Optional<User> getUserWithAuthoritiesByLogin(String login) {
		return userRepository.findOneByLogin(login).map(u -> {
			u.getAuthorities().size();
			return u;
		});
	}

	@Transactional(readOnly = true)
	public User getUserWithAuthorities(Long id) {
		User user = userRepository.findOne(id);
		user.getAuthorities().size(); // eagerly load the association
		return user;
	}

	@Transactional(readOnly = true)
	public User getUserWithAuthorities() {
		Optional<User> optionalUser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
		User user = null;
		if (optionalUser.isPresent()) {
			user = optionalUser.get();
			user.getAuthorities().size(); // eagerly load the association
		}
		return user;
	}

	/**
	 * Not activated users should be automatically deleted after 3 days.
	 * <p>
	 * This is scheduled to get fired everyday, at 01:00 (am).
	 * </p>
	 */
	@Scheduled(cron = "0 0 1 * * ?")
	public void removeNotActivatedUsers() {
		ZonedDateTime now = ZonedDateTime.now();
		List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3));
		for (User user : users) {
			log.debug("Deleting not activated user {}", user.getLogin());
			userRepository.delete(user);
		}
	}

	public User getCurrentUser() {
		User user = getUserWithAuthorities();
		return user;
	}
	
	@Transactional(readOnly = true)
	public User getUserWithAuthoritiesByEmail(String email) {
		Optional<User> optionalUser = userRepository.findOneByEmail(email);
		User user = null;
		if (optionalUser.isPresent()) {
			user = optionalUser.get();
			user.getAuthorities().size(); // eagerly load the association
		}
		return user;
	}

}
