package fi.lifesup.hackathon.repository;

import fi.lifesup.hackathon.domain.ChallengeResult;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ChallengeResult entity.
 */
@SuppressWarnings("unused")
public interface ChallengeResultRepository extends JpaRepository<ChallengeResult,Long> {

}
