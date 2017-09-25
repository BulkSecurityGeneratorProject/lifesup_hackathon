package fi.lifesup.hackathon.repository;

import fi.lifesup.hackathon.domain.Application;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Application entity.
 */
@SuppressWarnings("unused")
public interface ApplicationRepository extends JpaRepository<Application,Long> {
	
	List<Application> findByChallengeId(Long challengeId);

}
