package fi.lifesup.hackathon.repository;

import java.awt.print.Pageable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fi.lifesup.hackathon.domain.Challenge;

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
	
	@Query("SELECT c FROM Challenge c WHERE c.name LIKE CONCAT('%',?1,'%')")
	List<Challenge> findAllWithPartOfName(String name);
}
