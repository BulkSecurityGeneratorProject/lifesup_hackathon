package fi.lifesup.hackathon.repository;

import fi.lifesup.hackathon.domain.ChallengeWorkspaceNews;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ChallengeWorkspaceNews entity.
 */
@SuppressWarnings("unused")
public interface ChallengeWorkspaceNewsRepository extends JpaRepository<ChallengeWorkspaceNews,Long> {

}
