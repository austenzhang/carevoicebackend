package com.team7.carevoice.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team7.carevoice.model.DARP;
import com.team7.carevoice.repository.DARPRepository;

@Service
public class DARPService {
	
	@Autowired
	private DARPRepository darpRepository;

	public List<DARP> findAllByPatientId(Long patientId) {
        return darpRepository.findByPatientId(patientId);
    }

	public DARP getDARPById(Long id) {
		return darpRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("DARP not found with ID: " + id));
	}

	public DARP updateDARP(Long id, Map<String, String> updatedBody) {
		Optional<DARP> existingDARPOpt = darpRepository.findById(id);
		if(!existingDARPOpt.isPresent()) {
			return null;
		}
		DARP existingDARP = existingDARPOpt.get();

		if(updatedBody.get("action") != null) {
			existingDARP.setAction(updatedBody.get("action"));
		}
		if(updatedBody.get("plan") != null) {
			existingDARP.setPlan(updatedBody.get("plan"));
		}
		if(updatedBody.get("data") != null) {
			existingDARP.setData(updatedBody.get("data"));
		}
		if(updatedBody.get("response") != null) {
			existingDARP.setResponse(updatedBody.get("response"));
		}
		
		return darpRepository.save(existingDARP);
	}

	public DARP createDARP(DARP newDARP) {
		return darpRepository.save(newDARP);
	}

}
