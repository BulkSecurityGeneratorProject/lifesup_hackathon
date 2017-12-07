package fi.lifesup.hackathon.web.rest;

import java.io.File;
import java.io.FileInputStream;
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
import org.springframework.security.crypto.codec.Base64;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import fi.lifesup.hackathon.domain.ChallengeSubmission;
import fi.lifesup.hackathon.repository.ChallengeSubmissionRepository;
import fi.lifesup.hackathon.service.ChallengeSubmissionService;
import fi.lifesup.hackathon.service.dto.ChallengeSubmissionDTO;
import fi.lifesup.hackathon.service.dto.ChallengeSubmissionFileDTO;
import fi.lifesup.hackathon.web.rest.util.HeaderUtil;

/**
 * REST controller for managing ChallengeSubmission.
 */
@RestController
@RequestMapping("/api")
public class ChallengeSubmissionResource {

	private final Logger log = LoggerFactory.getLogger(ChallengeSubmissionResource.class);

	@Inject
	private ChallengeSubmissionRepository challengeSubmissionRepository;
	@Inject
	private ChallengeSubmissionService challengeSubmissionService;

	/**
	 * POST /challenge-submissions : Create a new challengeSubmission.
	 *
	 * @param challengeSubmission
	 *            the challengeSubmission to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new challengeSubmission, or with status 400 (Bad Request) if the
	 *         challengeSubmission has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/challenge-submissions")
	@Timed
	public ResponseEntity<ChallengeSubmission> createChallengeSubmission(
			@Valid @RequestBody ChallengeSubmission challengeSubmission) throws URISyntaxException {
		log.debug("REST request to save ChallengeSubmission : {}", challengeSubmission);
		if (challengeSubmission.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("challengeSubmission", "idexists",
					"A new challengeSubmission cannot already have an ID")).body(null);
		}
		ChallengeSubmission result = challengeSubmissionRepository.save(challengeSubmission);
		return ResponseEntity.created(new URI("/api/challenge-submissions/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("challengeSubmission", result.getId().toString()))
				.body(result);
	}

	@PostMapping(value="/challenge-submissions-created", consumes = "multipart/form-data")
	@Timed
	public ResponseEntity<ChallengeSubmission> createdChallengeSubmission(
			ChallengeSubmissionDTO challengeSubmissionDTO) throws URISyntaxException {
		log.debug("REST request to save ChallengeSubmission : {}", challengeSubmissionDTO);
		if (challengeSubmissionDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("challengeSubmission", "idexists",
					"A new challengeSubmission cannot already have an ID")).body(null);
		}
		ChallengeSubmission challengeSubmission = challengeSubmissionService
				.createChallengeSubmission(challengeSubmissionDTO);
		if (challengeSubmission == null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("challengeSubmission", "idexists",
					"A new challengeSubmission cannot already have an ID")).body(null);
		}
		ChallengeSubmission result = challengeSubmissionRepository.save(challengeSubmission);
		if(challengeSubmissionDTO.getMultipartFile() != null){
			ChallengeSubmissionFileDTO fileDTO = new ChallengeSubmissionFileDTO();
			fileDTO.setChallengeSubmissionId(result.getId());
			fileDTO.setFile(challengeSubmissionDTO.getMultipartFile());
			challengeSubmissionService.testUpload(fileDTO);
		}		
		return ResponseEntity.created(new URI("/api/challenge-submissions-created/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("challengeSubmission", result.getId().toString()))
				.body(result);

	}

	/**
	 * PUT /challenge-submissions : Updates an existing challengeSubmission.
	 *
	 * @param challengeSubmission
	 *            the challengeSubmission to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         challengeSubmission, or with status 400 (Bad Request) if the
	 *         challengeSubmission is not valid, or with status 500 (Internal
	 *         Server Error) if the challengeSubmission couldnt be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/challenge-submissions-created")
	@Timed
	public ResponseEntity<ChallengeSubmission> updateChallengeSubmission(
			@Valid @RequestBody ChallengeSubmissionDTO challengeSubmissionDTO) throws URISyntaxException {
		log.debug("REST request to update ChallengeSubmission : {}", challengeSubmissionDTO);
		if (challengeSubmissionDTO.getId() == null) {
			return createdChallengeSubmission(challengeSubmissionDTO);
		}
		ChallengeSubmission challengeSubmission = challengeSubmissionService
				.createChallengeSubmission(challengeSubmissionDTO);
		if (challengeSubmission == null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("challengeSubmission", "idexists",
					"A new challengeSubmission cannot already have an ID")).body(null);
		}
		ChallengeSubmission result = challengeSubmissionRepository.save(challengeSubmission);
		if(challengeSubmissionDTO.getMultipartFile() != null){
			ChallengeSubmissionFileDTO fileDTO = new ChallengeSubmissionFileDTO();
			fileDTO.setChallengeSubmissionId(result.getId());
			fileDTO.setFile(challengeSubmissionDTO.getMultipartFile());
			challengeSubmissionService.testUpload(fileDTO);
		}
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert("challengeSubmission", challengeSubmission.getId().toString()))
				.body(result);
	}

	@PutMapping("/challenge-submissions")
	@Timed
	public ResponseEntity<ChallengeSubmission> updateChallengeSubmission(
			@Valid @RequestBody ChallengeSubmission challengeSubmission) throws URISyntaxException {
		log.debug("REST request to update ChallengeSubmission : {}", challengeSubmission);
		if (challengeSubmission.getId() == null) {
			return createChallengeSubmission(challengeSubmission);
		}
		ChallengeSubmission result = challengeSubmissionRepository.save(challengeSubmission);
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert("challengeSubmission", challengeSubmission.getId().toString()))
				.body(result);
	}
	/**
	 * GET /challenge-submissions : get all the challengeSubmissions.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         challengeSubmissions in body
	 */
	@GetMapping("/challenge-submissions")
	@Timed
	public List<ChallengeSubmission> getAllChallengeSubmissions() {
		log.debug("REST request to get all ChallengeSubmissions");
		List<ChallengeSubmission> challengeSubmissions = challengeSubmissionRepository.findAll();
		return challengeSubmissions;
	}

	/**
	 * GET /challenge-submissions/:id : get the "id" challengeSubmission.
	 *
	 * @param id
	 *            the id of the challengeSubmission to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         challengeSubmission, or with status 404 (Not Found)
	 */
	@GetMapping("/challenge-submissions/{id}")
	@Timed
	public ResponseEntity<ChallengeSubmission> getChallengeSubmission(@PathVariable Long id) {
		log.debug("REST request to get ChallengeSubmission : {}", id);
		ChallengeSubmission challengeSubmission = challengeSubmissionRepository.findOne(id);
		return Optional.ofNullable(challengeSubmission).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /challenge-submissions/:id : delete the "id" challengeSubmission.
	 *
	 * @param id
	 *            the id of the challengeSubmission to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/challenge-submissions/{id}")
	@Timed
	public ResponseEntity<Void> deleteChallengeSubmission(@PathVariable Long id) {
		log.debug("REST request to delete ChallengeSubmission : {}", id);
		challengeSubmissionRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("challengeSubmission", id.toString()))
				.build();
	}

	@PostMapping(value = "/upload-test", consumes = "multipart/form-data")
	@Timed
	public ResponseEntity<ChallengeSubmission> updateSubmissionFile(ChallengeSubmissionFileDTO fileDTO)
			throws URISyntaxException {
		ChallengeSubmission result = challengeSubmissionService.testUpload(fileDTO);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert("challengesubmission", result.getId().toString()))
				.body(result);
	}
	
	@GetMapping("/challenge-submissions/application/{applicationId}")
	@Timed
	public List<ChallengeSubmission> getChallengeSubmissionApplication(@PathVariable Long applicationId) {
		log.debug("REST request to get ChallengeSubmission : {}", applicationId);
		List<ChallengeSubmission> challengeSubmissions = challengeSubmissionRepository.findByApplicationId(applicationId);
		return challengeSubmissions;
	}
	
	@PostMapping("/challenge-submissions/download")
	@Timed
	public ChallengeSubmissionFileDTO downloadSubmissionFile(@RequestBody String filePath)
			throws URISyntaxException {
		ChallengeSubmissionFileDTO outPut = new ChallengeSubmissionFileDTO();
    	FileInputStream stream = null;
    	try{
    	File file = new File(filePath);	
    	stream = new FileInputStream(file);
    	byte[] bytes = new byte[(int)file.length()];
    	stream.read(bytes);
    	String base64Content = new String(Base64.encode(bytes));   
    	outPut.setBase64(base64Content);
    	outPut.setFileName(file.getName());
    	}catch(Exception e){
    	}
    	return outPut;
	}

}
