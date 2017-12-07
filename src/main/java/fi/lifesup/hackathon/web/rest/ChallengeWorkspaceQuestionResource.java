package fi.lifesup.hackathon.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
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

import fi.lifesup.hackathon.domain.Application;
import fi.lifesup.hackathon.domain.ChallengeWorkspaceQuestion;
import fi.lifesup.hackathon.domain.User;
import fi.lifesup.hackathon.repository.ApplicationRepository;
import fi.lifesup.hackathon.repository.ChallengeWorkspaceQuestionRepository;
import fi.lifesup.hackathon.repository.UserRepository;
import fi.lifesup.hackathon.security.SecurityUtils;
import fi.lifesup.hackathon.service.ChallengeWorkspaceQuestionService;
import fi.lifesup.hackathon.service.MailService;
import fi.lifesup.hackathon.service.dto.ChallengeWorkspaceQuestionDTO;
import fi.lifesup.hackathon.web.rest.util.HeaderUtil;

/**
 * REST controller for managing ChallengeWorkspaceQuestion.
 */
@RestController
@RequestMapping("/api")
public class ChallengeWorkspaceQuestionResource {

	private final Logger log = LoggerFactory.getLogger(ChallengeWorkspaceQuestionResource.class);

	@Inject
	private ChallengeWorkspaceQuestionRepository challengeWorkspaceQuestionRepository;

	@Inject
	private UserRepository userReponsitory;

	@Inject
	private MailService mailService;

	@Inject
	private ApplicationRepository applicationResponsitory;

	@Inject
	private ChallengeWorkspaceQuestionService challengeWorkspaceQuestionService;

	//
	/**
	 * POST /challenge-workspace-questions : Create a new
	 * challengeWorkspaceQuestion.
	 *
	 * @param challengeWorkspaceQuestion
	 *            the challengeWorkspaceQuestion to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new challengeWorkspaceQuestion, or with status 400 (Bad Request)
	 *         if the challengeWorkspaceQuestion has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/challenge-workspace-questions")
	@Timed
	public ResponseEntity<ChallengeWorkspaceQuestion> createChallengeWorkspaceQuestion(
			@Valid @RequestBody ChallengeWorkspaceQuestionDTO challengeWorkspaceQuestion, HttpServletRequest request)
			throws URISyntaxException {
		log.debug("REST request to save ChallengeWorkspaceQuestion : {}", challengeWorkspaceQuestion);
		if (challengeWorkspaceQuestion.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("challengeWorkspaceQuestion",
					"idexists", "A new challengeWorkspaceQuestion cannot already have an ID")).body(null);
		}

		if (!SecurityUtils.isCurrentUserInRole("ROLE_USER") && !SecurityUtils.isCurrentUserInRole("ROLE_ADMIN")) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("challengeWorkspaceQuestion",
					"idexists", "A new challengeWorkspaceQuestion can only create  by user")).body(null);
		}

		ChallengeWorkspaceQuestion workspaceQuestion = challengeWorkspaceQuestionService
				.saveChallengeWorkspaceQuestion(challengeWorkspaceQuestion);

		if (workspaceQuestion == null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("challengeWorkspaceQuestion",
					"idexists", "A new challengeWorkspaceQuestion cannot already have an ID")).body(null);
		}
		workspaceQuestion.setCreatedBy(SecurityUtils.getCurrentUserLogin());
		workspaceQuestion.setCreatedDate(ZonedDateTime.now());
		ChallengeWorkspaceQuestion result = challengeWorkspaceQuestionRepository.save(workspaceQuestion);

		User userCompany = userReponsitory.findOne(result.getWorkspace().getChallenge().getCreated_by());
		String baseUrl = request.getScheme() + // "http"
				"://" + // "://"
				request.getServerName() + // "myhost"
				":" + // ":"
				request.getServerPort() + // "80"
				request.getContextPath();
		Application application = applicationResponsitory.findOne(challengeWorkspaceQuestion.getApplicationId());
		mailService.sendQuestionMail(application, baseUrl, userCompany.getLangKey(), userCompany.getEmail());

		return ResponseEntity.created(new URI("/api/challenge-workspace-questions/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("challengeWorkspaceQuestion", result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT /challenge-workspace-questions : Updates an existing
	 * challengeWorkspaceQuestion.
	 *
	 * @param challengeWorkspaceQuestion
	 *            the challengeWorkspaceQuestion to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         challengeWorkspaceQuestion, or with status 400 (Bad Request) if
	 *         the challengeWorkspaceQuestion is not valid, or with status 500
	 *         (Internal Server Error) if the challengeWorkspaceQuestion couldnt
	 *         be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/challenge-workspace-questions")
	@Timed
	public ResponseEntity<ChallengeWorkspaceQuestion> updateChallengeWorkspaceQuestion(
			@Valid @RequestBody ChallengeWorkspaceQuestionDTO challengeWorkspaceQuestion, HttpServletRequest request)
			throws URISyntaxException {
		log.debug("REST request to update ChallengeWorkspaceQuestion : {}", challengeWorkspaceQuestion);
		if (challengeWorkspaceQuestion.getId() == null) {
			createChallengeWorkspaceQuestion(challengeWorkspaceQuestion, request);
		}

		if (!SecurityUtils.isCurrentUserInRole("ROLE_USER") && !SecurityUtils.isCurrentUserInRole("ROLE_ADMIN")) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("challengeWorkspaceQuestion",
					"idexists", "A new challengeWorkspaceQuestion can only create  by user")).body(null);
		}

		ChallengeWorkspaceQuestion workspaceQuestion = challengeWorkspaceQuestionService
				.saveChallengeWorkspaceQuestion(challengeWorkspaceQuestion);

		if (workspaceQuestion == null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("challengeWorkspaceQuestion",
					"idexists", "A new challengeWorkspaceQuestion cannot already have an ID")).body(null);
		}
		
		ChallengeWorkspaceQuestion result = challengeWorkspaceQuestionRepository.save(workspaceQuestion);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("challengeWorkspaceQuestion",
				challengeWorkspaceQuestion.getId().toString())).body(result);
	}

	/**
	 * GET /challenge-workspace-questions : get all the
	 * challengeWorkspaceQuestions.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         challengeWorkspaceQuestions in body
	 */
	@GetMapping("/challenge-workspace-questions")
	@Timed
	public List<ChallengeWorkspaceQuestion> getAllChallengeWorkspaceQuestions() {
		log.debug("REST request to get all ChallengeWorkspaceQuestions");
		List<ChallengeWorkspaceQuestion> challengeWorkspaceQuestions = challengeWorkspaceQuestionRepository.findAll();
		return challengeWorkspaceQuestions;
	}

	/**
	 * GET /challenge-workspace-questions/:id : get the "id"
	 * challengeWorkspaceQuestion.
	 *
	 * @param id
	 *            the id of the challengeWorkspaceQuestion to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         challengeWorkspaceQuestion, or with status 404 (Not Found)
	 */
	@GetMapping("/challenge-workspace-questions/{id}")
	@Timed
	public ResponseEntity<ChallengeWorkspaceQuestion> getChallengeWorkspaceQuestion(@PathVariable Long id) {
		log.debug("REST request to get ChallengeWorkspaceQuestion : {}", id);
		ChallengeWorkspaceQuestion challengeWorkspaceQuestion = challengeWorkspaceQuestionRepository.findOne(id);
		return Optional.ofNullable(challengeWorkspaceQuestion)
				.map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /challenge-workspace-questions/:id : delete the "id"
	 * challengeWorkspaceQuestion.
	 *
	 * @param id
	 *            the id of the challengeWorkspaceQuestion to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/challenge-workspace-questions/{id}")
	@Timed
	public ResponseEntity<Void> deleteChallengeWorkspaceQuestion(@PathVariable Long id) {
		log.debug("REST request to delete ChallengeWorkspaceQuestion : {}", id);
		challengeWorkspaceQuestionRepository.delete(id);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityDeletionAlert("challengeWorkspaceQuestion", id.toString())).build();
	}	

	@GetMapping("/challenge-workspace-questions/details/{questionId}")
	@Timed
	public ChallengeWorkspaceQuestionDTO getQuestonDetail(@PathVariable Long questionId) {
		log.debug("REST request to get all ChallengeWorkspaceQuestions", questionId);

		ChallengeWorkspaceQuestionDTO questionDTO = challengeWorkspaceQuestionService
				.getQuestionDTO(questionId);
		return questionDTO;
	}

}
