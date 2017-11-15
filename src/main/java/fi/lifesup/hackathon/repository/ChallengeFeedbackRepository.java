package fi.lifesup.hackathon.repository;

import fi.lifesup.hackathon.domain.ChallengeFeedback;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ChallengeFeedback entity.
 */
@SuppressWarnings("unused")
public interface ChallengeFeedbackRepository extends JpaRepository<ChallengeFeedback,Long> {

}
