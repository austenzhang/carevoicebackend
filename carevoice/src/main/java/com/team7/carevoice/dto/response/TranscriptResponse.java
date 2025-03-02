package com.team7.carevoice.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.team7.carevoice.model.Transcript;

@JsonPropertyOrder({ "createdTime", "name", "patientId", "body" })
public class TranscriptResponse {
    private String createdTime;
    private String name;
    private Long patientId;
    private String body;

    public TranscriptResponse() {
        // default constructor
    }

    // A convenience constructor to build from the Transcript entity
    public TranscriptResponse(Transcript transcript) {
        // Format date/time as needed, or store it as an ISO string
        if (transcript.getCreatedTime() != null) {
            this.createdTime = transcript.getCreatedTime().toString(); 
        }
        this.name = transcript.getName();
        this.patientId = transcript.getPatient().getId();
        this.body = transcript.getBody();
    }

    // Getters/Setters (if needed)
    public String getCreatedTime() {
        return createdTime;
    }
    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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