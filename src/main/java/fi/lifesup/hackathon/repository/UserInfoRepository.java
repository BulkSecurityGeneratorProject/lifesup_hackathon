package fi.lifesup.hackathon.repository;

import fi.lifesup.hackathon.domain.UserInfo;
import fi.lifesup.hackathon.service.dto.UserInfoDTO;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserInfo entity.
 */
@SuppressWarnings("unused")
public interface UserInfoRepository extends JpaRepository<UserInfo,Long> {
	@Query("select new fi.lifesup.hackathon.service.dto.UserInfoDTO(u.email, u.firstName, u.lastName, u.userInfo)"
			+ " from User u, ChallengeUserApplication cua"
			+ " where u.id = cua.userId and cua.applicationId = :#{[0]}")	
	List<UserInfoDTO> getUserInfo(Long applicationId);
}
