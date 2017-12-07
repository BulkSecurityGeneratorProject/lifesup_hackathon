package fi.lifesup.hackathon.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

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

import fi.lifesup.hackathon.domain.ChallengeResult;
import fi.lifesup.hackathon.repository.ChallengeResultRepository;
import fi.lifesup.hackathon.web.rest.util.HeaderUtil;

/**
 * REST controller for managing ChallengeResult.
 */
@RestController
@RequestMapping("/api")
public class ChallengeResultResource {

    private final Logger log = LoggerFactory.getLogger(ChallengeResultResource.class);
        
    @Inject
    private ChallengeResultRepository challengeResultRepository;

    /**
     * POST  /challenge-results : Create a new challengeResult.
     *
     * @param challengeResult the challengeResult to create
     * @return the ResponseEntity with status 201 (Created) and with body the new challengeResult, or with status 400 (Bad Request) if the challengeResult has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/challenge-results")
    @Timed
    public ResponseEntity<ChallengeResult> createChallengeResult(@Valid @RequestBody ChallengeResult challengeResult) throws URISyntaxException {
        log.debug("REST request to save ChallengeResult : {}", challengeResult);
        if (challengeResult.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("challengeResult", "idexists", "A new challengeResult cannot already have an ID")).body(null);
        }
        ChallengeResult result = challengeResultRepository.save(challengeResult);
        return ResponseEntity.created(new URI("/api/challenge-results/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("challengeResult", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /challenge-results : Updates an existing challengeResult.
     *
     * @param challengeResult the challengeResult to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated challengeResult,
     * or with status 400 (Bad Request) if the challengeResult is not valid,
     * or with status 500 (Internal Server Error) if the challengeResult couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/challenge-results")
    @Timed
    public ResponseEntity<ChallengeResult> updateChallengeResult(@Valid @RequestBody ChallengeResult challengeResult) throws URISyntaxException {
        log.debug("REST request to update ChallengeResult : {}", challengeResult);
        if (challengeResult.getId() == null) {
            return createChallengeResult(challengeResult);
        }
        ChallengeResult result = challengeResultRepository.save(challengeResult);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("challengeResult", challengeResult.getId().toString()))
            .body(result);
    }

    /**
     * GET  /challenge-results : get all the challengeResults.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of challengeResults in body
     */
    @GetMapping("/challenge-results")
    @Timed
    public List<ChallengeResult> getAllChallengeResults() {
        log.debug("REST request to get all ChallengeResults");
        List<ChallengeResult> challengeResults = challengeResultRepository.findAll();
        return challengeResults;
    }

    /**
     * GET  /challenge-results/:id : get the "id" challengeResult.
     *
     * @param id the id of the challengeResult to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the challengeResult, or with status 404 (Not Found)
     */
    @GetMapping("/challenge-results/{id}")
    @Timed
    public ResponseEntity<ChallengeResult> getChallengeResult(@PathVariable Long id) {
        log.debug("REST request to get ChallengeResult : {}", id);
        ChallengeResult challengeResult = challengeResultRepository.findOne(id);
        return Optional.ofNullable(challengeResult)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /challenge-results/:id : delete the "id" challengeResult.
     *
     * @param id the id of the challengeResult to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/challenge-results/{id}")
    @Timed
    public ResponseEntity<Void> deleteChallengeResult(@PathVariable Long id) {
        log.debug("REST request to delete ChallengeResult : {}", id);
        challengeResultRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("challengeResult", id.toString())).build();
    }
    
    @GetMapping("/challenge-results/challenge/{challengeId}")
    @Timed
    public ChallengeResult getChallengeResultByChallenge(@PathVariable Long challengeId) {
        log.debug("REST request to get ChallengeResult : {}", challengeId);
        ChallengeResult challengeResult = challengeResultRepository.getByChallenge(challengeId);
        return challengeResult;
    }

}
