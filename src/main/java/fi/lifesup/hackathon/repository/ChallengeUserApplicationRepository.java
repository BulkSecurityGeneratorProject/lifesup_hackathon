package fi.lifesup.hackathon.repository;

import fi.lifesup.hackathon.domain.ChallengeUserApplication;
import fi.lifesup.hackathon.service.dto.ApplicationMemberDTO;


import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ChallengeUserApplication entity.
 */
@SuppressWarnings("unused")
public interface ChallengeUserApplicationRepository extends JpaRepository<ChallengeUserApplication, Long> {

	void deleteByApplicationId(Long id);

	void deleteByChallengeId(Long id);

	void deleteByAcceptKey(String key);

	List<ChallengeUserApplication> findByApplicationId(Long applicationId);

	@Query("select cua from ChallengeUserApplication cua" + " where cua.challengeId = :#{[0]} and cua.userId = :#{[1]}")
	ChallengeUserApplication findByChallengeIdAndUserId(Long challengeId, Long userId);

	List<ChallengeUserApplication> findByUserId(Long userId);

	ChallengeUserApplication findByAcceptKey(String acceptKey);

	@Query("select new fi.lifesup.hackathon.service.dto.ApplicationMemberDTO(cua.id, cua.invitedMail, u.status, cua.status)"
			+ " from User u, ChallengeUserApplication cua" + " where u.id = cua.userId and cua.applicationId = :#{[0]}")
	List<ApplicationMemberDTO> getMemberStatus(Long applicationId);

	@Query("select new fi.lifesup.hackathon.service.dto.ApplicationMemberDTO(cua.id, cua.invitedMail, cua.status)"
			+ " from ChallengeUserApplication cua where cua.applicationId = :#{[0]}")
	List<ApplicationMemberDTO> getApplicationMemberDeatil(Long applicationId);
}
