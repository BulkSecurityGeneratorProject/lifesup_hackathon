package fi.lifesup.hackathon.web.rest;

import com.codahale.metrics.annotation.Timed;
import fi.lifesup.hackathon.domain.ChallengeSubmission;

import fi.lifesup.hackathon.repository.ChallengeSubmissionRepository;
import fi.lifesup.hackathon.service.ChallengeSubmissionService;
import fi.lifesup.hackathon.service.dto.ChallengeSubmissionDTO;
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
 * REST controller for managing ChallengeSubmission.
 */
@RestController
@RequestMapping("/api")
public class ChallengeSubmissionResource {

    private final Logger log = LoggerFactory.getLogger(ChallengeSubmissionResource.class);
        
    @Inject
    private ChallengeSubmissionRepository challengeSubmissionRepository;
    @Inject
    private ChallengeSubmissionService challengeSubmissionService;

    /**
     * POST  /challenge-submissions : Create a new challengeSubmission.
     *
     * @param challengeSubmission the challengeSubmission to create
     * @return the ResponseEntity with status 201 (Created) and with body the new challengeSubmission, or with status 400 (Bad Request) if the challengeSubmission has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/challenge-submissions")
    @Timed
    public ResponseEntity<ChallengeSubmission> createChallengeSubmission(@Valid @RequestBody ChallengeSubmission challengeSubmission) throws URISyntaxException {
        log.debug("REST request to save ChallengeSubmission : {}", challengeSubmission);
        if (challengeSubmission.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("challengeSubmission", "idexists", "A new challengeSubmission cannot already have an ID")).body(null);
        }
        ChallengeSubmission result = challengeSubmissionRepository.save(challengeSubmission);
        return ResponseEntity.created(new URI("/api/challenge-submissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("challengeSubmission", result.getId().toString()))
            .body(result);
    }
    @PostMapping("/challenge-submissions-created")
    @Timed
    public ResponseEntity<ChallengeSubmission> createdChallengeSubmission(@Valid @RequestBody ChallengeSubmissionDTO challengeSubmissionDTO) throws URISyntaxException {
        log.debug("REST request to save ChallengeSubmission : {}", challengeSubmissionDTO);
        ChallengeSubmission challengeSubmission=challengeSubmissionService.createChallengeSubmission(challengeSubmissionDTO);
        if(challengeSubmission!=null)
        {
        	 if (challengeSubmission.getId() != null) {
                 return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("challengeSubmission", "idexists", "A new challengeSubmission cannot already have an ID")).body(null);
             }
             ChallengeSubmission result = challengeSubmissionRepository.save(challengeSubmission);
             return ResponseEntity.created(new URI("/api/challenge-submissions/" + result.getId()))
                 .headers(HeaderUtil.createEntityCreationAlert("challengeSubmission", result.getId().toString()))
                 .body(result);
        }
        else
        	 return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("challengeSubmission", "idexists", "cannot application or worksapce")).body(null);
        	
    }
    /**
     * PUT  /challenge-submissions : Updates an existing challengeSubmission.
     *
     * @param challengeSubmission the challengeSubmission to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated challengeSubmission,
     * or with status 400 (Bad Request) if the challengeSubmission is not valid,
     * or with status 500 (Internal Server Error) if the challengeSubmission couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/challenge-submissions")
    @Timed
    public ResponseEntity<ChallengeSubmission> updateChallengeSubmission(@Valid @RequestBody ChallengeSubmission challengeSubmission) throws URISyntaxException {
        log.debug("REST request to update ChallengeSubmission : {}", challengeSubmission);
        if (challengeSubmission.getId() == null) {
            return createChallengeSubmission(challengeSubmission);
        }
        ChallengeSubmission result = challengeSubmissionRepository.save(challengeSubmission);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("challengeSubmission", challengeSubmission.getId().toString()))
            .body(result);
    }

    /**
     * GET  /challenge-submissions : get all the challengeSubmissions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of challengeSubmissions in body
     */
    @GetMapping("/challenge-submissions")
    @Timed
    public List<ChallengeSubmission> getAllChallengeSubmissions() {
        log.debug("REST request to get all ChallengeSubmissions");
        List<ChallengeSubmission> challengeSubmissions = challengeSubmissionRepository.findAll();
        return challengeSubmissions;
    }

    /**
     * GET  /challenge-submissions/:id : get the "id" challengeSubmission.
     *
     * @param id the id of the challengeSubmission to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the challengeSubmission, or with status 404 (Not Found)
     */
    @GetMapping("/challenge-submissions/{id}")
    @Timed
    public ResponseEntity<ChallengeSubmission> getChallengeSubmission(@PathVariable Long id) {
        log.debug("REST request to get ChallengeSubmission : {}", id);
        ChallengeSubmission challengeSubmission = challengeSubmissionRepository.findOne(id);
        return Optional.ofNullable(challengeSubmission)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /challenge-submissions/:id : delete the "id" challengeSubmission.
     *
     * @param id the id of the challengeSubmission to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/challenge-submissions/{id}")
    @Timed
    public ResponseEntity<Void> deleteChallengeSubmission(@PathVariable Long id) {
        log.debug("REST request to delete ChallengeSubmission : {}", id);
        challengeSubmissionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("challengeSubmission", id.toString())).build();
    }

}
