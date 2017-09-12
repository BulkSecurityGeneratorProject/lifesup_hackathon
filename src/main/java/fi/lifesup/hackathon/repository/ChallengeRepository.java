package fi.lifesup.hackathon.repository;

import fi.lifesup.hackathon.domain.Challenge;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Challenge entity.
 */
@SuppressWarnings("unused")
public interface ChallengeRepository extends JpaRepository<Challenge,Long> {

}
