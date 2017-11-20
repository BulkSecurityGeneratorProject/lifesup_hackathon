package fi.lifesup.hackathon.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fi.lifesup.hackathon.domain.ChallengeWorkspaceAnswer;
import fi.lifesup.hackathon.domain.User;
import fi.lifesup.hackathon.domain.enumeration.WorkspaceAnswerType;
import fi.lifesup.hackathon.repository.ChallengeWorkspaceAnswerRepository;
import fi.lifesup.hackathon.repository.ChallengeWorkspaceQuestionRepository;
import fi.lifesup.hackathon.repository.UserRepository;
import fi.lifesup.hackathon.security.SecurityUtils;
import fi.lifesup.hackathon.service.dto.ChallengeWorkspaceAnswerDTO;
import fi.lifesup.hackathon.service.dto.ChallengeWorkspaceDTO;

import java.time.ZonedDateTime;

import javax.inject.Inject;
import javax.transaction.Transactional;

@Service
@Transactional
public class ChallengeWorkspaceAnswerService {

	private final Logger log = LoggerFactory.getLogger(ChallengeWorkspaceAnswerService.class);
	
	@Inject
    private ChallengeWorkspaceAnswerRepository challengeWorkspaceAnswerRepository;
	
	@Inject
    private UserRepository userReponsitory;
    
    @Inject 
    private UserService userService;
    
    @Inject 
    private ChallengeWorkspaceQuestionRepository challengeWorkspaceQuestionReponsitory;
    
	public ChallengeWorkspaceAnswer saveChallengeWorkspaceAnswer(ChallengeWorkspaceAnswerDTO challengeWorkspaceAnswer){
		ChallengeWorkspaceAnswer answer = new ChallengeWorkspaceAnswer();
        
        if(challengeWorkspaceAnswer.getId() != null){
        	answer.setId(challengeWorkspaceAnswer.getId());
        }
        
        User user = userReponsitory.getUserByAuthority(SecurityUtils.getCurrentUserLogin(), "ROLE_USER");
        if(user != null){
        	answer.setAnswerByType(WorkspaceAnswerType.BY_USER);
        }
        else{
        	answer.setAnswerByType(WorkspaceAnswerType.BY_HOST);
        }
        
        answer.setCreatedBy(SecurityUtils.getCurrentUserLogin());
        answer.setCreatedDate(ZonedDateTime.now());
        answer.setContent(challengeWorkspaceAnswer.getContent());
        answer.setQuestion(challengeWorkspaceQuestionReponsitory.findOne(challengeWorkspaceAnswer.getQuestionId()));
		
        return challengeWorkspaceAnswerRepository.save(answer);
	}
}
