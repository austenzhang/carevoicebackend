package com.team7.carevoice.services;

import com.team7.carevoice.model.Transcript;
import com.team7.carevoice.repository.TranscriptRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.team7.carevoice.dto.response.ApiResponse;
import com.team7.carevoice.dto.request.TranscriptRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class AudioToTranscriptionService {
    private static final Logger logger = LoggerFactory.getLogger(AudioToTranscriptionService.class);

    @Value("${deepgram.api.key}")
    private String deepgramApiKey;

    private final ResourceLoader resourceLoader;
    private final TranscriptService transcriptService;

    public AudioToTranscriptionService(
        ResourceLoader resourceLoader,
        TranscriptService transcriptService
    ) {
        this.resourceLoader = resourceLoader;
        this.transcriptService = transcriptService;
    }

    public ApiResponse<Transcript> transcribeAudio(MultipartFile file, Long patientId, String patientName) throws IOException, InterruptedException {
        // Get audio content from uploaded file
        byte[] audioContent = file.getBytes();
        
        // Get transcription from Deepgram
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://api.deepgram.com/v1/listen?model=nova-3&smart_format=true&language=en-US"))
                .header("Authorization", "Token " + deepgramApiKey)
                .header("Content-Type", "audio/mp3")
                .POST(HttpRequest.BodyPublishers.ofByteArray(audioContent))
                .build();

        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        // Parse response
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response.body());
        String transcriptText = rootNode
            .path("results")
            .path("channels")
            .get(0)
            .path("alternatives")
            .get(0)
            .path("transcript")
            .asText();

        // Create TranscriptRequest
        TranscriptRequest request = new TranscriptRequest();
        request.setBody(transcriptText);
        request.setPatientName(patientName);
        request.setPatientId(patientId);
        request.setCreatedTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        // Use existing service to save to DB
        return transcriptService.createTranscript(System.currentTimeMillis(), request);
    }
} 