package fi.lifesup.hackathon.repository;

import fi.lifesup.hackathon.domain.ChallengeWorkspace;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ChallengeWorkspace entity.
 */
@SuppressWarnings("unused")
public interface ChallengeWorkspaceRepository extends JpaRepository<ChallengeWorkspace,Long> {

}
