package com.team7.carevoice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team7.carevoice.model.DARP;

@Repository
public interface DARPRepository extends JpaRepository<DARP, Long> {
	List<DARP> findByPatientId(Long patientId);
}
