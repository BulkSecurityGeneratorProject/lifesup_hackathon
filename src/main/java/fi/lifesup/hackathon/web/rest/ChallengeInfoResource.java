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

import fi.lifesup.hackathon.domain.ChallengeInfo;
import fi.lifesup.hackathon.repository.ChallengeInfoRepository;
import fi.lifesup.hackathon.web.rest.util.HeaderUtil;

/**
 * REST controller for managing ChallengeInfo.
 */
@RestController
@RequestMapping("/api")
public class ChallengeInfoResource {

    private final Logger log = LoggerFactory.getLogger(ChallengeInfoResource.class);
        
    @Inject
    private ChallengeInfoRepository challengeInfoRepository;

    /**
     * POST  /challenge-infos : Create a new challengeInfo.
     *
     * @param challengeInfo the challengeInfo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new challengeInfo, or with status 400 (Bad Request) if the challengeInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/challenge-infos")
    @Timed
    public ResponseEntity<ChallengeInfo> createChallengeInfo(@Valid @RequestBody ChallengeInfo challengeInfo) throws URISyntaxException {
        log.debug("REST request to save ChallengeInfo : {}", challengeInfo);
        if (challengeInfo.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("challengeInfo", "idexists", "A new challengeInfo cannot already have an ID")).body(null);
        }
  
        ChallengeInfo result = challengeInfoRepository.save(challengeInfo);
        return ResponseEntity.created(new URI("/api/challenge-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("challengeInfo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /challenge-infos : Updates an existing challengeInfo.
     *
     * @param challengeInfo the challengeInfo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated challengeInfo,
     * or with status 400 (Bad Request) if the challengeInfo is not valid,
     * or with status 500 (Internal Server Error) if the challengeInfo couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/challenge-infos")
    @Timed
    public ResponseEntity<ChallengeInfo> updateChallengeInfo(@Valid @RequestBody ChallengeInfo challengeInfo) throws URISyntaxException {
        log.debug("REST request to update ChallengeInfo : {}", challengeInfo);
        if (challengeInfo.getId() == null) {
            return createChallengeInfo(challengeInfo);
        }
        
        ChallengeInfo result = challengeInfoRepository.save(challengeInfo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("challengeInfo", challengeInfo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /challenge-infos : get all the challengeInfos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of challengeInfos in body
     */
    @GetMapping("/challenge-infos")
    @Timed
    public List<ChallengeInfo> getAllChallengeInfos() {
        log.debug("REST request to get all ChallengeInfos");
        List<ChallengeInfo> challengeInfos = challengeInfoRepository.findAll();
        return challengeInfos;
    }

    /**
     * GET  /challenge-infos/:id : get the "id" challengeInfo.
     *
     * @param id the id of the challengeInfo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the challengeInfo, or with status 404 (Not Found)
     */
    @GetMapping("/challenge-infos/{id}")
    @Timed
    public ResponseEntity<ChallengeInfo> getChallengeInfo(@PathVariable Long id) {
        log.debug("REST request to get ChallengeInfo : {}", id);
        ChallengeInfo challengeInfo = challengeInfoRepository.findOne(id);       
        return Optional.ofNullable(challengeInfo)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /challenge-infos/:id : delete the "id" challengeInfo.
     *
     * @param id the id of the challengeInfo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/challenge-infos/{id}")
    @Timed
    public ResponseEntity<Void> deleteChallengeInfo(@PathVariable Long id) {
        log.debug("REST request to delete ChallengeInfo : {}", id);
        challengeInfoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("challengeInfo", id.toString())).build();
    }

}
