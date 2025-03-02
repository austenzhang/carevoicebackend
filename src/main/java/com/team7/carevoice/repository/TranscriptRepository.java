package com.team7.carevoice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team7.carevoice.model.Transcript;

@Repository
public interface TranscriptRepository extends JpaRepository<Transcript, Long> {

    // can add custom finder methods here
    // for example:
    List<Transcript> findByPatientId(Long patientId);
}