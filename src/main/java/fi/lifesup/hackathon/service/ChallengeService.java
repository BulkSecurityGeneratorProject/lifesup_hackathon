package fi.lifesup.hackathon.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fi.lifesup.hackathon.domain.Challenge;
import fi.lifesup.hackathon.domain.User;
import fi.lifesup.hackathon.domain.util.JSR310PersistenceConverters.ZonedDateTimeConverter;
import fi.lifesup.hackathon.repository.ChallengeRepository;
import fi.lifesup.hackathon.repository.CompanyRepository;
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

	@Inject
	private CompanyRepository companyRepository;

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

	public List<Challenge> getChallengeByUser() {
		User user = userService.getUserWithAuthorities();
		return challengeRepository.getChallengeByUser(user.getId());
	}

	public Challenge saveChallenge(Challenge challenge) {
		User user = userService.getUserWithAuthorities();

		challenge.setCompany(user.getCompany());

		return challengeRepository.save(challenge);
	}

	public List<Challenge> getChallengeByDate() {
		// public List<Challenge> getChallengeByDate(String startDate, String
		// endDate) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E MMM d yyyy");
		String date1 = "Sat Sep 23 2017";
		String date2 = "Sat Sep 16 2017";
		ZonedDateTimeConverter convert = new ZonedDateTimeConverter();
		ZonedDateTime start = null;
		ZonedDateTime end = null;
		try {
			start = convert.convertToEntityAttribute(simpleDateFormat.parse(date1));
			end = convert.convertToEntityAttribute(simpleDateFormat.parse(date2));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return challengeRepository.getChallengeByDate(start, end);
	}
}
