package fi.lifesup.hackathon.repository;

import fi.lifesup.hackathon.domain.ChallengeSubmissionFeedback;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ChallengeSubmissionFeedback entity.
 */
@SuppressWarnings("unused")
public interface ChallengeSubmissionFeedbackRepository extends JpaRepository<ChallengeSubmissionFeedback,Long> {

}
