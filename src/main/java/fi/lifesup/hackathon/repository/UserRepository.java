package fi.lifesup.hackathon.repository;

import fi.lifesup.hackathon.domain.Application;
import fi.lifesup.hackathon.domain.Challenge;
import fi.lifesup.hackathon.domain.User;

import java.time.ZonedDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(ZonedDateTime dateTime);

    Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmail(String email);

    Optional<User> findOneByLogin(String login);

    @Query(value = "select distinct user from User user left join fetch user.authorities",
        countQuery = "select count(user) from User user")
    Page<User> findAllWithAuthorities(Pageable pageable);
    @Query("select u from User u  where u.status not like 'REMOVED'")
	List<User> listUser();

    @Override
    void delete(User t);
    
    @Query("select c from Challenge c, ChallengeUserApplication cua where c.id = cua.challengeId and cua.userId = :#{[0]} ")
    List<Challenge> getChallengeByUser(Long id);
    
    @Query("select a from Application a, ChallengeUserApplication cua where a.id = cua.applicationId and cua.userId = :#{[0]} ")
    List<Application>  getApplicationByUser(Long id);

}
