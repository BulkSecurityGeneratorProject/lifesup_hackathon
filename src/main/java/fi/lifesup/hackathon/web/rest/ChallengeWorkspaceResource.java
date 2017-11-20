package fi.lifesup.hackathon.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import fi.lifesup.hackathon.domain.ChallengeWorkspace;
import fi.lifesup.hackathon.repository.ChallengeWorkspaceRepository;
import fi.lifesup.hackathon.service.ChallengeWorkspaceService;
import fi.lifesup.hackathon.service.dto.ChallengeWorkspaceDTO;
import fi.lifesup.hackathon.web.rest.util.HeaderUtil;

/**
 * REST controller for managing ChallengeWorkspace.
 */
@RestController
@RequestMapping("/api")
public class ChallengeWorkspaceResource {

    private final Logger log = LoggerFactory.getLogger(ChallengeWorkspaceResource.class);
        
    @Inject
    private ChallengeWorkspaceRepository challengeWorkspaceRepository;
    
    @Inject
    private ChallengeWorkspaceService challengeWorkspaceService;

    /**
     * POST  /challenge-workspaces : Create a new challengeWorkspace.
     *
     * @param challengeWorkspace the challengeWorkspace to create
     * @return the ResponseEntity with status 201 (Created) and with body the new challengeWorkspace, or with status 400 (Bad Request) if the challengeWorkspace has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/challenge-workspaces")
    @Timed
    public ResponseEntity<ChallengeWorkspace> createChallengeWorkspace(@RequestBody ChallengeWorkspaceDTO challengeWorkspace) throws URISyntaxException {
        log.debug("REST request to save ChallengeWorkspace : {}", challengeWorkspace);
        if (challengeWorkspace.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("challengeWorkspace", "idexists", "A new challengeWorkspace cannot already have an ID")).body(null);
        }
        ChallengeWorkspace workspace = challengeWorkspaceRepository.findByChallengeId(challengeWorkspace.getChallengeId());
        if(workspace != null){
        	return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("challengeWorkspace", "idexists", "A challengeWorkspace is already exist.")).body(null);
        }
        ChallengeWorkspace result = challengeWorkspaceService.saveChallengeWorksapce(challengeWorkspace);
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
    public ResponseEntity<ChallengeWorkspace> updateChallengeWorkspace(@RequestBody ChallengeWorkspaceDTO challengeWorkspace) throws URISyntaxException {
        log.debug("REST request to update ChallengeWorkspace : {}", challengeWorkspace);
        if (challengeWorkspace.getId() == null) {
            return createChallengeWorkspace(challengeWorkspace);
        }
        
        ChallengeWorkspace result = challengeWorkspaceService.saveChallengeWorksapce(challengeWorkspace);
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
    
    @GetMapping("/challenge-workspaces/challenge/{challengeId}")
    @Timed
    public ResponseEntity<ChallengeWorkspace> getChallengeWorkspaceByChallenge(@PathVariable Long challengeId) {
        log.debug("REST request to get ChallengeWorkspace by challenge id : {}", challengeId);
        ChallengeWorkspace challengeWorkspace = challengeWorkspaceRepository.findByChallengeId(challengeId);
        return Optional.ofNullable(challengeWorkspace)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/challenge-workspaces/details/{login}/{challengeId}")
    @Timed
    public ChallengeWorkspaceDTO getChallengeWorkspaceDetail(@PathVariable Long challengeId) {
        log.debug("REST request to get ChallengeWorkspace by challenge id : {}", challengeId);
        ChallengeWorkspaceDTO challengeWorkspace = challengeWorkspaceService.getChallengeWorkspaceDetail(challengeId);
        return challengeWorkspace;
    }

}
