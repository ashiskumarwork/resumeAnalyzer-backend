package com.student.resumeanalyzer.controller;

import com.student.resumeanalyzer.model.ResumeAnalysis;
import com.student.resumeanalyzer.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Simple REST controller
 * Only one endpoint: POST /analyze
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Allow frontend to call this API
public class ResumeController {
    
    @Autowired
    private ResumeService resumeService;
    
    /**
     * Main endpoint to analyze resume
     * Accepts either file upload or text
     */
    @PostMapping("/analyze")
    public ResponseEntity<?> analyzeResume(
            @RequestParam(value = "resumeFile", required = false) MultipartFile resumeFile,
            @RequestParam(value = "resumeText", required = false) String resumeText,
            @RequestParam("jobDescription") String jobDescription) {
        
        try {
            // Call service to analyze
            ResumeAnalysis result = resumeService.analyzeResume(resumeFile, resumeText, jobDescription);
            
            // Return result as JSON
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            // Simple error handling
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
}

