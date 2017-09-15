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

import fi.lifesup.hackathon.domain.Skill;
import fi.lifesup.hackathon.repository.SkillRepository;
import fi.lifesup.hackathon.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Skill.
 */
@RestController
@RequestMapping("/api")
public class SkillResource {

	private final Logger log = LoggerFactory.getLogger(SkillResource.class);

	@Inject
	private SkillRepository skillRepository;

	/**
	 * POST /skills : Create a new Skill.
	 *
	 * @param Skill
	 *            the Skill to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new Skill, or with status 400 (Bad Request) if the Skill has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/skills")
	@Timed
	public ResponseEntity<Skill> createSkill(@Valid @RequestBody Skill Skill) throws URISyntaxException {
		log.debug("REST request to save Skill : {}", Skill);
		if (Skill.getId() != null) {
			return ResponseEntity.badRequest().headers(
					HeaderUtil.createFailureAlert("Skill", "idexists", "A new Skill cannot already have an ID"))
					.body(null);
		}
		Skill result = skillRepository.save(Skill);
		return ResponseEntity.created(new URI("/api/skills/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("Skill", result.getId().toString())).body(result);
	}

	/**
	 * PUT /skills : Updates an existing Skill.
	 *
	 * @param Skill
	 *            the Skill to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         Skill, or with status 400 (Bad Request) if the Skill is not
	 *         valid, or with status 500 (Internal Server Error) if the Skill
	 *         couldnt be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/skills")
	@Timed
	public ResponseEntity<Skill> updateSkill(@Valid @RequestBody Skill Skill) throws URISyntaxException {
		log.debug("REST request to update Skill : {}", Skill);
		if (Skill.getId() == null) {
			return createSkill(Skill);
		}
		Skill result = skillRepository.save(Skill);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("Skill", Skill.getId().toString()))
				.body(result);
	}

	/**
	 * GET /skills : get all the skills.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of skills
	 *         in body
	 */
	@GetMapping("/skills")
	@Timed
	public List<Skill> getAllskills() {
		log.debug("REST request to get all Skills");
		List<Skill> skills = skillRepository.findAll();
		return skills;
	}

	/**
	 * GET /skills/:id : get the "id" Skill.
	 *
	 * @param id
	 *            the id of the Skill to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         Skill, or with status 404 (Not Found)
	 */
	@GetMapping("/skills/{id}")
	@Timed
	public ResponseEntity<Skill> getSkill(@PathVariable Long id) {
		log.debug("REST request to get Skill : {}", id);
		Skill Skill = skillRepository.findOne(id);
		return Optional.ofNullable(Skill).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /skills/:id : delete the "id" Skill.
	 *
	 * @param id
	 *            the id of the Skill to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/skills/{id}")
	@Timed
	public ResponseEntity<Void> deleteSkill(@PathVariable Long id) {
		log.debug("REST request to delete Skill : {}", id);
		skillRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("Skill", id.toString())).build();
	}

}
