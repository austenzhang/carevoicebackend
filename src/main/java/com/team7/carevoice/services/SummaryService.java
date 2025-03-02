package com.team7.carevoice.services;

import com.team7.carevoice.model.Patient;
import com.team7.carevoice.model.Summary;
import com.team7.carevoice.repository.PatientRepository;
import com.team7.carevoice.repository.SummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SummaryService {

    @Autowired
    private SummaryRepository summaryRepository;

    @Autowired
    private PatientRepository patientRepository;

    //get all summaries by patientId
    public List<Summary> getSummariesByPatientId(Long patientId) {
        return summaryRepository.findByPatientId(patientId);
    }

    //create summary
    public Summary createSummary(Long patientId, String body){
        Optional<Patient> patientOptional = patientRepository.findById(patientId);
        if(patientOptional.isPresent()){
            Summary summary = new Summary();
            summary.setBody(body);
            summary.setPatient(patientOptional.get());
            return summaryRepository.save(summary);
        }
        throw new RuntimeException("Patient not found!");
    }

    public Summary getSummaryById(Long summaryId){
        return summaryRepository.findById(summaryId)
                .orElseThrow(() -> new RuntimeException("Summary not found!"));
    }

    public Summary updateSummary(Long summaryId, String body){
        Optional<Summary> summaryOptional = summaryRepository.findById(summaryId);
        if(summaryOptional.isPresent()){
            Summary summary = summaryOptional.get();
            summary.setBody(body);
            return summaryRepository.save(summary);
        }
        throw new RuntimeException("Summary not found!");
    }





}
