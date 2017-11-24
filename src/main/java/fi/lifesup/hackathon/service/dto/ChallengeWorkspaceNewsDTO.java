package fi.lifesup.hackathon.service.dto;

import java.time.ZonedDateTime;

import fi.lifesup.hackathon.domain.ChallengeWorkspaceNews;

public class ChallengeWorkspaceNewsDTO {
	 private Long id;
	 private String title;
	 private String content;
	 private ZonedDateTime createdDate;
	 private String createdBy;
	 private Long workspaceId;
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
	public Long getWorkspaceId() {
		return workspaceId;
	}
	public void setWorkspaceId(Long workspace) {
		this.workspaceId = workspace;
	}
	
	public ChallengeWorkspaceNewsDTO() {
		super();
	}
	public ChallengeWorkspaceNewsDTO(Long id, String title, String content, ZonedDateTime createdDate, String createdBy,
			Long workspaceId) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.createdDate = createdDate;
		this.createdBy = createdBy;
		this.workspaceId = workspaceId;
	}
	
	public ChallengeWorkspaceNewsDTO(ChallengeWorkspaceNews news) {
		this.id = news.getId();
		this.title = news.getTitle();
		this.content = news.getContent();
		this.createdDate = news.getCreatedDate();
		this.createdBy = news.getCreatedBy();
		this.workspaceId = news.getWorkspace().getId();
	}


	 
}
