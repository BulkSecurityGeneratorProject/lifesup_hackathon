package fi.lifesup.hackathon.repository;

import fi.lifesup.hackathon.domain.UserList;

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
    
    UserList findByEmail(String email);

}
