package fi.lifesup.hackathon.repository;

import fi.lifesup.hackathon.domain.ChallengeUserApplication;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ChallengeUserApplication entity.
 */
@SuppressWarnings("unused")
public interface ChallengeUserApplicationRepository extends JpaRepository<ChallengeUserApplication, Long> {

	void deleteByApplicationId(Long id);

	List<ChallengeUserApplication> findByApplicationId(Long applicationId);

	@Query("select u.email from User u, ChallengeUserApplication cua, Application a"
			+ " where u.id = cua.userId and cua.applicationId = a.id and a.id = :#{[0]}")
	List<String> getApplicationMember(Long applicationId);

	@Query("select cua from ChallengeUserApplication cua" + " where cua.challengeId = :#{[0]} and cua.userId = :#{[1]}")
	ChallengeUserApplication findByChallengeIdAndUserId(Long challengeId, Long userId);

	List<ChallengeUserApplication> findByUserId(Long userId);
	
	ChallengeUserApplication findByAcceptKey(String acceptKey);
	
	void deleteByChanllengeId(Long challengeId);
	
}
