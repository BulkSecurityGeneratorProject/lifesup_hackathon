package fi.lifesup.hackathon.repository;

import fi.lifesup.hackathon.domain.UserInfo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserInfo entity.
 */
@SuppressWarnings("unused")
public interface UserInfoRepository extends JpaRepository<UserInfo,Long> {

}
