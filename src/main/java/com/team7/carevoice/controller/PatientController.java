package com.team7.carevoice.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.team7.carevoice.dto.response.PatientDocumentsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team7.carevoice.dto.request.CreatePatientRequest;
import com.team7.carevoice.dto.response.ApiResponse;
import com.team7.carevoice.dto.response.DocumentBriefResponse;
import com.team7.carevoice.model.DARP;
import com.team7.carevoice.model.HeadToToeAssessment;
import com.team7.carevoice.model.Patient;
import com.team7.carevoice.model.Summary;
import com.team7.carevoice.model.Transcript;
import com.team7.carevoice.services.DARPService;
import com.team7.carevoice.services.HeadToToeAssessmentService;
import com.team7.carevoice.services.PatientService;
import com.team7.carevoice.services.SummaryService;
import com.team7.carevoice.services.TranscriptService;

@RestController
@RequestMapping("/api")
public class PatientController {
	
	@Autowired
	private PatientService patientService;

	@Autowired
	private DARPService darpService;

	@Autowired
	private HeadToToeAssessmentService assessmentService;

	@Autowired
	private TranscriptService transcriptService;

	@Autowired
	private SummaryService summaryService;

	// For future use if accounts are implemented
	@GetMapping("/patients/{userId}")
	public ResponseEntity<?> getPatientsByUserId(
		@PathVariable(required = false) Long userId
	) {
		if (userId == null) {
			userId = 1L;
		}
		return ResponseEntity.ok(new ApiResponse<>(
			true, 
			"Fetched successfully",
			patientService.getPatientsByUserId(userId))
		);
	}

	@GetMapping("/patients")
	public ResponseEntity<?> getPatientsByUserId() {
		return ResponseEntity.ok(new ApiResponse<>(
			true, 
			"Fetched successfully",
			patientService.getPatientsByUserId(1L))
		);
	}

	@GetMapping("/patient/{patientId}")
	public ResponseEntity<?> getDocumentsByPatientId(@PathVariable(required = true) Long patientId) {
		Patient patient = patientService.getPatientById(patientId);
		if (patient == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "Patient not found", null));
		}

		List<DARP> darps = darpService.findAllByPatientId(patientId);
		List<Summary> summaries = summaryService.getSummariesByPatientId(patientId);
		List<Transcript> transcripts = transcriptService.getTranscriptsByPatientId(patientId);
		List<HeadToToeAssessment> assessments = assessmentService.getAssessmentsByPatientId(patientId);

		List<DocumentBriefResponse> response = new ArrayList<>();

		for (DARP darp : darps) {
			DocumentBriefResponse res = new DocumentBriefResponse(darp.getId(), "DARP", darp.getCreatedTime());
			response.add(res);
		}
		for (Summary summary : summaries) {
			DocumentBriefResponse res = new DocumentBriefResponse(summary.getSummaryId(), "Summary", summary.getCreatedTime());
			response.add(res);
		}
		for (HeadToToeAssessment assessment : assessments) {
			DocumentBriefResponse res = new DocumentBriefResponse(assessment.getId(), "HeadToToe", assessment.getCreatedTime());
			response.add(res);
		}
		for (Transcript transcript : transcripts) {
			DocumentBriefResponse res = new DocumentBriefResponse(transcript.getId(), "Transcript", transcript.getCreatedTime());
			response.add(res);
		}

		Collections.sort(response, Comparator.comparing(DocumentBriefResponse::getCreatedTime).reversed());

		return ResponseEntity.ok(new ApiResponse<>(true, "Fetched documents", new PatientDocumentsResponse(patient.getName(), response)));
	}

	@PostMapping("/patient")
	public ResponseEntity<?> registerPatient(@RequestBody CreatePatientRequest request) {
		Patient newPatient = patientService.save(request);
		return ResponseEntity.ok(new ApiResponse<>(true, "Patient registered successfully", newPatient));
	}
}
