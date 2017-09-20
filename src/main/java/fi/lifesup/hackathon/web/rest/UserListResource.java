package fi.lifesup.hackathon.web.rest;

import com.codahale.metrics.annotation.Timed;
import fi.lifesup.hackathon.domain.UserList;

import fi.lifesup.hackathon.repository.UserListRepository;
import fi.lifesup.hackathon.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing UserList.
 */
@RestController
@RequestMapping("/api")
public class UserListResource {

    private final Logger log = LoggerFactory.getLogger(UserListResource.class);
        
    @Inject
    private UserListRepository userListRepository;

    /**
     * POST  /user-lists : Create a new userList.
     *
     * @param userList the userList to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userList, or with status 400 (Bad Request) if the userList has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
//    @GetMapping("/updateUser")
//     @Timed
//     public UserList updateAccount() {
//         log.debug("REST request to get all UserLists");
//         UserList userLists = userListRepository.updateAccount();
//         return userLists;
//     }
    
    @PostMapping("/user-lists")
    @Timed
    public ResponseEntity<UserList> createUserList(@Valid @RequestBody UserList userList) throws URISyntaxException {
        log.debug("REST request to save UserList : {}", userList);
        if (userList.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userList", "idexists", "A new userList cannot already have an ID")).body(null);
        }
        UserList result = userListRepository.save(userList);
        return ResponseEntity.created(new URI("/api/user-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("userList", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-lists : Updates an existing userList.
     *
     * @param userList the userList to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userList,
     * or with status 400 (Bad Request) if the userList is not valid,
     * or with status 500 (Internal Server Error) if the userList couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-lists")
    @Timed
    public ResponseEntity<UserList> updateUserList(@Valid @RequestBody UserList userList) throws URISyntaxException {
        log.debug("REST request to update UserList : {}", userList);
        if (userList.getId() == null) {
            return createUserList(userList);
        }
        UserList result = userListRepository.save(userList);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("userList", userList.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-lists : get all the userLists.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userLists in body
     */
    @GetMapping("/user-lists")
    @Timed
    public List<UserList> getAllUserLists() {
        log.debug("REST request to get all UserLists");
        List<UserList> userLists = userListRepository.findAllWithEagerRelationships();
        return userLists;
    }

    /**
     * GET  /user-lists/:id : get the "id" userList.
     *
     * @param id the id of the userList to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userList, or with status 404 (Not Found)
     */
    @GetMapping("/user-lists/{id}")
    @Timed
    public ResponseEntity<UserList> getUserList(@PathVariable Long id) {
        log.debug("REST request to get UserList : {}", id);
        UserList userList = userListRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(userList)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /user-lists/:id : delete the "id" userList.
     *
     * @param id the id of the userList to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-lists/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserList(@PathVariable Long id) {
        log.debug("REST request to delete UserList : {}", id);
        userListRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userList", id.toString())).build();
    }

}
