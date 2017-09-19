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

import fi.lifesup.hackathon.domain.Experience;
import fi.lifesup.hackathon.repository.ExperienceRepository;
import fi.lifesup.hackathon.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Experience.
 */
@RestController
@RequestMapping("/api")
public class ExperienceResource {

	private final Logger log = LoggerFactory.getLogger(ExperienceResource.class);

	@Inject
	private ExperienceRepository experienceRepository;

	/**
	 * POST /experiences : Create a new Experience.
	 *
	 * @param Experience
	 *            the Experience to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new Experience, or with status 400 (Bad Request) if the Experience has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/experiences")
	@Timed
	public ResponseEntity<Experience> createExperience(@Valid @RequestBody Experience experience) throws URISyntaxException {
		log.debug("REST request to save Experience : {}", experience);
		if (experience.getId() != null) {
			return ResponseEntity.badRequest().headers(
					HeaderUtil.createFailureAlert("Experience", "idexists", "A new Experience cannot already have an ID"))
					.body(null);
		}
		Experience result = experienceRepository.save(experience);
		return ResponseEntity.created(new URI("/api/experiences/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("Experience", result.getId().toString())).body(result);
	}

	/**
	 * PUT /experiences : Updates an existing Experience.
	 *
	 * @param Experience
	 *            the Experience to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         Experience, or with status 400 (Bad Request) if the Experience is not
	 *         valid, or with status 500 (Internal Server Error) if the Experience
	 *         couldnt be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/experiences")
	@Timed
	public ResponseEntity<Experience> updateExperience(@Valid @RequestBody Experience experience) throws URISyntaxException {
		log.debug("REST request to update Experience : {}", experience);
		if (experience.getId() == null) {
			return createExperience(experience);
		}
		Experience result = experienceRepository.save(experience);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("Experience", experience.getId().toString()))
				.body(result);
	}

	/**
	 * GET /experiences : get all the experiences.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of experiences
	 *         in body
	 */
	@GetMapping("/experiences")
	@Timed
	public List<Experience> getAllexperiences() {
		log.debug("REST request to get all experiences");
		List<Experience> experiences = experienceRepository.findAll();
		return experiences;
	}

	/**
	 * GET /experiences/:id : get the "id" Experience.
	 *
	 * @param id
	 *            the id of the Experience to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         Experience, or with status 404 (Not Found)
	 */
	@GetMapping("/experiences/{id}")
	@Timed
	public ResponseEntity<Experience> getExperience(@PathVariable Long id) {
		log.debug("REST request to get Experience : {}", id);
		Experience experience = experienceRepository.findOne(id);
		return Optional.ofNullable(experience).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /experiences/:id : delete the "id" Experience.
	 *
	 * @param id
	 *            the id of the Experience to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/experiences/{id}")
	@Timed
	public ResponseEntity<Void> deleteExperience(@PathVariable Long id) {
		log.debug("REST request to delete Experience : {}", id);
		experienceRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("Experience", id.toString())).build();
	}

}
