package fi.lifesup.hackathon.repository;

import fi.lifesup.hackathon.domain.Challenge;

import org.springframework.data.jpa.repository.*;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the Challenge entity.
 */
@SuppressWarnings("unused")
public interface ChallengeRepository extends JpaRepository<Challenge,Long> {

	@Query("select c from Challenge c  where c.company.id = ?1 and c.info.status not like 'REMOVED'")
	List<Challenge> findByCompanyId(Long id);
	
	@Query("select c from Challenge c  where c.info.status not like 'REMOVED'")
	List<Challenge> listChallenge();
	
	@Query("select challenge from Challenge challenge, ChallengeInfo challengeInfo, "
			+ "ChallengeUserApplication challengeUserApplication where challengeUserApplication.userId = :#{[0]} "
			+ "and challengeUserApplication.challengeId = challenge.id and challenge.info.id = challengeInfo.id "
			+ "and (challengeInfo.status = 'ACTIVE' or challengeInfo.status = 'INACTIVE')")
	List<Challenge> getChallengeByUser(Long id);
	
}
