package fi.lifesup.hackathon.web.rest;

import com.codahale.metrics.annotation.Timed;
import fi.lifesup.hackathon.domain.ChallengeWorkspaceQuestion;

import fi.lifesup.hackathon.repository.ChallengeWorkspaceQuestionRepository;
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
 * REST controller for managing ChallengeWorkspaceQuestion.
 */
@RestController
@RequestMapping("/api")
public class ChallengeWorkspaceQuestionResource {

    private final Logger log = LoggerFactory.getLogger(ChallengeWorkspaceQuestionResource.class);
        
    @Inject
    private ChallengeWorkspaceQuestionRepository challengeWorkspaceQuestionRepository;

    /**
     * POST  /challenge-workspace-questions : Create a new challengeWorkspaceQuestion.
     *
     * @param challengeWorkspaceQuestion the challengeWorkspaceQuestion to create
     * @return the ResponseEntity with status 201 (Created) and with body the new challengeWorkspaceQuestion, or with status 400 (Bad Request) if the challengeWorkspaceQuestion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/challenge-workspace-questions")
    @Timed
    public ResponseEntity<ChallengeWorkspaceQuestion> createChallengeWorkspaceQuestion(@Valid @RequestBody ChallengeWorkspaceQuestion challengeWorkspaceQuestion) throws URISyntaxException {
        log.debug("REST request to save ChallengeWorkspaceQuestion : {}", challengeWorkspaceQuestion);
        if (challengeWorkspaceQuestion.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("challengeWorkspaceQuestion", "idexists", "A new challengeWorkspaceQuestion cannot already have an ID")).body(null);
        }
        ChallengeWorkspaceQuestion result = challengeWorkspaceQuestionRepository.save(challengeWorkspaceQuestion);
        return ResponseEntity.created(new URI("/api/challenge-workspace-questions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("challengeWorkspaceQuestion", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /challenge-workspace-questions : Updates an existing challengeWorkspaceQuestion.
     *
     * @param challengeWorkspaceQuestion the challengeWorkspaceQuestion to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated challengeWorkspaceQuestion,
     * or with status 400 (Bad Request) if the challengeWorkspaceQuestion is not valid,
     * or with status 500 (Internal Server Error) if the challengeWorkspaceQuestion couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/challenge-workspace-questions")
    @Timed
    public ResponseEntity<ChallengeWorkspaceQuestion> updateChallengeWorkspaceQuestion(@Valid @RequestBody ChallengeWorkspaceQuestion challengeWorkspaceQuestion) throws URISyntaxException {
        log.debug("REST request to update ChallengeWorkspaceQuestion : {}", challengeWorkspaceQuestion);
        if (challengeWorkspaceQuestion.getId() == null) {
            return createChallengeWorkspaceQuestion(challengeWorkspaceQuestion);
        }
        ChallengeWorkspaceQuestion result = challengeWorkspaceQuestionRepository.save(challengeWorkspaceQuestion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("challengeWorkspaceQuestion", challengeWorkspaceQuestion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /challenge-workspace-questions : get all the challengeWorkspaceQuestions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of challengeWorkspaceQuestions in body
     */
    @GetMapping("/challenge-workspace-questions")
    @Timed
    public List<ChallengeWorkspaceQuestion> getAllChallengeWorkspaceQuestions() {
        log.debug("REST request to get all ChallengeWorkspaceQuestions");
        List<ChallengeWorkspaceQuestion> challengeWorkspaceQuestions = challengeWorkspaceQuestionRepository.findAll();
        return challengeWorkspaceQuestions;
    }

    /**
     * GET  /challenge-workspace-questions/:id : get the "id" challengeWorkspaceQuestion.
     *
     * @param id the id of the challengeWorkspaceQuestion to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the challengeWorkspaceQuestion, or with status 404 (Not Found)
     */
    @GetMapping("/challenge-workspace-questions/{id}")
    @Timed
    public ResponseEntity<ChallengeWorkspaceQuestion> getChallengeWorkspaceQuestion(@PathVariable Long id) {
        log.debug("REST request to get ChallengeWorkspaceQuestion : {}", id);
        ChallengeWorkspaceQuestion challengeWorkspaceQuestion = challengeWorkspaceQuestionRepository.findOne(id);
        return Optional.ofNullable(challengeWorkspaceQuestion)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /challenge-workspace-questions/:id : delete the "id" challengeWorkspaceQuestion.
     *
     * @param id the id of the challengeWorkspaceQuestion to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/challenge-workspace-questions/{id}")
    @Timed
    public ResponseEntity<Void> deleteChallengeWorkspaceQuestion(@PathVariable Long id) {
        log.debug("REST request to delete ChallengeWorkspaceQuestion : {}", id);
        challengeWorkspaceQuestionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("challengeWorkspaceQuestion", id.toString())).build();
    }

}
