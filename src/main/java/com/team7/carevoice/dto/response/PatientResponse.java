package com.team7.carevoice.dto.response;

public class PatientResponse {
	private Long patientId;

	public PatientResponse(Long patientId) {
		this.patientId = patientId;
	}

	public void setPatientId(Long id) {
		this.patientId = id;
	}

	public Long getPatientId() {
		return patientId;
	}
}
