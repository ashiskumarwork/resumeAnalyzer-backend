package com.student.resumeanalyzer.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Model class to store resume analysis results in MongoDB
 * Simple POJO - no complex annotations
 */
@Document(collection = "resume_analyses")
public class ResumeAnalysis {
    
    @Id
    private String id;
    
    private String resumeText;
    private String jobDescription;
    
    // AI Analysis Results
    private int score;
    private List<String> strengths;
    private List<String> weaknesses;
    private List<String> missingSkills;
    private List<String> suggestions;
    
    private LocalDateTime timestamp;
    
    // Default constructor
    public ResumeAnalysis() {
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getResumeText() {
        return resumeText;
    }
    
    public void setResumeText(String resumeText) {
        this.resumeText = resumeText;
    }
    
    public String getJobDescription() {
        return jobDescription;
    }
    
    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }
    
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    public List<String> getStrengths() {
        return strengths;
    }
    
    public void setStrengths(List<String> strengths) {
        this.strengths = strengths;
    }
    
    public List<String> getWeaknesses() {
        return weaknesses;
    }
    
    public void setWeaknesses(List<String> weaknesses) {
        this.weaknesses = weaknesses;
    }
    
    public List<String> getMissingSkills() {
        return missingSkills;
    }
    
    public void setMissingSkills(List<String> missingSkills) {
        this.missingSkills = missingSkills;
    }
    
    public List<String> getSuggestions() {
        return suggestions;
    }
    
    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

