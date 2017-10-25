package fi.lifesup.hackathon.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fi.lifesup.hackathon.domain.Challenge;
import fi.lifesup.hackathon.domain.User;
import fi.lifesup.hackathon.repository.ApplicationRepository;
import fi.lifesup.hackathon.repository.ChallengeRepository;
import fi.lifesup.hackathon.repository.ChallengeUserApplicationRepository;
import fi.lifesup.hackathon.repository.CompanyRepository;
import fi.lifesup.hackathon.repository.UserRepository;
import fi.lifesup.hackathon.searchCriteria.ChallengeSearch;
import fi.lifesup.hackathon.security.SecurityUtils;
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

	@Inject
	private ApplicationRepository applicationRepository;

	@Inject
	private EntityManager em;

	public List<Challenge> getChallenges() {
		User user = userRepository.getUserByAuthority(SecurityUtils.getCurrentUserLogin(), "ROLE_ADMIN");
		if (user != null) {
			return challengeRepository.listChallenge();
		}

		else {
			user = userRepository.getUserByAuthority(SecurityUtils.getCurrentUserLogin(), "ROLE_HOST");
			if (user != null) {
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

	public Challenge updateChallengeBanner(ChallengeImageDTO dto) {
		System.err.println("minh");
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

	public void deleteChanllenge(Long id) {

		challengeRepository.delete(id);

	}

	private String buildQueryChallenge(ChallengeSearch search, String authority) {

		StringBuilder where = new StringBuilder();
		where.append(" where c.info.id = p.id and c.name like '%").append(search.getName()).append("%'");
		// if (search.getStatus() != null) {
		// where.append(" and p.status = :status");
		//
		// }
		if(authority.equals("ROLE_USER")){
			where.append(" and p.status in ('ACTIVE','INACTIVE') ");
		}
		else{
			where.append(" and p.status in ('ACTIVE','INACTIVE','DRAFT') ");
		}
		
		if(authority.equals("ROLE_HOST")){
			where.append(" and c.createdBy = :createdBy ");
		}
				
		if (search.getEventStartTime() != null && search.getEventEndTime() == null) {
			where.append(" and p.eventStartTime >= :startTime ");
		} else if (search.getEventStartTime() == null && search.getEventEndTime() != null) {
			where.append(" and p.eventEndTime >= :endTime ");

		} else if (search.getEventStartTime() != null && search.getEventEndTime() != null) {
			where.append(" and ( p.eventStartTime >= :startTime  or p.eventEndTime >= :endTime )");

		}
		

		return where.toString();
	}

	public Page<Challenge> getChallengeSearch(ChallengeSearch search, Pageable pageable) {
		StringBuilder sbQuery = new StringBuilder();
		sbQuery.append("select c from Challenge c, ChallengeInfo p ");
		StringBuilder sbCount = new StringBuilder();
		sbCount.append("select count(c.id) from Challenge c, ChallengeInfo p ");
		List<Challenge> lst = null;
		Long total = null;
		String authority = null;
		
		User user = userRepository.getUserByAuthority(SecurityUtils.getCurrentUserLogin(), "ROLE_ADMIN");
		if(user != null){
			authority = "ROLE_ADMIN";
		}
		else{
			user = userRepository.getUserByAuthority(SecurityUtils.getCurrentUserLogin(), "ROLE_HOST");
			if(user != null){
				authority = "ROLE_HOST";
			}
			else{
				authority = "ROLE_USER";
			}
		}
		
		String where = buildQueryChallenge(search,authority);

		Query query = em.createQuery(sbQuery.toString() + where + " order by p.status, p.applicationCloseDate asc",
				Challenge.class);
		query.setFirstResult(pageable.getOffset()).setMaxResults(pageable.getPageSize());
		

		if (search.getEventStartTime() != null) {
			query.setParameter("startTime", search.getEventStartTime());
		}
		if (search.getEventEndTime() != null) {
			query.setParameter("endTime", search.getEventEndTime());
		}
		if(authority.equals("ROLE_HOST")){
			query.setParameter("createdBy", user.getId());
		}
		System.out.println(search);
		lst = query.getResultList();

		Query count = em.createQuery(sbCount.toString() + where);

		if (search.getEventStartTime() != null) {
			count.setParameter("startTime", search.getEventStartTime());
		}
		if (search.getEventEndTime() != null) {
			count.setParameter("endTime", search.getEventEndTime());
		}
		if(authority.equals("ROLE_HOST")){
			count.setParameter("createdBy", user.getId());
		}
		

		
		total = (Long) count.getSingleResult();
		return new PageImpl<>(lst, pageable, total);

	}

}
