package fi.lifesup.hackathon.repository;

import fi.lifesup.hackathon.domain.ChallengeUserApplication;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ChallengeUserApplication entity.
 */
@SuppressWarnings("unused")
public interface ChallengeUserApplicationRepository extends JpaRepository<ChallengeUserApplication,Long> {

}
