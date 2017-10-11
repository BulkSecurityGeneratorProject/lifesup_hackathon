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

import fi.lifesup.hackathon.domain.ApplicationInviteEmail;
import fi.lifesup.hackathon.repository.ApplicationInviteEmailReponsitory;
import fi.lifesup.hackathon.web.rest.util.HeaderUtil;


/**
 * REST controller for managing ApplicationInviteEmail.
 */
@RestController
@RequestMapping("/api")
public class ApplicationInviteEmailResource {

	private final Logger log = LoggerFactory.getLogger(ApplicationInviteEmailResource.class);
	
	@Inject 
	private ApplicationInviteEmailReponsitory applicationInviteEmailReponsitory;
	
	/**
	 * POST /application-invite-emails : Create a new
	 * applicationInviteEmail.
	 *
	 * @param pplicationInviteEmail
	 *            the applicationInviteEmail to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new applicationInviteEmail, or with status 400 (Bad Request) if
	 *         the applicationInviteEmail has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/application-invite-emails")
	@Timed
	public ResponseEntity<ApplicationInviteEmail> createApplicationInviteEmail(
			@RequestBody ApplicationInviteEmail applicationInviteEmail) throws URISyntaxException {
		log.debug("REST request to save applicationInviteEmail : {}", applicationInviteEmail);
		if (applicationInviteEmail.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("applicationInviteEmail",
					"idexists", "A new applicationInviteEmail cannot already have an ID")).body(null);
		}
		ApplicationInviteEmail result = applicationInviteEmailReponsitory.save(applicationInviteEmail);
		return ResponseEntity.created(new URI("/api/challenge-user-applications/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("applicationInviteEmail", result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT /application-invite-emails : Updates an existing
	 * applicationInviteEmail.
	 *
	 * @param applicationInviteEmail
	 *            the applicationInviteEmail to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         applicationInviteEmail, or with status 400 (Bad Request) if the
	 *         applicationInviteEmail is not valid, or with status 500
	 *         (Internal Server Error) if the applicationInviteEmail couldnt
	 *         be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/application-invite-emails")
	@Timed
	public ResponseEntity<ApplicationInviteEmail> updateapplicationInviteEmail(
			@RequestBody ApplicationInviteEmail applicationInviteEmail) throws URISyntaxException {
		log.debug("REST request to update applicationInviteEmail : {}", applicationInviteEmail);
		if (applicationInviteEmail.getId() == null) {
			return createApplicationInviteEmail(applicationInviteEmail);
		}
		ApplicationInviteEmail result = applicationInviteEmailReponsitory.save(applicationInviteEmail);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("applicationInviteEmail",
				applicationInviteEmail.getId().toString())).body(result);
	}

	/**
	 * GET /application-invite-emails : get all the applicationInviteEmails.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         applicationInviteEmails in body
	 */
	@GetMapping("/application-invite-emails")
	@Timed
	public List<ApplicationInviteEmail> getAllapplicationInviteEmails() {
		log.debug("REST request to get all applicationInviteEmails");
		List<ApplicationInviteEmail> applicationInviteEmails = applicationInviteEmailReponsitory.findAll();
		return applicationInviteEmails;
	}

	/**
	 * GET /application-invite-emails/:id : get the "id"
	 * applicationInviteEmail.
	 *
	 * @param id
	 *            the id of the applicationInviteEmail to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         applicationInviteEmail, or with status 404 (Not Found)
	 */
	@GetMapping("/application-invite-emails/{id}")
	@Timed
	public ResponseEntity<ApplicationInviteEmail> getapplicationInviteEmail(@PathVariable Long id) {
		log.debug("REST request to get applicationInviteEmail : {}", id);
		ApplicationInviteEmail applicationInviteEmail = applicationInviteEmailReponsitory.findOne(id);
		return Optional.ofNullable(applicationInviteEmail).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /application-invite-emails/:id : delete the "id"
	 * applicationInviteEmail.
	 *
	 * @param id
	 *            the id of the applicationInviteEmail to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/application-invite-emails/{id}")
	@Timed
	public ResponseEntity<Void> deleteApplicationInviteEmail(@PathVariable Long id) {
		log.debug("REST request to delete applicationInviteEmail : {}", id);
		applicationInviteEmailReponsitory.delete(id);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityDeletionAlert("applicationInviteEmail", id.toString())).build();
	}
	
	@GetMapping("/application-invite-emails/acceptkey/{acceptkey}")
	@Timed
	public ApplicationInviteEmail getapplicationInviteEmailByAcceotkey(@PathVariable String acceptkey) {
		log.debug("REST request to get applicationInviteEmail : {}", acceptkey);
		ApplicationInviteEmail applicationInviteEmail = applicationInviteEmailReponsitory.findByAcceptKey(acceptkey);
		return applicationInviteEmail;
	}
}
