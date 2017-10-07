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

import fi.lifesup.hackathon.domain.Application;
import fi.lifesup.hackathon.domain.enumeration.ApplicationStatus;
import fi.lifesup.hackathon.repository.ApplicationRepository;
import fi.lifesup.hackathon.service.ApplicationService;
import fi.lifesup.hackathon.service.dto.ApplicationDTO;
import fi.lifesup.hackathon.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Application.
 */
@RestController
@RequestMapping("/api")
public class ApplicationResource {

    private final Logger log = LoggerFactory.getLogger(ApplicationResource.class);
        
    @Inject
    private ApplicationRepository applicationRepository;

    @Inject
    private ApplicationService applicationService;
    /**
     * POST  /applications : Create a new application.
     *
     * @param application the application to create
     * @return the ResponseEntity with status 201 (Created) and with body the new application, or with status 400 (Bad Request) if the application has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/applications")
    @Timed
    public ResponseEntity<Application> createApplication(@Valid @RequestBody ApplicationDTO application) throws URISyntaxException {
        log.debug("REST request to save Application : {}", application);
        if (application.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("application", "idexists", "A new application cannot already have an ID")).body(null);
        }
        Application result = applicationService.createApplication(application);
        return ResponseEntity.created(new URI("/api/applications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("application", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /applications : Updates an existing application.
     *
     * @param application the application to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated application,
     * or with status 400 (Bad Request) if the application is not valid,
     * or with status 500 (Internal Server Error) if the application couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/applications")
    @Timed
    public ResponseEntity<Application> updateApplication(@Valid @RequestBody ApplicationDTO application) throws URISyntaxException {
        log.debug("REST request to update Application : {}", application);
        if (application.getId() == null) {
            return createApplication(application);
        }
        Application result = applicationService.updateApplication(application);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("application", result.getId().toString()))
            .body(result);
    }

    /**
     * GET  /applications : get all the applications.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of applications in body
     */
    @GetMapping("/applications")
    @Timed
    public List<Application> getAllApplications() {
        log.debug("REST request to get all Applications");
        List<Application> applications = applicationRepository.findAll();
        return applications;
    }

    /**
     * GET  /applications/:id : get the "id" application.
     *
     * @param id the id of the application to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the application, or with status 404 (Not Found)
     */
    @GetMapping("/applications/{id}")
    @Timed
    public ResponseEntity<Application> getApplication(@PathVariable Long id) {
        log.debug("REST request to get Application : {}", id);
        Application application = applicationRepository.findOne(id);
        return Optional.ofNullable(application)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /applications/:id : delete the "id" application.
     *
     * @param id the id of the application to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/applications/{id}")
    @Timed
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        log.debug("REST request to delete Application : {}", id);
        applicationService.deleteApplication(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("application", id.toString())).build();
    }
    
    @GetMapping("/applications/challenges/{challengeId}")
    @Timed
    public List<Application> getApplicationByChallenge(@PathVariable Long challengeId) {
        log.debug("REST request to get Application by challengeId : {}", challengeId);
        List<Application> applications = applicationRepository.findByChallengeId(challengeId);
        return applications;
    }
    
    @GetMapping("/applications/details/{applicationId}")
    @Timed
    public ApplicationDTO getApplicationDetail(@PathVariable Long applicationId) {
        log.debug("REST request to get Application by challengeId : {}", applicationId);
        ApplicationDTO application = applicationService.getApplicationDetail(applicationId);
        return application;
    }
    
    @PutMapping("/applications/status")
    @Timed
    public ResponseEntity<Application> updateApplicationStatus(@RequestBody ApplicationDTO applicationDTO) throws URISyntaxException {
        log.debug("REST request to update Application : {}", applicationDTO);
        Application application = applicationRepository.findOne(applicationDTO.getId());
        application.setStatus(applicationDTO.getStatus());
        Application result = applicationRepository.save(application);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("application", result.getId().toString()))
            .body(result);
    }
    

    @GetMapping("/applications/check/{id}")
    @Timed
    public Boolean[] getCheckApplication(@PathVariable Long id) {
        log.debug("REST request to get Application by acceptKey : {}", id);
        
        return applicationService.checkApplication(id);
    }
}
