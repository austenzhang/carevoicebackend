package com.team7.carevoice.controller;

import com.team7.carevoice.dto.response.ApiResponse;
import com.team7.carevoice.model.HeadToToeAssessment;
import com.team7.carevoice.model.Patient;
import com.team7.carevoice.services.HeadToToeAssessmentService;
import com.team7.carevoice.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/head-to-toe")
public class HeadToToeAssessmentController {

    private final HeadToToeAssessmentService assessmentService;

    @Autowired
    public HeadToToeAssessmentController(HeadToToeAssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    // GET endpoint - Retrieve an assessment by ID
    @GetMapping("/{id}")
    public ApiResponse<?> getAssessmentById(@PathVariable Long id) {
        return assessmentService.getAssessmentById(id);
    }

    // PATCH endpoint - Update an assessment by ID
    @PatchMapping("/{id}")
    public ApiResponse<HeadToToeAssessment> updateAssessment(@PathVariable Long id, @RequestBody HeadToToeAssessment updatedAssessment) {
        return assessmentService.updateAssessment(id, updatedAssessment);
    }

    @Autowired
    private PatientService patientService;

    @PostMapping("/patient/{patientId}")
    public ApiResponse<HeadToToeAssessment> createNewAssessment(
            @PathVariable Long patientId,
            @RequestBody Map<String, String> requestBody) {


        Patient patient = patientService.getPatientById(patientId);
        if (patient == null) {
            return new ApiResponse<>(false, "Patient not found", null);
        }

        String neurological = requestBody.get("neurological");
        String heent = requestBody.get("heent");
        String respiratory = requestBody.get("respiratory");
        String cardiac = requestBody.get("cardiac");
        String peripheralVascular = requestBody.get("peripheralVascular");
        String integumentary = requestBody.get("integumentary");
        String musculoskeletal = requestBody.get("musculoskeletal");
        String gastrointestinal = requestBody.get("gastrointestinal");
        String genitourinary = requestBody.get("genitourinary");
        String sleepRest = requestBody.get("sleepRest");
        String psychosocial = requestBody.get("psychosocial");

        HeadToToeAssessment newAssessment = assessmentService.createNewAssessment(
                patient, neurological, heent, respiratory, cardiac, peripheralVascular,
                integumentary, musculoskeletal, gastrointestinal, genitourinary, sleepRest, psychosocial);

        return new ApiResponse<>(true, "Assessment created successfully", newAssessment);
    }


}
