package fi.lifesup.hackathon.service;



import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.lifesup.hackathon.domain.Authority;
import fi.lifesup.hackathon.domain.enumeration.UserSex;
import fi.lifesup.hackathon.repository.UserInfoRepository;
import fi.lifesup.hackathon.repository.UserRepository;
import fi.lifesup.hackathon.security.SecurityUtils;


@Service
@Transactional
public class UserInfoService {
	@Inject
	private UserInfoRepository userInfoRepository;
	@Inject
	private UserRepository userRepository;
	
//	public void updateUserInfo(String phone,UserSex sex,String companyName,String jobTitle,String logoUrl,String country,String city,String nationality,ZonedDateTime birthday,String introduction,String twitterUrl,String linkedInUrl,String skills,String workArea,String feedbackFrom, String websiteUrl)
//	{
//
//		userInfoRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(u -> {
//			u.setPhone(phone);
//			u.setSex(sex);
//			u.setCompanyName(companyName);
//			u.setJobTitle(jobTitle);
//			u.setLogoUrl(logoUrl);
//			u.setCity(city);
//			u.setCountry(country);
//			u.setNationality(nationality);
//			u.setBirthday(birthday);
//			u.setIntroduction(introduction);
//			u.setLinkedInUrl(linkedInUrl);
//			u.setSkills(skills);
//			u.setTwitterUrl(twitterUrl);
//			u.setWebsiteUrl(websiteUrl);
//			u.setWorkArea(workArea);
//			u.setFeedbackFrom(feedbackFrom);
//			userInfoRepository.save(u);
//			log.debug("Changed Information for UserInfo: {}", u);
//			
//		});
//	}
}
