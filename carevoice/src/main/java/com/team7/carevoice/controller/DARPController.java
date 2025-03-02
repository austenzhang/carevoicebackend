package com.team7.carevoice.controller;

import java.util.HashMap;
import java.util.Map;

import com.team7.carevoice.model.Patient;
import com.team7.carevoice.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.team7.carevoice.dto.response.ApiResponse;
import com.team7.carevoice.model.DARP;
import com.team7.carevoice.services.DARPService;

@RestController
@RequestMapping("/api/DARP")
public class DARPController {
	
	@Autowired
	private DARPService darpService;

	@GetMapping("/{id}")
	public ResponseEntity<?> getDARPById(@PathVariable Long id) {

		DARP darp = darpService.getDARPById(id);

		Map<String, Object> response = new HashMap<>();
        response.put("createdTime", darp.getCreatedTime());
		response.put("patientName", darp.getPatient().getName());
		response.put("patientId", darp.getPatient().getId());

        Map<String, String> body = new HashMap<>();
        body.put("data", darp.getData());
        body.put("action", darp.getAction());
        body.put("response", darp.getResponse());
        body.put("plan", darp.getPlan());

        response.put("body", body);

		return ResponseEntity.ok(new ApiResponse<>(
			true, 
			"Retrieved DARP", 
			response));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<?> updateDARP(@PathVariable Long id, @RequestBody Map<String, String> updatedBody) {
		return ResponseEntity.ok(new ApiResponse<>(
			true, 
			"Updated successfully",
			darpService.updateDARP(id, updatedBody)));
	}

	@Autowired
	private PatientService patientService;

	@PostMapping("/patient/{patientId}")
	public ResponseEntity<?> createDARP(@PathVariable Long patientId, @RequestBody Map<String, String> requestBody) {
		String data = requestBody.get("data");
		String action = requestBody.get("action");
		String response = requestBody.get("response");
		String plan = requestBody.get("plan");

		// Retrieve the Patient object using the patientId
		Patient patient = patientService.getPatientById(patientId);
		if (patient == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "Patient not found", null));
		}

		DARP newDARP = new DARP(data, action, response, plan, patient);
		DARP savedDARP = darpService.createDARP(newDARP);

		return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "DARP created successfully", savedDARP));
	}


}
