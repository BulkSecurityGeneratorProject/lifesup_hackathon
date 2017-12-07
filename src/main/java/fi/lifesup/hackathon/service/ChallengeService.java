package fi.lifesup.hackathon.service;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
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

import fi.lifesup.hackathon.domain.Application;
import fi.lifesup.hackathon.domain.Challenge;
import fi.lifesup.hackathon.domain.User;
import fi.lifesup.hackathon.domain.enumeration.ChallengeStatus;
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

	@Value("${attach.path}")
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
	private ApplicationService applicationService;

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

	public Challenge createChallenge(Challenge challenge) {
		User user = userService.getUserWithAuthorities();
		challenge.setCreated_by(user.getId());
		challenge.setCompany(user.getCompany());
		return challengeRepository.save(challenge);
	}

	public Challenge updateChallengeBanner(ChallengeImageDTO dto) {
		Challenge challenge = challengeRepository.findOne(dto.getChallengeId());
		String filePath = null;
		try {
			// tao thu muc
			String dirPath = attachPath + "/challenge/" + "challenge_" + challenge.getId() + "/banner/";
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
			filePath = dirPath + dto.getChallengeId() + "." + extention;
			// tao file
			File file = new File(filePath);
			file.createNewFile();

			byte[] bytes = DatatypeConverter.parseBase64Binary(dto.getBase64());

			InputStream in = new ByteArrayInputStream(bytes);
			BufferedImage bImageFromConvert = ImageIO.read(in);
			ImageIO.write(bImageFromConvert, extention, file);

			challenge.setBannerUrl(filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return challengeRepository.save(challenge);
	}

	public void deleteChanllenge(Long id) {
		Challenge challenge = challengeRepository.findOne(id);
		challenge.getInfo().setStatus(ChallengeStatus.REMOVED);

		List<Application> applications = applicationRepository.findByChallengeId(id);
		for (Application application : applications) {
			applicationService.deleteApplication(application.getId());
		}
		challengeRepository.save(challenge);

	}

	private String buildQueryChallenge(ChallengeSearch search) {

		StringBuilder where = new StringBuilder();
		where.append(" where c.info.id = p.id and c.name like '%").append(search.getName()).append("%'");
		// if (search.getStatus() != null) {
		// where.append(" and p.status = :status");
		//
		// }

		where.append(" and p.status in ('ACTIVE','INACTIVE') ");

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

		String where = buildQueryChallenge(search);

		Query query = em.createQuery(sbQuery.toString() + where + " order by p.status, p.applicationCloseDate asc",
				Challenge.class);
		query.setFirstResult(pageable.getOffset()).setMaxResults(pageable.getPageSize());

		if (search.getEventStartTime() != null) {
			query.setParameter("startTime", search.getEventStartTime());
		}
		if (search.getEventEndTime() != null) {
			query.setParameter("endTime", search.getEventEndTime());
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

		total = (Long) count.getSingleResult();
		return new PageImpl<>(lst, pageable, total);

	}

	private String buildQueryManageChallenge(ChallengeSearch search) {

		StringBuilder where = new StringBuilder();
		where.append(" where c.info.id = p.id and c.name like '%").append(search.getName()).append("%'");
		if (search.getStatus() != null) {
			where.append(" and p.status = :status ");
		} else {
			where.append("and p.status not like 'REMOVED' ");
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

	public Page<Challenge> getChallengeManageSearch(ChallengeSearch search, Pageable pageable) {
		StringBuilder sbQuery = new StringBuilder();
		sbQuery.append("select c from Challenge c, ChallengeInfo p ");
		StringBuilder sbCount = new StringBuilder();
		sbCount.append("select count(c.id) from Challenge c, ChallengeInfo p ");
		List<Challenge> lst = null;
		Long total = null;
		int t = 0;
		User user = userRepository.getUserByAuthority(SecurityUtils.getCurrentUserLogin(), "ROLE_ADMIN");

		if (user == null) {
			user = userRepository.getUserByAuthority(SecurityUtils.getCurrentUserLogin(), "ROLE_HOST");

			t = 1;
			if (user == null) {
				user = userRepository.getUserByAuthority(SecurityUtils.getCurrentUserLogin(), "ROLE_HOST");
				t = 2;
			}
		}

		String where = buildQueryManageChallenge(search);
		if (t == 1) {
			where = where + " and c.created_by= " + user.getId();
		}

		Query query = em.createQuery(sbQuery.toString() + where, Challenge.class);
		query.setFirstResult(pageable.getOffset()).setMaxResults(pageable.getPageSize());

		if (search.getStatus() != null) {
			query.setParameter("status", search.getStatus());
		}
		if (search.getEventStartTime() != null) {
			query.setParameter("startTime", search.getEventStartTime());
		}
		if (search.getEventEndTime() != null) {
			query.setParameter("endTime", search.getEventEndTime());
		}

		System.out.println(search);
		lst = query.getResultList();

		Query count = em.createQuery(sbCount.toString() + where);

		if (search.getStatus() != null) {
			count.setParameter("status", search.getStatus());
		}
		if (search.getEventStartTime() != null) {
			count.setParameter("startTime", search.getEventStartTime());
		}
		if (search.getEventEndTime() != null) {
			count.setParameter("endTime", search.getEventEndTime());
		}
		total = (Long) count.getSingleResult();
		return new PageImpl<>(lst, pageable, total);

	}

	public String converbase64(String url) {
		File file = new File(url);
		String base64 = null;
		FileInputStream fis;
		try {
			fis = new FileInputStream(url);
			BufferedInputStream bis = new BufferedInputStream(fis);
			byte[] bytes = new byte[(int) file.length()];
			bis.read(bytes);
			base64 = Base64.getEncoder().encodeToString(bytes);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return base64;
	}

}
