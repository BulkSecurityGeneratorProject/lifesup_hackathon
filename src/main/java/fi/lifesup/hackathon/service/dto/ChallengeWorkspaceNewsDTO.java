package fi.lifesup.hackathon.service.dto;

import java.time.ZonedDateTime;

import fi.lifesup.hackathon.domain.ChallengeWorkspace;

public class ChallengeWorkspaceNewsDTO {
	 private Long id;
	 private String title;
	 private String content;
	 private ZonedDateTime createdDate;
	 private String createdBy;
	 private Long workspace;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public ZonedDateTime getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(ZonedDateTime createdDate) {
		this.createdDate = createdDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Long getWorkspace() {
		return workspace;
	}
	public void setWorkspace(Long workspace) {
		this.workspace = workspace;
	}
	
	public ChallengeWorkspaceNewsDTO() {
		super();
	}
	public ChallengeWorkspaceNewsDTO(Long id, String title, String content, ZonedDateTime createdDate, String createdBy,
			Long workspace) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.createdDate = createdDate;
		this.createdBy = createdBy;
		this.workspace = workspace;
	}


	 
}
