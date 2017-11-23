package fi.lifesup.hackathon.service;

import java.io.File;
import java.net.URISyntaxException;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import fi.lifesup.hackathon.domain.*;
import fi.lifesup.hackathon.repository.*;
import fi.lifesup.hackathon.service.dto.*;
import fi.lifesup.hackathon.web.rest.ChallengeInfoResource;

@Service
@Transactional
public class ChallengeSubmissionService {
	private final Logger log = LoggerFactory.getLogger(ChallengeService.class);

	@Value("${attach.path}")
	private String filePath;
	@Inject
	private ChallengeWorkspaceRepository challengeWorkspaceRepository;
	@Inject
	private ApplicationRepository applicationRepository;
	@Inject
	private ChallengeInfoRepository challengeInfoRepository;
	@Inject
	private ChallengeRepository challengeRepository;
	@Inject
	private ChallengeSubmissionRepository challengeSubmissionRepository;
	@Inject
	private UserService userService;

	public String testUpload(MultipartFile file) throws URISyntaxException {
		if (!file.isEmpty()) {
			try {
				String realPathtoUploads = "D://";
				if (!new File(realPathtoUploads).exists()) {
					new File(realPathtoUploads).mkdir();
				}

				log.info("realPathtoUploads = {}", realPathtoUploads);

				String orgName = file.getOriginalFilename();
				String filePath = realPathtoUploads + orgName;
				File dest = new File(filePath);
				file.transferTo(dest);
				return filePath;
			} catch (Exception e) {
				return null;
			}
		} else {
			return null;
		}
	}
	public ChallengeSubmission convertDTOToEntity(ChallengeSubmissionDTO challengeSubmissionDTO )
	{
		ChallengeSubmission challengeSubmission=new ChallengeSubmission();
		Application application=applicationRepository.findOne(challengeSubmissionDTO.getApplicationId());
		ChallengeWorkspace challengeWorkspace=challengeWorkspaceRepository.findOne(challengeSubmissionDTO.getWorkspace());
		challengeSubmission.setApplicationId(application.getId());
		challengeSubmission.setWorkspace(challengeWorkspace);
		return challengeSubmission;
	}

public ChallengeSubmission createChallengeSubmission(ChallengeSubmissionDTO challengeSubmissionDTO) throws URISyntaxException
{
	ChallengeSubmission challengeSubmission=new ChallengeSubmission();
	Application application=applicationRepository.findOne(challengeSubmissionDTO.getApplicationId());
	Challenge challenge=challengeRepository.findOne(application.getChallenge().getId());
	ChallengeInfo challengeInfo=challengeInfoRepository.findOne(challenge.getInfo().getId());
	//String filePath=testUpload(challengeSubmissionDTO.getMultipartFile());
	
	if(challengeSubmissionDTO.getModifiedDate().isBefore(challengeInfo.getEventEndTime()))
	{
		ChallengeWorkspace challengeWorkspace=challengeWorkspaceRepository.findOne(challengeSubmissionDTO.getWorkspace());
		if(challengeWorkspace!=null)
		{
			challengeSubmission.setAdditionalNote(challengeSubmission.getAdditionalNote());
			challengeSubmission.setApplicationId(challengeSubmissionDTO.getApplicationId());
			challengeSubmission.setModifiedBy(userService.getCurrentUser().getLastName());
			challengeSubmission.setModifiedDate(challengeSubmissionDTO.getModifiedDate());
			challengeSubmission.setWorkspace(convertDTOToEntity(challengeSubmissionDTO).getWorkspace());
			//challengeSubmission.setFilePath(filePath);
			return challengeSubmissionRepository.save(challengeSubmission);
		}
		return null;
	}
	return null;
}}
