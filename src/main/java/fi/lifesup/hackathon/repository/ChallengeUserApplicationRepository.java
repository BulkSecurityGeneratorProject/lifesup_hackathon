package fi.lifesup.hackathon.repository;

import fi.lifesup.hackathon.domain.ChallengeUserApplication;
import fi.lifesup.hackathon.service.dto.UserInfoDTO;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ChallengeUserApplication entity.
 */
@SuppressWarnings("unused")
public interface ChallengeUserApplicationRepository extends JpaRepository<ChallengeUserApplication, Long> {

	void deleteByApplicationId(Long id);

	List<ChallengeUserApplication> findByApplicationId(Long applicationId);

	@Query("select new fi.lifesup.hackathon.service.dto.UserInfoDTO(u.email, u.firstName, u.lastName, ui)"
			+ " from User u, ChallengeUserApplication cua, UserInfo ui"
			+ " where u.id = cua.userId and u.userInfo.id = ui.id and cua.applicationId = :#{[0]}")	
	List<UserInfoDTO> getApplicationMember(Long applicationId);

	@Query("select cua from ChallengeUserApplication cua" + " where cua.challengeId = :#{[0]} and cua.userId = :#{[1]}")
	ChallengeUserApplication findByChallengeIdAndUserId(Long challengeId, Long userId);

	List<ChallengeUserApplication> findByUserId(Long userId);
	
	ChallengeUserApplication findByAcceptKey(String acceptKey);
	
	void deleteByChallengeId(Long challengeId);
	
}
