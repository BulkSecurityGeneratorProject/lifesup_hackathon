package fi.lifesup.hackathon.repository;

import fi.lifesup.hackathon.domain.Application;
import fi.lifesup.hackathon.service.dto.ApplicationDTO;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Application entity.
 */
@SuppressWarnings("unused")
public interface ApplicationRepository extends JpaRepository<Application,Long> {
	
	List<Application> findByChallengeId(Long challengeId);
	
	@Query("select new fi.lifesup.hackathon.service.dto.ApplicationDTO(a.id, a.teamName, a.companyName, a.description,"
			+ " a.motivation, a.ideasDescription, a.status, cua.challengeId)"
			+ " from Application a, ChallengeUserApplication cua"
			+ " where cua.challengeId = :#{[0]} and cua.applicationId = a.id")
	List<ApplicationDTO> getapplication(Long challengeId);
	
	@Query("select a from Application a, ChallengeUserApplication cua"
			+ " where cua.acceptKey = :#{[0]} and cua.applicationId = a.id")
	Application getapplication(String acceptKey);
}
