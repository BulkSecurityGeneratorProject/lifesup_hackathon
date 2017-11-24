package fi.lifesup.hackathon.service;

import java.time.ZonedDateTime;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import fi.lifesup.hackathon.domain.*;
import fi.lifesup.hackathon.repository.*;
import fi.lifesup.hackathon.security.SecurityUtils;
import fi.lifesup.hackathon.service.dto.*;

@Service
@Transactional
public class ChallengeWorkspaceNewsService {
	private final Logger log = LoggerFactory.getLogger(ApplicationService.class);

	@Inject
	private ChallengeWorkspaceRepository challengeWorkspaceRepository;
	@Inject
	private ChallengeWorkspaceNewsRepository challengeWorkspaceNewsRepository;
	@Inject
	private UserService userService;

	public ChallengeWorkspaceNews convertDTOToentity(ChallengeWorkspaceNewsDTO challengeWorkspaceNewsDTO) {
		ChallengeWorkspaceNews challengeWorkspaceNews = new ChallengeWorkspaceNews();
		ChallengeWorkspace challengeWorkspace = challengeWorkspaceRepository
				.findOne(challengeWorkspaceNewsDTO.getWorkspaceId());
		challengeWorkspaceNews.setWorkspace(challengeWorkspace);
		return challengeWorkspaceNews;
	}

	public ChallengeWorkspaceNews createChallengeWorkspaceNews(ChallengeWorkspaceNewsDTO challengeWorkspaceNewsDTO) {
		ChallengeWorkspaceNews challengeWorkspaceNews = new ChallengeWorkspaceNews();
		ChallengeWorkspace challengeWorkspace = challengeWorkspaceRepository
				.findOne(challengeWorkspaceNewsDTO.getWorkspaceId());
		if (challengeWorkspace == null) {
			return null;
		} else {
			if (challengeWorkspaceNewsDTO.getId() != null) {
				challengeWorkspaceNews.setId(challengeWorkspaceNewsDTO.getId());
			}
			challengeWorkspaceNews.setWorkspace(convertDTOToentity(challengeWorkspaceNewsDTO).getWorkspace());
			challengeWorkspaceNews.setContent(challengeWorkspaceNewsDTO.getContent());
			challengeWorkspaceNews.setCreatedBy(SecurityUtils.getCurrentUserLogin());
			challengeWorkspaceNews.setCreatedDate(ZonedDateTime.now());
			challengeWorkspaceNews.setTitle(challengeWorkspaceNewsDTO.getTitle());
			return challengeWorkspaceNews;
		}
	}
}
