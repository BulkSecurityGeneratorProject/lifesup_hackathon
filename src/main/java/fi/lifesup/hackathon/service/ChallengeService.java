package fi.lifesup.hackathon.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.List;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fi.lifesup.hackathon.domain.Challenge;
import fi.lifesup.hackathon.domain.User;
import fi.lifesup.hackathon.domain.util.JSR310PersistenceConverters.ZonedDateTimeConverter;
import fi.lifesup.hackathon.repository.ChallengeRepository;
import fi.lifesup.hackathon.repository.ChallengeUserApplicationRepository;
import fi.lifesup.hackathon.repository.CompanyRepository;
import fi.lifesup.hackathon.repository.UserRepository;
import fi.lifesup.hackathon.service.dto.ChallengeImageDTO;

@Service
@Transactional
public class ChallengeService {

	private final Logger log = LoggerFactory.getLogger(ChallengeService.class);

	@Value("src/main/webapp/content/images")
	private String attachPath;

	@Inject
	private UserRepository userRepository;

	@Inject
	private UserService userService;

	@Inject
	private ChallengeRepository challengeRepository;

	@Inject
	private CompanyRepository companyRepository;
	
	@Inject 
	private ChallengeUserApplicationRepository challengeUserApplicationRepository;

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
		System.err.println(start.toString());
		return challengeRepository.findByInfoEventStartTimeBetween(end, start);
	}

	public Challenge updateChallengeBanner(ChallengeImageDTO dto){
		Challenge challenge = challengeRepository.findOne(dto.getChallengeId());
		String filePath = null;
		try {
			// tao thu muc		
			String dirPath = attachPath + "/challenge/" + challenge.getId();
			File dir = new File(dirPath);
			if (!dir.exists()) {
				if (dir.mkdirs()) {
					System.out.println("Directory is created!");
				} else {
					System.out.println("Failed to create directory!");
				}
			}
		
			// lay thong tin file
			String[] fileTypeArray = dto.getFiletype().split("/");
			String extention = fileTypeArray[1];
			String fileType = fileTypeArray[0];
			filePath = dirPath + "/" + dto.getFilename();
			// tao file
			File file = new File(filePath);
			file.createNewFile();			
			
			byte[] bytes = DatatypeConverter.parseBase64Binary(dto.getBase64());
		
			InputStream in = new ByteArrayInputStream(bytes);
			BufferedImage bImageFromConvert = ImageIO.read(in);
			ImageIO.write(bImageFromConvert, extention, file);
			
			String displayPath = filePath.replace("src/main/webapp/", "");
			challenge.setBannerUrl(displayPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return challengeRepository.save(challenge);
	}
	public void deleteChanllenge(Long id){
		
		challengeRepository.delete(id);
		
	}
}

