package com.team7.carevoice.controller;

import com.team7.carevoice.dto.response.ApiResponse;
import com.team7.carevoice.model.Summary;
import com.team7.carevoice.services.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/summary")
public class SummaryController {
    @Autowired
    private SummaryService summaryService;

    @GetMapping("/{summaryId}")
    public ApiResponse<?> getSummaryById(@PathVariable Long summaryId){
        Summary summary = summaryService.getSummaryById(summaryId);
        Map<String, Object> response = new HashMap<>();
        response.put("createdTime", summary.getCreatedTime().toString());
        response.put("patientName", summary.getPatient().getName());
        response.put("patientId", summary.getPatient().getId());
        response.put("body", summary.getBody());
        return new ApiResponse<>(true, "Retrieved summary", response);
    }

    @PatchMapping("/{summaryId}")
    public Summary updateSummary(@PathVariable Long summaryId, @RequestBody Map<String, String> requestBody) {
        String body = requestBody.get("body");
        return summaryService.updateSummary(summaryId, body);
    }

    @PostMapping("/patient/{patientId}")
    public Summary createSummary(@PathVariable Long patientId, @RequestBody Map<String, String> requestBody) {
        String body = requestBody.get("body");
        return summaryService.createSummary(patientId, body);
    }
}
