package fi.lifesup.hackathon.repository;

import fi.lifesup.hackathon.domain.*;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the UserList entity.
 */
@SuppressWarnings("unused")
public interface UserListRepository extends JpaRepository<UserList,Long> {

    @Query("select distinct userList from UserList userList left join fetch userList.applications")
    List<UserList> findAllWithEagerRelationships();

    @Query("select userList from UserList userList left join fetch userList.applications where userList.id =:id")
    UserList findOneWithEagerRelationships(@Param("id") Long id);
//   @Query("update UserList u set status='PROFILE_COMPLETE' where id=u.id and u.birthday is not null and u.city is not null and u.jobTitle is not null and u.company is not null "
//   		+ "and u.country is not null and u.email is not null and u.phone is not null and u.city is not null and u.nationality is not null")
//	UserList updateAccount();
   
   

}
