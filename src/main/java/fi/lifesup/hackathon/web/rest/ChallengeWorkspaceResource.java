package fi.lifesup.hackathon.web.rest;

import com.codahale.metrics.annotation.Timed;
import fi.lifesup.hackathon.domain.ChallengeWorkspace;

import fi.lifesup.hackathon.repository.ChallengeWorkspaceRepository;
import fi.lifesup.hackathon.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ChallengeWorkspace.
 */
@RestController
@RequestMapping("/api")
public class ChallengeWorkspaceResource {

    private final Logger log = LoggerFactory.getLogger(ChallengeWorkspaceResource.class);
        
    @Inject
    private ChallengeWorkspaceRepository challengeWorkspaceRepository;

    /**
     * POST  /challenge-workspaces : Create a new challengeWorkspace.
     *
     * @param challengeWorkspace the challengeWorkspace to create
     * @return the ResponseEntity with status 201 (Created) and with body the new challengeWorkspace, or with status 400 (Bad Request) if the challengeWorkspace has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/challenge-workspaces")
    @Timed
    public ResponseEntity<ChallengeWorkspace> createChallengeWorkspace(@RequestBody ChallengeWorkspace challengeWorkspace) throws URISyntaxException {
        log.debug("REST request to save ChallengeWorkspace : {}", challengeWorkspace);
        if (challengeWorkspace.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("challengeWorkspace", "idexists", "A new challengeWorkspace cannot already have an ID")).body(null);
        }
        ChallengeWorkspace result = challengeWorkspaceRepository.save(challengeWorkspace);
        return ResponseEntity.created(new URI("/api/challenge-workspaces/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("challengeWorkspace", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /challenge-workspaces : Updates an existing challengeWorkspace.
     *
     * @param challengeWorkspace the challengeWorkspace to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated challengeWorkspace,
     * or with status 400 (Bad Request) if the challengeWorkspace is not valid,
     * or with status 500 (Internal Server Error) if the challengeWorkspace couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/challenge-workspaces")
    @Timed
    public ResponseEntity<ChallengeWorkspace> updateChallengeWorkspace(@RequestBody ChallengeWorkspace challengeWorkspace) throws URISyntaxException {
        log.debug("REST request to update ChallengeWorkspace : {}", challengeWorkspace);
        if (challengeWorkspace.getId() == null) {
            return createChallengeWorkspace(challengeWorkspace);
        }
        ChallengeWorkspace result = challengeWorkspaceRepository.save(challengeWorkspace);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("challengeWorkspace", challengeWorkspace.getId().toString()))
            .body(result);
    }

    /**
     * GET  /challenge-workspaces : get all the challengeWorkspaces.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of challengeWorkspaces in body
     */
    @GetMapping("/challenge-workspaces")
    @Timed
    public List<ChallengeWorkspace> getAllChallengeWorkspaces() {
        log.debug("REST request to get all ChallengeWorkspaces");
        List<ChallengeWorkspace> challengeWorkspaces = challengeWorkspaceRepository.findAll();
        return challengeWorkspaces;
    }

    /**
     * GET  /challenge-workspaces/:id : get the "id" challengeWorkspace.
     *
     * @param id the id of the challengeWorkspace to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the challengeWorkspace, or with status 404 (Not Found)
     */
    @GetMapping("/challenge-workspaces/{id}")
    @Timed
    public ResponseEntity<ChallengeWorkspace> getChallengeWorkspace(@PathVariable Long id) {
        log.debug("REST request to get ChallengeWorkspace : {}", id);
        ChallengeWorkspace challengeWorkspace = challengeWorkspaceRepository.findOne(id);
        return Optional.ofNullable(challengeWorkspace)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /challenge-workspaces/:id : delete the "id" challengeWorkspace.
     *
     * @param id the id of the challengeWorkspace to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/challenge-workspaces/{id}")
    @Timed
    public ResponseEntity<Void> deleteChallengeWorkspace(@PathVariable Long id) {
        log.debug("REST request to delete ChallengeWorkspace : {}", id);
        challengeWorkspaceRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("challengeWorkspace", id.toString())).build();
    }

}
