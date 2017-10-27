package fi.lifesup.hackathon.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.websocket.server.PathParam;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import fi.lifesup.hackathon.domain.ChallengeUserApplication;
import fi.lifesup.hackathon.repository.ChallengeUserApplicationRepository;
import fi.lifesup.hackathon.service.ApplicationService;
import fi.lifesup.hackathon.service.UserService;
import fi.lifesup.hackathon.web.rest.util.HeaderUtil;

/**
 * REST controller for managing ChallengeUserApplication.
 */
@RestController
@RequestMapping("/api")
public class ChallengeUserApplicationResource {

	private final Logger log = LoggerFactory.getLogger(ChallengeUserApplicationResource.class);

	@Inject
	private ChallengeUserApplicationRepository challengeUserApplicationRepository;

	@Inject
	private ApplicationService applicationService;

	@Inject
	private UserService userService;

	/**
	 * POST /challenge-user-applications : Create a new
	 * challengeUserApplication.
	 *
	 * @param challengeUserApplication
	 *            the challengeUserApplication to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new challengeUserApplication, or with status 400 (Bad Request) if
	 *         the challengeUserApplication has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/challenge-user-applications")
	@Timed
	public ResponseEntity<ChallengeUserApplication> createChallengeUserApplication(
			@RequestBody ChallengeUserApplication challengeUserApplication) throws URISyntaxException {
		log.debug("REST request to save ChallengeUserApplication : {}", challengeUserApplication);
		if (challengeUserApplication.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("challengeUserApplication",
					"idexists", "A new challengeUserApplication cannot already have an ID")).body(null);
		}
		ChallengeUserApplication result = challengeUserApplicationRepository.save(challengeUserApplication);
		return ResponseEntity.created(new URI("/api/challenge-user-applications/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("challengeUserApplication", result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT /challenge-user-applications : Updates an existing
	 * challengeUserApplication.
	 *
	 * @param challengeUserApplication
	 *            the challengeUserApplication to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         challengeUserApplication, or with status 400 (Bad Request) if the
	 *         challengeUserApplication is not valid, or with status 500
	 *         (Internal Server Error) if the challengeUserApplication couldnt
	 *         be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/challenge-user-applications")
	@Timed
	public ResponseEntity<ChallengeUserApplication> updateChallengeUserApplication(
			@RequestBody ChallengeUserApplication challengeUserApplication) throws URISyntaxException {
		log.debug("REST request to update ChallengeUserApplication : {}", challengeUserApplication);
		if (challengeUserApplication.getId() == null) {
			return createChallengeUserApplication(challengeUserApplication);
		}
		ChallengeUserApplication result = challengeUserApplicationRepository.save(challengeUserApplication);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("challengeUserApplication",
				challengeUserApplication.getId().toString())).body(result);
	}

	/**
	 * GET /challenge-user-applications : get all the challengeUserApplications.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         challengeUserApplications in body
	 */
	@GetMapping("/challenge-user-applications")
	@Timed
	public List<ChallengeUserApplication> getAllChallengeUserApplications() {
		log.debug("REST request to get all ChallengeUserApplications");
		List<ChallengeUserApplication> challengeUserApplications = challengeUserApplicationRepository.findAll();
		return challengeUserApplications;
	}

	/**
	 * GET /challenge-user-applications/:id : get the "id"
	 * challengeUserApplication.
	 *
	 * @param id
	 *            the id of the challengeUserApplication to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         challengeUserApplication, or with status 404 (Not Found)
	 */
	@GetMapping("/challenge-user-applications/{id}")
	@Timed
	public ResponseEntity<ChallengeUserApplication> getChallengeUserApplication(@PathVariable Long id) {
		log.debug("REST request to get ChallengeUserApplication : {}", id);
		ChallengeUserApplication challengeUserApplication = challengeUserApplicationRepository.findOne(id);
		return Optional.ofNullable(challengeUserApplication).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /challenge-user-applications/:id : delete the "id"
	 * challengeUserApplication.
	 *
	 * @param id
	 *            the id of the challengeUserApplication to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/challenge-user-applications/{id}")
	@Timed
	public ResponseEntity<Void> deleteChallengeUserApplication(@PathVariable Long id) {
		log.debug("REST request to delete ChallengeUserApplication : {}", id);
		challengeUserApplicationRepository.delete(id);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityDeletionAlert("challengeUserApplication", id.toString())).build();
	}

	@GetMapping("/challenge-user-applications/challenge/{challengeId}")
	@Timed
	public ChallengeUserApplication getApplication(@PathVariable Long challengeId) {
		log.debug("REST request to get ChallengeUserApplication : {}");
		ChallengeUserApplication challengeUserApplication = challengeUserApplicationRepository
				.findByChallengeIdAndUserId(challengeId, userService.getCurrentUser().getId());
		return challengeUserApplication;
	}

	@GetMapping("/challenge-user-applications/current-user")
	@Timed
	public List<ChallengeUserApplication> getAllApplicationsByUser() {
		log.debug("REST request to get all ChallengeUserApplications");
		List<ChallengeUserApplication> challengeUserApplications = challengeUserApplicationRepository
				.findByUserId(userService.getCurrentUser().getId());
		return challengeUserApplications;
	}

	@PutMapping("/challenge-user-applications/accept-invitation")
	@Timed
	public ResponseEntity<Void> finishAcceptedInvitation(@RequestBody String acceptKey) {		
		applicationService.finishAcceptInvitation(acceptKey, true);
		return ResponseEntity.ok().build();
	}
	@PutMapping("/challenge-user-applications/decline-invitation")
	@Timed
	public ResponseEntity<Void> finishDeclinedInvitation(@RequestBody String acceptKey) {		
	applicationService.finishAcceptInvitation(acceptKey, false);
		return ResponseEntity.ok().build();
	}
}
