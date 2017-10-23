package fi.lifesup.hackathon.repository;

import fi.lifesup.hackathon.domain.Application;
import fi.lifesup.hackathon.service.dto.ApplicationDTO;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Application entity.
 */
@SuppressWarnings("unused")
public interface ApplicationRepository extends JpaRepository<Application, Long> {

	List<Application> findByChallengeId(Long challengeId);

	@Query("select new fi.lifesup.hackathon.service.dto.ApplicationDTO(a.id, a.teamName, a.companyName, a.description,"
			+ " a.motivation, a.ideasDescription, a.status, a.challenge.id)"
			+ " from Application a where a.challenge.id = :#{[0]}")
	List<ApplicationDTO> getapplication(Long challengeId);
	
	@Query("select new fi.lifesup.hackathon.service.dto.ApplicationDTO(a.id, a.teamName, a.companyName, a.description,"
			+ " a.motivation, a.ideasDescription, a.status, a.challenge.id)"
			+ " from Application a where a.id = :#{[0]}")
	ApplicationDTO getapplicationById(Long id);

	@Query("select aie.application from ApplicationInviteEmail aie"
			+ " where aie.acceptKey = :#{[0]}")
	Application getapplication(String acceptKey);

	
}
