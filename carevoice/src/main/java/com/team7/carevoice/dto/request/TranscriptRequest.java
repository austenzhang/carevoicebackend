package com.team7.carevoice.dto.request;

import jakarta.validation.constraints.NotBlank;

public class TranscriptRequest {

    private String createdTime;

    @NotBlank(message = "Patient ID number is required.")
    private Long patientId;

    @NotBlank(message = "Patient Name is required.")
    private String patientName;
    private String body;

    public TranscriptRequest() {
    }

    public TranscriptRequest(String createdTime, Long patientId, String patientName, String body) {
        this.createdTime = createdTime;
        this.patientId = patientId;
        this.patientName = patientName;
        this.body = body;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}