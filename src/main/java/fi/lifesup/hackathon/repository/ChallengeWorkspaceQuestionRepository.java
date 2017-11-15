package fi.lifesup.hackathon.repository;

import fi.lifesup.hackathon.domain.ChallengeWorkspaceQuestion;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ChallengeWorkspaceQuestion entity.
 */
@SuppressWarnings("unused")
public interface ChallengeWorkspaceQuestionRepository extends JpaRepository<ChallengeWorkspaceQuestion,Long> {

}
