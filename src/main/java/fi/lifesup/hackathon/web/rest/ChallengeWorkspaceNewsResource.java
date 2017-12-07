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

import fi.lifesup.hackathon.domain.ChallengeWorkspaceNews;
import fi.lifesup.hackathon.repository.ChallengeWorkspaceNewsRepository;
import fi.lifesup.hackathon.security.SecurityUtils;
import fi.lifesup.hackathon.service.ChallengeWorkspaceNewsService;
import fi.lifesup.hackathon.service.dto.ChallengeWorkspaceNewsDTO;
import fi.lifesup.hackathon.web.rest.util.HeaderUtil;

/**
 * REST controller for managing ChallengeWorkspaceNews.
 */
@RestController
@RequestMapping("/api")
public class ChallengeWorkspaceNewsResource {

	private final Logger log = LoggerFactory.getLogger(ChallengeWorkspaceNewsResource.class);

	@Inject
	private ChallengeWorkspaceNewsRepository challengeWorkspaceNewsRepository;
	@Inject
	private ChallengeWorkspaceNewsService challengeWorkspaceNewsService;

	/**
	 * POST /challenge-workspace-news : Create a new challengeWorkspaceNews.
	 *
	 * @param challengeWorkspaceNews
	 *            the challengeWorkspaceNews to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new challengeWorkspaceNews, or with status 400 (Bad Request) if
	 *         the challengeWorkspaceNews has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/challenge-workspace-news")
	@Timed
	public ResponseEntity<ChallengeWorkspaceNews> createChallengeWorkspaceNews(
			@Valid @RequestBody ChallengeWorkspaceNews challengeWorkspaceNews) throws URISyntaxException {
		log.debug("REST request to save ChallengeWorkspaceNews : {}", challengeWorkspaceNews);
		if (challengeWorkspaceNews.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("challengeWorkspaceNews",
					"idexists", "A new challengeWorkspaceNews cannot already have an ID")).body(null);
		}
		ChallengeWorkspaceNews result = challengeWorkspaceNewsRepository.save(challengeWorkspaceNews);
		return ResponseEntity.created(new URI("/api/challenge-workspace-news/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("challengeWorkspaceNews", result.getId().toString()))
				.body(result);
	}

	@PostMapping("/challenge-workspace-news-created")
	@Timed
	public ResponseEntity<ChallengeWorkspaceNews> createWorkspaceNews(@Valid @RequestBody ChallengeWorkspaceNewsDTO challengeWorkspaceNewsDTO) throws URISyntaxException {
		log.debug("REST request to save ChallengeWorkspaceNews : {}", challengeWorkspaceNewsDTO);
		

		if (!SecurityUtils.isCurrentUserInRole("ROLE_HOST")) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("challengeWorkspaceNews",
					"idexists", "A new challengeWorkspaceNews cannot already have an ID")).body(null);
		}
		
		if (challengeWorkspaceNewsDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("challengeWorkspaceNews",
					"idexists", "A new challengeWorkspaceNews cannot already have an ID")).body(null);
		}
		
		ChallengeWorkspaceNews challengeWorkspaceNews = challengeWorkspaceNewsService
				.createChallengeWorkspaceNews(challengeWorkspaceNewsDTO);
		
		if (challengeWorkspaceNews == null) {
			return ResponseEntity.badRequest().headers(
					HeaderUtil.createFailureAlert("challengeWorkspaceNews", "idexists", "cannot challenge workspace"))
					.body(null);
		}
		ChallengeWorkspaceNews result = challengeWorkspaceNewsRepository.save(challengeWorkspaceNews);

		return ResponseEntity.created(new URI("/api/challenge-workspace-news-created/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("challengeWorkspaceNews", result.getId().toString()))
				.body(result);

	}

	/**
	 * PUT /challenge-workspace-news : Updates an existing
	 * challengeWorkspaceNews.
	 *
	 * @param challengeWorkspaceNews
	 *            the challengeWorkspaceNews to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         challengeWorkspaceNews, or with status 400 (Bad Request) if the
	 *         challengeWorkspaceNews is not valid, or with status 500 (Internal
	 *         Server Error) if the challengeWorkspaceNews couldnt be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/challenge-workspace-news")
	@Timed
	public ResponseEntity<ChallengeWorkspaceNews> updateChallengeWorkspaceNews(
			@Valid @RequestBody ChallengeWorkspaceNews challengeWorkspaceNews) throws URISyntaxException {
		log.debug("REST request to update ChallengeWorkspaceNews : {}", challengeWorkspaceNews);
		if (challengeWorkspaceNews.getId() == null) {
			return createChallengeWorkspaceNews(challengeWorkspaceNews);
		}
		ChallengeWorkspaceNews result = challengeWorkspaceNewsRepository.save(challengeWorkspaceNews);
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert("challengeWorkspaceNews", challengeWorkspaceNews.getId().toString()))
				.body(result);
	}

	/**
	 * GET /challenge-workspace-news : get all the challengeWorkspaceNews.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         challengeWorkspaceNews in body
	 */
	@GetMapping("/challenge-workspace-news")
	@Timed
	public List<ChallengeWorkspaceNews> getAllChallengeWorkspaceNews() {
		log.debug("REST request to get all ChallengeWorkspaceNews");
		List<ChallengeWorkspaceNews> challengeWorkspaceNews = challengeWorkspaceNewsRepository.findAll();
		return challengeWorkspaceNews;
	}

	/**
	 * GET /challenge-workspace-news/:id : get the "id" challengeWorkspaceNews.
	 *
	 * @param id
	 *            the id of the challengeWorkspaceNews to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         challengeWorkspaceNews, or with status 404 (Not Found)
	 */
	@GetMapping("/challenge-workspace-news/{id}")
	@Timed
	public ResponseEntity<ChallengeWorkspaceNews> getChallengeWorkspaceNews(@PathVariable Long id) {
		log.debug("REST request to get ChallengeWorkspaceNews : {}", id);
		ChallengeWorkspaceNews challengeWorkspaceNews = challengeWorkspaceNewsRepository.findOne(id);
		return Optional.ofNullable(challengeWorkspaceNews).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /challenge-workspace-news/:id : delete the "id"
	 * challengeWorkspaceNews.
	 *
	 * @param id
	 *            the id of the challengeWorkspaceNews to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/challenge-workspace-news/{id}")
	@Timed
	public ResponseEntity<Void> deleteChallengeWorkspaceNews(@PathVariable Long id) {
		log.debug("REST request to delete ChallengeWorkspaceNews : {}", id);
		challengeWorkspaceNewsRepository.delete(id);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityDeletionAlert("challengeWorkspaceNews", id.toString())).build();
	}

	@PutMapping("/challenge-workspace-news-created")
	@Timed
	public ResponseEntity<ChallengeWorkspaceNews> updateWorkspaceNews(
			@Valid @RequestBody ChallengeWorkspaceNewsDTO challengeWorkspaceNewsDTO) throws URISyntaxException {
		log.debug("REST request to save ChallengeWorkspaceNews : {}", challengeWorkspaceNewsDTO);
		
		if (!SecurityUtils.isCurrentUserInRole("ROLE_HOST")) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("challengeWorkspaceNews",
					"idexists", "A new challengeWorkspaceNews cannot already have an ID")).body(null);
		}
		
		if (challengeWorkspaceNewsDTO.getId() != null) {
			createWorkspaceNews(challengeWorkspaceNewsDTO);
		}
		
		ChallengeWorkspaceNews challengeWorkspaceNews = challengeWorkspaceNewsService
				.createChallengeWorkspaceNews(challengeWorkspaceNewsDTO);
		if(challengeWorkspaceNews == null){
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("challengeWorkspaceNews",
					"idexists", "A challengeWorkspace is not exits")).body(null);
		}
		
		ChallengeWorkspaceNews result = challengeWorkspaceNewsRepository.save(challengeWorkspaceNews);
		
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert("challengeWorkspaceNews", challengeWorkspaceNews.getId().toString()))
				.body(result);
		

	}

}
