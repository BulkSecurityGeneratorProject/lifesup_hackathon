package fi.lifesup.hackathon.repository;

import fi.lifesup.hackathon.domain.Challenge;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Challenge entity.
 */
@SuppressWarnings("unused")
public interface ChallengeRepository extends JpaRepository<Challenge,Long> {

	List<Challenge> findByCompanyId(Long id);
	
	@Query("select c from Challenge c, ChallengeUserApplication cua "
			+ "where cua.userId = :#{[0]} and c.id = cua.challengeId")
	List<Challenge> getChallengeforUser(Long userId);
}
