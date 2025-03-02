package com.team7.carevoice.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Summary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long summaryId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "patient_id")
    @JsonBackReference
    private Patient patient;

    private LocalDateTime createdTime;

    @Column(columnDefinition = "TEXT")
    private String body;

    @PrePersist
    protected void onCreate() {
        this.createdTime = LocalDateTime.now();
    }

    public Long getSummaryId() {
        return summaryId;
    }

    public Patient getPatient() {
        return patient;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public String getBody() {
        return body;
    }

    public void setSummaryId(Long summaryId) {
        this.summaryId = summaryId;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
