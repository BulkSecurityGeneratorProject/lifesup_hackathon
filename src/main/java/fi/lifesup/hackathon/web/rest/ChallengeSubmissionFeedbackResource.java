package fi.lifesup.hackathon.web.rest;

import com.codahale.metrics.annotation.Timed;

import fi.lifesup.hackathon.domain.ChallengeSubmission;
import fi.lifesup.hackathon.domain.ChallengeSubmissionFeedback;

import fi.lifesup.hackathon.repository.ChallengeSubmissionFeedbackRepository;
import fi.lifesup.hackathon.repository.ChallengeSubmissionRepository;
import fi.lifesup.hackathon.security.SecurityUtils;
import fi.lifesup.hackathon.service.ChallengeSubmissionFeedbackService;
import fi.lifesup.hackathon.service.UserService;
import fi.lifesup.hackathon.service.dto.ChallengeSubmissionFeedbackDTO;
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
 * REST controller for managing ChallengeSubmissionFeedback.
 */
@RestController
@RequestMapping("/api")
public class ChallengeSubmissionFeedbackResource {

	private final Logger log = LoggerFactory.getLogger(ChallengeSubmissionFeedbackResource.class);

	@Inject
	private ChallengeSubmissionFeedbackRepository challengeSubmissionFeedbackRepository;
	@Inject
	private ChallengeSubmissionFeedbackService challengeSubmissionFeedbackService;

	/**
	 * POST /challenge-submission-feedbacks : Create a new
	 * challengeSubmissionFeedback.
	 *
	 * @param challengeSubmissionFeedback
	 *            the challengeSubmissionFeedback to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new challengeSubmissionFeedback, or with status 400 (Bad Request)
	 *         if the challengeSubmissionFeedback has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/challenge-submission-feedbacks")
	@Timed
	public ResponseEntity<ChallengeSubmissionFeedback> createChallengeSubmissionFeedback(
			@RequestBody ChallengeSubmissionFeedbackDTO challengeSubmissionFeedback) throws URISyntaxException {
		log.debug("REST request to save ChallengeSubmissionFeedback : {}", challengeSubmissionFeedback);
		if (!(SecurityUtils.isCurrentUserInRole("ROLE_HOST") || SecurityUtils.isCurrentUserInRole("ROLE_ADMIN"))) {
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert("challengeSubmissionFeedback", "error", "cannot role"))
					.body(null);
		}
		if (challengeSubmissionFeedback.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("challengeSubmissionFeedback",
					"idexists", "A new challengeSubmissionFeedback cannot already have an ID")).body(null);
		}
		ChallengeSubmissionFeedback result = challengeSubmissionFeedbackService
				.createChallengeSubmission(challengeSubmissionFeedback);
		return ResponseEntity.created(new URI("/api/challenge-submission-feedbacks/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("challengeSubmissionFeedback", result.getId().toString()))
				.body(result);

	}

	/**
	 * PUT /challenge-submission-feedbacks : Updates an existing
	 * challengeSubmissionFeedback.
	 *
	 * @param challengeSubmissionFeedback
	 *            the challengeSubmissionFeedback to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         challengeSubmissionFeedback, or with status 400 (Bad Request) if
	 *         the challengeSubmissionFeedback is not valid, or with status 500
	 *         (Internal Server Error) if the challengeSubmissionFeedback
	 *         couldnt be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/challenge-submission-feedbacks")
	@Timed
	public ResponseEntity<ChallengeSubmissionFeedback> updateChallengeSubmissionFeedback(
			@RequestBody ChallengeSubmissionFeedbackDTO challengeSubmissionFeedback) throws URISyntaxException {
		log.debug("REST request to update ChallengeSubmissionFeedback : {}", challengeSubmissionFeedback);
		if (!(SecurityUtils.isCurrentUserInRole("ROLE_HOST") || SecurityUtils.isCurrentUserInRole("ROLE_ADMIN"))) {
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert("challengeSubmissionFeedback", "error", "cannot role"))
					.body(null);
		}
		if (challengeSubmissionFeedback.getId() == null) {
			return createChallengeSubmissionFeedback(challengeSubmissionFeedback);
		}

		ChallengeSubmissionFeedback result = challengeSubmissionFeedbackService
				.createChallengeSubmission(challengeSubmissionFeedback);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("challengeSubmissionFeedback",
				challengeSubmissionFeedback.getId().toString())).body(result);
	}

	/**
	 * GET /challenge-submission-feedbacks : get all the
	 * challengeSubmissionFeedbacks.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         challengeSubmissionFeedbacks in body
	 */
	@GetMapping("/challenge-submission-feedbacks")
	@Timed
	public List<ChallengeSubmissionFeedback> getAllChallengeSubmissionFeedbacks() {
		log.debug("REST request to get all ChallengeSubmissionFeedbacks");
		List<ChallengeSubmissionFeedback> challengeSubmissionFeedbacks = challengeSubmissionFeedbackRepository
				.findAll();
		return challengeSubmissionFeedbacks;
	}

	/**
	 * GET /challenge-submission-feedbacks/:id : get the "id"
	 * challengeSubmissionFeedback.
	 *
	 * @param id
	 *            the id of the challengeSubmissionFeedback to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         challengeSubmissionFeedback, or with status 404 (Not Found)
	 */
	@GetMapping("/challenge-submission-feedbacks/{id}")
	@Timed
	public ResponseEntity<ChallengeSubmissionFeedback> getChallengeSubmissionFeedback(@PathVariable Long id) {
		log.debug("REST request to get ChallengeSubmissionFeedback : {}", id);
		ChallengeSubmissionFeedback challengeSubmissionFeedback = challengeSubmissionFeedbackRepository.findOne(id);
		return Optional.ofNullable(challengeSubmissionFeedback)
				.map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /challenge-submission-feedbacks/:id : delete the "id"
	 * challengeSubmissionFeedback.
	 *
	 * @param id
	 *            the id of the challengeSubmissionFeedback to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/challenge-submission-feedbacks/{id}")
	@Timed
	public ResponseEntity<Void> deleteChallengeSubmissionFeedback(@PathVariable Long id) {
		log.debug("REST request to delete ChallengeSubmissionFeedback : {}", id);
		challengeSubmissionFeedbackRepository.delete(id);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityDeletionAlert("challengeSubmissionFeedback", id.toString())).build();
	}

}
