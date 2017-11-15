package fi.lifesup.hackathon.web.rest;

import com.codahale.metrics.annotation.Timed;
import fi.lifesup.hackathon.domain.ChallengeWorkspaceAnswer;

import fi.lifesup.hackathon.repository.ChallengeWorkspaceAnswerRepository;
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
 * REST controller for managing ChallengeWorkspaceAnswer.
 */
@RestController
@RequestMapping("/api")
public class ChallengeWorkspaceAnswerResource {

    private final Logger log = LoggerFactory.getLogger(ChallengeWorkspaceAnswerResource.class);
        
    @Inject
    private ChallengeWorkspaceAnswerRepository challengeWorkspaceAnswerRepository;

    /**
     * POST  /challenge-workspace-answers : Create a new challengeWorkspaceAnswer.
     *
     * @param challengeWorkspaceAnswer the challengeWorkspaceAnswer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new challengeWorkspaceAnswer, or with status 400 (Bad Request) if the challengeWorkspaceAnswer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/challenge-workspace-answers")
    @Timed
    public ResponseEntity<ChallengeWorkspaceAnswer> createChallengeWorkspaceAnswer(@Valid @RequestBody ChallengeWorkspaceAnswer challengeWorkspaceAnswer) throws URISyntaxException {
        log.debug("REST request to save ChallengeWorkspaceAnswer : {}", challengeWorkspaceAnswer);
        if (challengeWorkspaceAnswer.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("challengeWorkspaceAnswer", "idexists", "A new challengeWorkspaceAnswer cannot already have an ID")).body(null);
        }
        ChallengeWorkspaceAnswer result = challengeWorkspaceAnswerRepository.save(challengeWorkspaceAnswer);
        return ResponseEntity.created(new URI("/api/challenge-workspace-answers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("challengeWorkspaceAnswer", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /challenge-workspace-answers : Updates an existing challengeWorkspaceAnswer.
     *
     * @param challengeWorkspaceAnswer the challengeWorkspaceAnswer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated challengeWorkspaceAnswer,
     * or with status 400 (Bad Request) if the challengeWorkspaceAnswer is not valid,
     * or with status 500 (Internal Server Error) if the challengeWorkspaceAnswer couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/challenge-workspace-answers")
    @Timed
    public ResponseEntity<ChallengeWorkspaceAnswer> updateChallengeWorkspaceAnswer(@Valid @RequestBody ChallengeWorkspaceAnswer challengeWorkspaceAnswer) throws URISyntaxException {
        log.debug("REST request to update ChallengeWorkspaceAnswer : {}", challengeWorkspaceAnswer);
        if (challengeWorkspaceAnswer.getId() == null) {
            return createChallengeWorkspaceAnswer(challengeWorkspaceAnswer);
        }
        ChallengeWorkspaceAnswer result = challengeWorkspaceAnswerRepository.save(challengeWorkspaceAnswer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("challengeWorkspaceAnswer", challengeWorkspaceAnswer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /challenge-workspace-answers : get all the challengeWorkspaceAnswers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of challengeWorkspaceAnswers in body
     */
    @GetMapping("/challenge-workspace-answers")
    @Timed
    public List<ChallengeWorkspaceAnswer> getAllChallengeWorkspaceAnswers() {
        log.debug("REST request to get all ChallengeWorkspaceAnswers");
        List<ChallengeWorkspaceAnswer> challengeWorkspaceAnswers = challengeWorkspaceAnswerRepository.findAll();
        return challengeWorkspaceAnswers;
    }

    /**
     * GET  /challenge-workspace-answers/:id : get the "id" challengeWorkspaceAnswer.
     *
     * @param id the id of the challengeWorkspaceAnswer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the challengeWorkspaceAnswer, or with status 404 (Not Found)
     */
    @GetMapping("/challenge-workspace-answers/{id}")
    @Timed
    public ResponseEntity<ChallengeWorkspaceAnswer> getChallengeWorkspaceAnswer(@PathVariable Long id) {
        log.debug("REST request to get ChallengeWorkspaceAnswer : {}", id);
        ChallengeWorkspaceAnswer challengeWorkspaceAnswer = challengeWorkspaceAnswerRepository.findOne(id);
        return Optional.ofNullable(challengeWorkspaceAnswer)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /challenge-workspace-answers/:id : delete the "id" challengeWorkspaceAnswer.
     *
     * @param id the id of the challengeWorkspaceAnswer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/challenge-workspace-answers/{id}")
    @Timed
    public ResponseEntity<Void> deleteChallengeWorkspaceAnswer(@PathVariable Long id) {
        log.debug("REST request to delete ChallengeWorkspaceAnswer : {}", id);
        challengeWorkspaceAnswerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("challengeWorkspaceAnswer", id.toString())).build();
    }

}
