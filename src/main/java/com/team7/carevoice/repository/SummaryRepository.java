package com.team7.carevoice.repository;

import com.team7.carevoice.model.Summary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SummaryRepository extends JpaRepository<Summary,Long> {
    List<Summary> findByPatientId(Long patientId);

}
