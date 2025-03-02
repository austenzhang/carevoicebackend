package com.team7.carevoice.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "document_transcriptions")
public class Transcript {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String patientName;
    private LocalDateTime createdTime;

    @ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "patient_id")
	@JsonBackReference
	private Patient patient;

    @Column(columnDefinition = "TEXT") // for large texts
    private String body;

    @PrePersist
    protected void onCreate() {
        this.createdTime = LocalDateTime.now();
    }

    // Constructors
    public Transcript() {}

    public Transcript(Patient patient, String name, String type, String body) {
        this.patient = patient;
        this.patientName = name;
        this.createdTime = LocalDateTime.now();
        this.body = body;

    }

    // Getters and Setters
    public String getName() {
        return patientName;
    }
    public void setName(String name) {
        this.patientName = name;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}