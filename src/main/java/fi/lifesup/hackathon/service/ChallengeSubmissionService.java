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
import org.springframework.stereotype.Service;

import fi.lifesup.hackathon.domain.Application;
import fi.lifesup.hackathon.domain.ChallengeSubmission;
import fi.lifesup.hackathon.domain.ChallengeWorkspace;
import fi.lifesup.hackathon.repository.ApplicationRepository;
import fi.lifesup.hackathon.repository.ChallengeInfoRepository;
import fi.lifesup.hackathon.repository.ChallengeRepository;
import fi.lifesup.hackathon.repository.ChallengeSubmissionRepository;
import fi.lifesup.hackathon.repository.ChallengeWorkspaceRepository;
import fi.lifesup.hackathon.security.SecurityUtils;
import fi.lifesup.hackathon.service.dto.ChallengeSubmissionDTO;
import fi.lifesup.hackathon.service.dto.ChallengeSubmissionFileDTO;

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
		if (!dto.getFile().isEmpty()) {
			try {

				String realPathtoUploads = attachPath + "/challenge/" + "challenge_"
						+ challengeSubmission.getWorkspace().getChallenge().getId() + "/ChallengeSubmission/";
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


	public ChallengeSubmission createChallengeSubmission(ChallengeSubmissionDTO challengeSubmissionDTO)
			throws URISyntaxException {
		ChallengeSubmission challengeSubmission = new ChallengeSubmission();
		ChallengeWorkspace challengeWorkspace = challengeWorkspaceRepository
				.findOne(challengeSubmissionDTO.getWorkspaceId());
		if (challengeWorkspace != null) {
			if (challengeSubmissionDTO.getId() != null) {
				challengeSubmission.setId(challengeSubmissionDTO.getId());
			}
			challengeSubmission.setAdditionalNote(challengeSubmissionDTO.getAdditionalNote());
			challengeSubmission.setApplicationId(challengeSubmissionDTO.getApplicationId());
			challengeSubmission.setModifiedBy(SecurityUtils.getCurrentUserLogin());
			challengeSubmission.setModifiedDate(ZonedDateTime.now());
			challengeSubmission.setWorkspace(challengeWorkspace);
			return challengeSubmission;
		}
		return null;
	}

}
