package fi.lifesup.hackathon.service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;

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
import fi.lifesup.hackathon.security.SecurityUtils;
import fi.lifesup.hackathon.service.dto.*;
import fi.lifesup.hackathon.web.rest.ChallengeInfoResource;
import io.undertow.util.FileUtils;

@Service
@Transactional
public class ChallengeSubmissionService {
	private final Logger log = LoggerFactory.getLogger(ChallengeService.class);

	@Value("${attach.path}")
	private String attachPath;
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

	@Transactional
	public ChallengeSubmission testUpload(ChallengeSubmissionFileDTO dto) throws URISyntaxException {
		ChallengeSubmission challengeSubmission = challengeSubmissionRepository.findOne(dto.getChallengeSubmissionId());
		String filePath = null;
		System.err.println(dto.getFile());
		if (!dto.getFile().isEmpty()) {
			try {
				
				String realPathtoUploads = attachPath + "/challenge/" + "challenge_" + challengeSubmission.getWorkspace().getChallenge().getId() 
						+ "/ChallengeSubmission/";
				if (!new File(realPathtoUploads).exists()) {
					new File(realPathtoUploads).mkdir();
				}

				log.info("realPathtoUploads = {}", realPathtoUploads);
				String orgName = dto.getFile().getOriginalFilename();
				
				filePath = realPathtoUploads + orgName;
				try {
					File file = new File(filePath);
					org.apache.commons.io.FileUtils.writeByteArrayToFile(file, dto.getFile().getBytes());
					challengeSubmission.setFilePath(filePath);
					return challengeSubmissionRepository.save(challengeSubmission);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				

			} catch (Exception e) {
				e.printStackTrace();

			}
		} else {
			return null;
		}
		return null;
	}

	public static boolean createFile(String path, byte[] data) {
		try {
			File file = new File(path);
			org.apache.commons.io.FileUtils.writeByteArrayToFile(file, data);
		} catch (IOException e) {
			return false;
		}

		return true;
	}

	public ChallengeSubmission convertDTOToEntity(ChallengeSubmissionDTO challengeSubmissionDTO) {
		ChallengeSubmission challengeSubmission = new ChallengeSubmission();
		Application application = applicationRepository.findOne(challengeSubmissionDTO.getApplicationId());
		ChallengeWorkspace challengeWorkspace = challengeWorkspaceRepository
				.findOne(challengeSubmissionDTO.getWorkspaceId());
		challengeSubmission.setApplicationId(application.getId());
		challengeSubmission.setWorkspace(challengeWorkspace);
		return challengeSubmission;
	}

	// public ChallengeSubmission
	// createChallengeSubmission(ChallengeSubmissionDTO challengeSubmissionDTO)
	// throws URISyntaxException {
	// ChallengeSubmission challengeSubmission = new ChallengeSubmission();
	// Application application =
	// applicationRepository.findOne(challengeSubmissionDTO.getApplicationId());
	// Challenge challenge =
	// challengeRepository.findOne(application.getChallenge().getId());
	// ChallengeInfo challengeInfo =
	// challengeInfoRepository.findOne(challenge.getInfo().getId());
	// // String
	// // filePath=testUpload(challengeSubmissionDTO.getMultipartFile());
	//
	// if
	// (challengeSubmissionDTO.getModifiedDate().isBefore(challengeInfo.getEventEndTime()))
	// {
	// ChallengeWorkspace challengeWorkspace = challengeWorkspaceRepository
	// .findOne(challengeSubmissionDTO.getWorkspace());
	// if (challengeWorkspace != null) {
	// challengeSubmission.setAdditionalNote(challengeSubmission.getAdditionalNote());
	// challengeSubmission.setApplicationId(challengeSubmissionDTO.getApplicationId());
	// challengeSubmission.setModifiedBy(userService.getCurrentUser().getLastName());
	// challengeSubmission.setModifiedDate(challengeSubmissionDTO.getModifiedDate());
	// challengeSubmission.setWorkspace(convertDTOToEntity(challengeSubmissionDTO).getWorkspace());
	// // challengeSubmission.setFilePath(filePath);
	// return challengeSubmissionRepository.save(challengeSubmission);
	// }
	// return null;
	// }
	// return null;
	// }

	public ChallengeSubmission createChallengeSubmission(ChallengeSubmissionDTO challengeSubmissionDTO)
			throws URISyntaxException {
		ChallengeSubmission challengeSubmission = new ChallengeSubmission();
		ChallengeWorkspace challengeWorkspace = challengeWorkspaceRepository
				.findOne(challengeSubmissionDTO.getWorkspaceId());
		if (challengeWorkspace != null) {
			if (challengeSubmissionDTO.getId() != null) {
				challengeSubmission.setId(challengeSubmissionDTO.getId());
			}
			challengeSubmission.setAdditionalNote(challengeSubmission.getAdditionalNote());
			challengeSubmission.setApplicationId(challengeSubmissionDTO.getApplicationId());
			challengeSubmission.setModifiedBy(SecurityUtils.getCurrentUserLogin());
			challengeSubmission.setModifiedDate(ZonedDateTime.now());
			challengeSubmission.setWorkspace(challengeWorkspace);
			// challengeSubmission.setFilePath(filePath);
			return challengeSubmission;
		}
		return null;
	}

}
