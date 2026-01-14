package com.student.resumeanalyzer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.student.resumeanalyzer.model.ResumeAnalysis;
import com.student.resumeanalyzer.repository.ResumeRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class that handles business logic
 * - Extracts text from PDF files
 * - Calls OpenRouter API
 * - Saves results to MongoDB
 */
@Service
public class ResumeService {
    
    @Autowired
    private ResumeRepository resumeRepository;
    
    @Value("${openrouter.api.key}")
    private String openRouterApiKey;
    
    @Value("${openrouter.api.url:https://openrouter.ai/api/v1/chat/completions}")
    private String openRouterApiUrl;
    
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    
    public ResumeService() {
        this.webClient = WebClient.builder().build();
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * Main method to analyze resume
     * Takes resume file or text and job description
     */
    public ResumeAnalysis analyzeResume(MultipartFile resumeFile, String resumeText, String jobDescription) {
        try {
            // Extract text from file if provided
            String finalResumeText = resumeText;
            if (resumeFile != null && !resumeFile.isEmpty()) {
                finalResumeText = extractTextFromFile(resumeFile);
            }
            
            if (finalResumeText == null || finalResumeText.trim().isEmpty()) {
                throw new RuntimeException("Resume text cannot be empty");
            }
            
            if (jobDescription == null || jobDescription.trim().isEmpty()) {
                throw new RuntimeException("Job description cannot be empty");
            }
            
            // Call OpenRouter API
            ResumeAnalysis analysis = callOpenRouterAPI(finalResumeText, jobDescription);
            
            // Set resume text and job description
            analysis.setResumeText(finalResumeText);
            analysis.setJobDescription(jobDescription);
            
            // Save to MongoDB
            resumeRepository.save(analysis);
            
            return analysis;
            
        } catch (Exception e) {
            throw new RuntimeException("Error analyzing resume: " + e.getMessage(), e);
        }
    }
    
    /**
     * Extract text from PDF file
     * Simple PDF text extraction using PDFBox
     */
    private String extractTextFromFile(MultipartFile file) {
        try {
            if (file.getContentType() != null && file.getContentType().equals("application/pdf")) {
                InputStream inputStream = file.getInputStream();
                PDDocument document = PDDocument.load(inputStream);
                PDFTextStripper stripper = new PDFTextStripper();
                String text = stripper.getText(document);
                document.close();
                inputStream.close();
                return text;
            } else if (file.getContentType() != null && file.getContentType().equals("text/plain")) {
                return new String(file.getBytes());
            } else {
                throw new RuntimeException("Unsupported file type. Please use PDF or text file.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error reading file: " + e.getMessage(), e);
        }
    }
    
    /**
     * Call OpenRouter API to get AI analysis
     * Uses WebClient for reactive HTTP calls
     */
    private ResumeAnalysis callOpenRouterAPI(String resumeText, String jobDescription) {
        try {
            // Build the prompt for AI
            String prompt = buildPrompt(resumeText, jobDescription);
            
            // Prepare request body
            String requestBody = buildRequestBody(prompt);
            
            // Make API call
            String response = webClient.post()
                    .uri(openRouterApiUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + openRouterApiKey)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header("HTTP-Referer", "http://localhost:3000")
                    .header("X-Title", "AI Resume Analyzer")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(); // Blocking call - simple for students
            
            // Parse response
            return parseAIResponse(response);
            
        } catch (Exception e) {
            throw new RuntimeException("Error calling OpenRouter API: " + e.getMessage(), e);
        }
    }
    
    /**
     * Build the prompt for AI
     */
    private String buildPrompt(String resumeText, String jobDescription) {
        return "You are an ATS resume analyzer. Compare this resume with the job description and return a JSON response with:\n" +
               "1. Score (0-100) - how well the resume matches the job\n" +
               "2. Strengths - list of strengths (array of strings)\n" +
               "3. Weaknesses - list of weaknesses (array of strings)\n" +
               "4. Missing Skills - list of missing skills from job description (array of strings)\n" +
               "5. Suggestions - list of improvement suggestions (array of strings)\n\n" +
               "Resume:\n" + resumeText + "\n\n" +
               "Job Description:\n" + jobDescription + "\n\n" +
               "Return ONLY valid JSON in this exact format:\n" +
               "{\n" +
               "  \"score\": number,\n" +
               "  \"strengths\": [\"string1\", \"string2\"],\n" +
               "  \"weaknesses\": [\"string1\", \"string2\"],\n" +
               "  \"missingSkills\": [\"string1\", \"string2\"],\n" +
               "  \"suggestions\": [\"string1\", \"string2\"]\n" +
               "}";
    }
    
    /**
     * Build request body for OpenRouter API
     */
    private String buildRequestBody(String prompt) {
        // Using a simple model - students can change this
        return "{\n" +
               "  \"model\": \"openai/gpt-3.5-turbo\",\n" +
               "  \"messages\": [\n" +
               "    {\n" +
               "      \"role\": \"user\",\n" +
               "      \"content\": \"" + escapeJson(prompt) + "\"\n" +
               "    }\n" +
               "  ],\n" +
               "  \"temperature\": 0.7\n" +
               "}";
    }
    
    /**
     * Escape JSON string for request body
     */
    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
    
    /**
     * Parse AI response and create ResumeAnalysis object
     */
    private ResumeAnalysis parseAIResponse(String response) {
        try {
            ResumeAnalysis analysis = new ResumeAnalysis();
            
            // Parse JSON response
            JsonNode rootNode = objectMapper.readTree(response);
            
            // Get the message content
            JsonNode choices = rootNode.get("choices");
            if (choices != null && choices.isArray() && choices.size() > 0) {
                JsonNode message = choices.get(0).get("message");
                if (message != null) {
                    String content = message.get("content").asText();
                    
                    // Try to extract JSON from content (AI might wrap it in markdown)
                    content = extractJsonFromContent(content);
                    
                    // Parse the actual JSON
                    JsonNode analysisNode = objectMapper.readTree(content);
                    
                    // Extract fields
                    analysis.setScore(analysisNode.get("score").asInt());
                    analysis.setStrengths(parseStringArray(analysisNode.get("strengths")));
                    analysis.setWeaknesses(parseStringArray(analysisNode.get("weaknesses")));
                    analysis.setMissingSkills(parseStringArray(analysisNode.get("missingSkills")));
                    analysis.setSuggestions(parseStringArray(analysisNode.get("suggestions")));
                }
            }
            
            return analysis;
            
        } catch (Exception e) {
            throw new RuntimeException("Error parsing AI response: " + e.getMessage(), e);
        }
    }
    
    /**
     * Extract JSON from AI response (might be wrapped in markdown code blocks)
     */
    private String extractJsonFromContent(String content) {
        // Remove markdown code blocks if present
        content = content.trim();
        if (content.startsWith("```json")) {
            content = content.substring(7);
        } else if (content.startsWith("```")) {
            content = content.substring(3);
        }
        if (content.endsWith("```")) {
            content = content.substring(0, content.length() - 3);
        }
        return content.trim();
    }
    
    /**
     * Parse JSON array to List<String>
     */
    private List<String> parseStringArray(JsonNode node) {
        List<String> list = new ArrayList<>();
        if (node != null && node.isArray()) {
            for (JsonNode item : node) {
                list.add(item.asText());
            }
        }
        return list;
    }
}

