package fi.lifesup.hackathon.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import fi.lifesup.hackathon.domain.Challenge;
import fi.lifesup.hackathon.domain.enumeration.ChallengeStatus;
import fi.lifesup.hackathon.repository.ChallengeInfoRepository;
import fi.lifesup.hackathon.repository.ChallengeRepository;
import fi.lifesup.hackathon.searchCriteria.ChallengeSearch;
import fi.lifesup.hackathon.service.ChallengeService;
import fi.lifesup.hackathon.service.dto.ChallengeImageDTO;
import fi.lifesup.hackathon.service.dto.ChallengeTimeDTO;
import fi.lifesup.hackathon.web.rest.util.HeaderUtil;
import fi.lifesup.hackathon.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Challenge.
 */
@RestController
@RequestMapping("/api")
public class ChallengeResource {

	private final Logger log = LoggerFactory.getLogger(ChallengeResource.class);

	@Inject
	private ChallengeRepository challengeRepository;

	@Inject
	private ChallengeService challengeService;

	@Inject
	private ChallengeInfoRepository challengeInfoRepository;

	/**
	 * POST /challenges : Create a new challenge.
	 *
	 * @param challenge
	 *            the challenge to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new challenge, or with status 400 (Bad Request) if the challenge
	 *         has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/challenges")
	@Timed
	public ResponseEntity<Challenge> createChallenge(@Valid @RequestBody Challenge challenge)
			throws URISyntaxException {
		log.debug("REST request to save Challenge : {}", challenge);
		if (challenge.getId() != null) {
			return ResponseEntity.badRequest().headers(
					HeaderUtil.createFailureAlert("challenge", "idexists", "A new challenge cannot already have an ID"))
					.body(null);
		}

		if (challenge.getMinTeamNumber().intValue() >= challenge.getMaxTeamNumber().intValue()) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("challenge", "idexists",
					"Max team number is bigger min team number.")).body(null);
		}

		Challenge result = challengeService.createChallenge(challenge);
		return ResponseEntity.created(new URI("/api/challenges/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("challenge", result.getId().toString())).body(result);
	}

	/**
	 * PUT /challenges : Updates an existing challenge.
	 *
	 * @param challenge
	 *            the challenge to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         challenge, or with status 400 (Bad Request) if the challenge is
	 *         not valid, or with status 500 (Internal Server Error) if the
	 *         challenge couldnt be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/challenges")
	@Timed
	public ResponseEntity<Challenge> updateChallenge(@Valid @RequestBody Challenge challenge)
			throws URISyntaxException {
		log.debug("REST request to update Challenge : {}", challenge);

		if (challenge.getId() == null) {
			return createChallenge(challenge);
		}
		if (challenge.getMinTeamNumber().intValue() >= challenge.getMaxTeamNumber().intValue()) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("challenge", "idexists",
					"Max team number is bigger min team number.")).body(null);
		}
		Challenge result = challengeRepository.save(challenge);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert("challenge", challenge.getId().toString())).body(result);
	}

	/**
	 * GET /challenges : get all the challenges.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         challenges in body
	 * @throws URISyntaxException
	 */
	@GetMapping("/challenges")
	@Timed
	public ResponseEntity<List<Challenge>> getAllChallenges(ChallengeSearch challengeSearch, Pageable pageable)
			throws URISyntaxException {
		Page<Challenge> page = challengeService.getChallengeSearch(challengeSearch, pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/challenges");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);

	}

	/**
	 * GET /challenges/:id : get the "id" challenge.
	 *
	 * @param id
	 *            the id of the challenge to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         challenge, or with status 404 (Not Found)
	 */
	@GetMapping("/challenges/{id}")
	@Timed
	public ResponseEntity<Challenge> getChallenge(@PathVariable Long id) {
		log.debug("REST request to get Challenge : {}", id);

		Challenge challenge = challengeRepository.findOne(id);

		return Optional.ofNullable(challenge).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /challenges/:id : delete the "id" challenge.
	 *
	 * @param id
	 *            the id of the challenge to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/challenges/{id}")
	@Timed
	public ResponseEntity<Void> deleteChallenge(@PathVariable Long id) {
		log.debug("REST request to delete Challenge : {}", id);
		challengeService.deleteChanllenge(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("challenge", id.toString())).build();
	}

	@GetMapping("/challenges-by-user")
	@Timed
	public ResponseEntity<List<Challenge>> getManageChallenges(ChallengeSearch challengeSearch, Pageable pageable)
			throws URISyntaxException {
		Page<Challenge> page = challengeService.getChallengeManageSearch(challengeSearch, pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/challenges-by-user");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);

	}

	@GetMapping("/challenges-by-authories")
	@Timed
	public List<Challenge> getChallengesBasedOnOwner() {
		log.debug("REST request to get all Challenges By Authories");
		List<Challenge> challenges = challengeService.getChallenges();
		return challenges;
	}

	@PutMapping("/challenges/banner")
	@Timed
	public ResponseEntity<Challenge> updateChallengeBanner(@Valid @RequestBody ChallengeImageDTO imageDTO)
			throws URISyntaxException {
		log.debug("REST request to update Challenge banner : {}", imageDTO);

		Challenge result = challengeService.updateChallengeBanner(imageDTO);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert("challenge", imageDTO.getChallengeId().toString()))
				.body(result);
	}

	@PostMapping("/challenges/get-banner-base64")
	@Timed
	public String getChallengesBase64(@RequestBody String id) {
		log.debug("REST request to get all Challenges By Authories");
		String base = challengeService.converbase64(id);
		return base;
	}

//	@Scheduled(fixedRate = 60000)
//	@GetMapping("/challenges/check")
//	@Timed
//	public void checkChallenge() {
//		List<Challenge> challenges = challengeRepository.listChallenges();
//		ZonedDateTime time = ZonedDateTime.now();
//		for (Challenge challenge : challenges) {
//
//			if (challenge.getInfo().getStatus() == ChallengeStatus.ACTIVE
//					&& !challenge.getInfo().getApplicationCloseDate().isAfter(time)) {
//				challenge.getInfo().setStatus(ChallengeStatus.INACTIVE);
//				challengeInfoRepository.save(challenge.getInfo());
//			}
//			if (challenge.getInfo().getStatus() == ChallengeStatus.INACTIVE
//					&& !challenge.getInfo().getEventEndTime().isAfter(time)) {
//				challenge.getInfo().setStatus(ChallengeStatus.CLOSED);
//				challengeInfoRepository.save(challenge.getInfo());
//			}
//		}
//	}

	@GetMapping("/challenges/get-time")
	@Timed
	public ChallengeTimeDTO getChallengeTime() {
		ChallengeTimeDTO challengeTimeDTO = new ChallengeTimeDTO(ZonedDateTime.now());
		return challengeTimeDTO;
	}

	@GetMapping("/challenges-by-aut/{id}")
	@Timed
	public List<Challenge> getChallengesBasedOnOwner(@RequestBody String id) {
		log.debug("REST request to get all Challenges By Authories");
		List<Challenge> challenges = challengeService.getChallenges();
		return challenges;
	}
}
