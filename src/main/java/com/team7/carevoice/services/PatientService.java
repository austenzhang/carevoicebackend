package com.team7.carevoice.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team7.carevoice.dto.request.CreatePatientRequest;
import com.team7.carevoice.model.CareVoiceUser;
import com.team7.carevoice.model.Patient;
import com.team7.carevoice.repository.PatientRepository;
import com.team7.carevoice.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class PatientService {
	
	@Autowired
	private PatientRepository patientRepository;

	@Autowired
	private UserRepository userRepository;

	public Patient getPatientById(Long id) {
		return patientRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Patient not found with id: " + id));
	}

	public List<Patient> getPatientsByUserId(Long userId) {
        return patientRepository.findByCarevoiceuser_Id(userId);
    }

	@Transactional
	public Patient save(CreatePatientRequest patientReq) {

		Optional<CareVoiceUser> careVoiceUserOpt = userRepository.findById(1L);
		
		Patient newpatient = new Patient(
			patientReq.getAhsNumber(),
			patientReq.getName(),
			patientReq.getDateOfBirth(),
			careVoiceUserOpt.get()
		);
		return patientRepository.save(newpatient);
	}
}
