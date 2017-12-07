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

import fi.lifesup.hackathon.domain.ChallengeFeedback;
import fi.lifesup.hackathon.repository.ChallengeFeedbackRepository;
import fi.lifesup.hackathon.service.ChallengeFeedbackService;
import fi.lifesup.hackathon.service.dto.ChallengeFeedbackDTO;
import fi.lifesup.hackathon.web.rest.util.HeaderUtil;

/**
 * REST controller for managing ChallengeFeedback.
 */
@RestController
@RequestMapping("/api")
public class ChallengeFeedbackResource {

	private final Logger log = LoggerFactory.getLogger(ChallengeFeedbackResource.class);

	@Inject
	private ChallengeFeedbackRepository challengeFeedbackRepository;
	@Inject
	private ChallengeFeedbackService challengeFeedbackService;

	/**
	 * POST /challenge-feedbacks : Create a new challengeFeedback.
	 *
	 * @param challengeFeedback
	 *            the challengeFeedback to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         challengeFeedback, or with status 400 (Bad Request) if the
	 *         challengeFeedback has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/challenge-feedbacks")
	@Timed
	public ResponseEntity<ChallengeFeedback> createChallengeFeedback(
			@Valid @RequestBody ChallengeFeedback challengeFeedback) throws URISyntaxException {
		log.debug("REST request to save ChallengeFeedback : {}", challengeFeedback);
		if (challengeFeedback.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("challengeFeedback", "idexists",
					"A new challengeFeedback cannot already have an ID")).body(null);
		}
		ChallengeFeedback result = challengeFeedbackRepository.save(challengeFeedback);
		return ResponseEntity.created(new URI("/api/challenge-feedbacks/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("challengeFeedback", result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT /challenge-feedbacks : Updates an existing challengeFeedback.
	 *
	 * @param challengeFeedback
	 *            the challengeFeedback to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         challengeFeedback, or with status 400 (Bad Request) if the
	 *         challengeFeedback is not valid, or with status 500 (Internal Server
	 *         Error) if the challengeFeedback couldnt be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/challenge-feedbacks")
	@Timed
	public ResponseEntity<ChallengeFeedback> updateChallengeFeedback(
			@Valid @RequestBody ChallengeFeedback challengeFeedback) throws URISyntaxException {
		log.debug("REST request to update ChallengeFeedback : {}", challengeFeedback);
		if (challengeFeedback.getId() == null) {
			return createChallengeFeedback(challengeFeedback);
		}
		ChallengeFeedback result = challengeFeedbackRepository.save(challengeFeedback);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert("challengeFeedback", challengeFeedback.getId().toString()))
				.body(result);
	}

	/**
	 * GET /challenge-feedbacks : get all the challengeFeedbacks.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         challengeFeedbacks in body
	 */
	@GetMapping("/challenge-feedbacks")
	@Timed
	public List<ChallengeFeedback> getAllChallengeFeedbacks() {
		log.debug("REST request to get all ChallengeFeedbacks");
		List<ChallengeFeedback> challengeFeedbacks = challengeFeedbackRepository.findAll();
		return challengeFeedbacks;
	}

	/**
	 * GET /challenge-feedbacks/:id : get the "id" challengeFeedback.
	 *
	 * @param id
	 *            the id of the challengeFeedback to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         challengeFeedback, or with status 404 (Not Found)
	 */
	@GetMapping("/challenge-feedbacks/{id}")
	@Timed
	public ResponseEntity<ChallengeFeedback> getChallengeFeedback(@PathVariable Long id) {
		log.debug("REST request to get ChallengeFeedback : {}", id);
		ChallengeFeedback challengeFeedback = challengeFeedbackRepository.findOne(id);
		return Optional.ofNullable(challengeFeedback).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /challenge-feedbacks/:id : delete the "id" challengeFeedback.
	 *
	 * @param id
	 *            the id of the challengeFeedback to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/challenge-feedbacks/{id}")
	@Timed
	public ResponseEntity<Void> deleteChallengeFeedback(@PathVariable Long id) {
		log.debug("REST request to delete ChallengeFeedback : {}", id);
		challengeFeedbackRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("challengeFeedback", id.toString()))
				.build();
	}

	@PostMapping("/challenge-feedbacks-created")
	@Timed
	public ResponseEntity<ChallengeFeedback> createdChallengeFeedback(
			@Valid @RequestBody ChallengeFeedbackDTO challengeFeedbackDTO) throws URISyntaxException {
		log.debug("REST request to save ChallengeFeedback : {}", challengeFeedbackDTO);

		if (challengeFeedbackDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("challengeFeedback", "idexists",
					"A new challengeFeedback cannot already have an ID")).body(null);
		}
		ChallengeFeedback challengeFeedback = challengeFeedbackService.createdChallengeFeedback(challengeFeedbackDTO);

		if (challengeFeedback == null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("challengeFeedback", "idexists",
					"A new challengeFeedback cannot already have an ID")).body(null);
		}
		ChallengeFeedback result = challengeFeedbackRepository.save(challengeFeedback);
		return ResponseEntity.created(new URI("/api/challenge-feedbacks-created/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("challengeFeedback", result.getId().toString()))
				.body(result);
	}
	
	@PutMapping("/challenge-feedbacks-created")
	@Timed
	public ResponseEntity<ChallengeFeedback> updateChallengeFeedback(
			@Valid @RequestBody ChallengeFeedbackDTO challengeFeedbackDTO) throws URISyntaxException {
		log.debug("REST request to save ChallengeFeedback : {}", challengeFeedbackDTO);

		if (challengeFeedbackDTO.getId() != null) {
			createdChallengeFeedback(challengeFeedbackDTO);
		}
		ChallengeFeedback challengeFeedback = challengeFeedbackService.createdChallengeFeedback(challengeFeedbackDTO);

		if (challengeFeedback == null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("challengeFeedback", "idexists",
					"A new challengeFeedback cannot already have an ID")).body(null);
		}
		ChallengeFeedback result = challengeFeedbackRepository.save(challengeFeedback);
		return ResponseEntity.created(new URI("/api/challenge-feedbacks-created/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("challengeFeedback", result.getId().toString()))
				.body(result);
	}

	@GetMapping("/challenge-feedbacks/challenge/{challengeId}")
	@Timed
	public List<ChallengeFeedbackDTO> getAllChallengeFeedbacks(@PathVariable Long challengeId) {
		log.debug("REST request to get all ChallengeFeedbacks");
		List<ChallengeFeedbackDTO> challengeFeedbacks = challengeFeedbackService.getFeedbackByChallenge(challengeId);
		return challengeFeedbacks;
	}

}
