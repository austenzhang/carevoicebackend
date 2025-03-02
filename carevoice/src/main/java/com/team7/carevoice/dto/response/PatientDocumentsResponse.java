package com.team7.carevoice.dto.response;

import java.util.List;

public class PatientDocumentsResponse {
    private String patientName;
    private List<DocumentBriefResponse> documents;

    public PatientDocumentsResponse(String patientName, List<DocumentBriefResponse> documents) {
        this.patientName = patientName;
        this.documents = documents;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public List<DocumentBriefResponse> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentBriefResponse> documents) {
        this.documents = documents;
    }
}