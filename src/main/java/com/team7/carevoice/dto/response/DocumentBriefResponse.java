package com.team7.carevoice.dto.response;

import java.time.LocalDateTime;

public class DocumentBriefResponse {
	private Long id;
	private String type;
	private LocalDateTime createdTime;

	public DocumentBriefResponse(Long id, String type, LocalDateTime createdTime) {
		this.id = id;
		this.type = type;
		this.createdTime = createdTime;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}

	public Long getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public LocalDateTime getCreatedTime() {
		return createdTime;
	}

}
