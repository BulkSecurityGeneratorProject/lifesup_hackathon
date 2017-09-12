package fi.lifesup.hackathon.repository;

import fi.lifesup.hackathon.domain.ChallengeInfo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ChallengeInfo entity.
 */
@SuppressWarnings("unused")
public interface ChallengeInfoRepository extends JpaRepository<ChallengeInfo,Long> {

}
