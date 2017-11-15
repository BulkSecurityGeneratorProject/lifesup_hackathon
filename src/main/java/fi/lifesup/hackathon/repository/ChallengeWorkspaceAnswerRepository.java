package fi.lifesup.hackathon.repository;

import fi.lifesup.hackathon.domain.ChallengeWorkspaceAnswer;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ChallengeWorkspaceAnswer entity.
 */
@SuppressWarnings("unused")
public interface ChallengeWorkspaceAnswerRepository extends JpaRepository<ChallengeWorkspaceAnswer,Long> {

}
