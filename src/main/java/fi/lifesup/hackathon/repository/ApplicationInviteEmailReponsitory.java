package fi.lifesup.hackathon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fi.lifesup.hackathon.domain.ApplicationInviteEmail;
import fi.lifesup.hackathon.service.dto.ApplicationMemberDTO;

@SuppressWarnings("unused")
public interface ApplicationInviteEmailReponsitory extends JpaRepository<ApplicationInviteEmail, Long> {

	ApplicationInviteEmail findByAcceptKey(String acceptKey);
	
	void deleteByAcceptKey(String acceptKey);
	
	
	List<ApplicationInviteEmail> findByApplicationId(Long applicationId);
}
