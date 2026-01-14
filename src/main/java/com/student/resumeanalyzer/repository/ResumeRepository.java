package com.student.resumeanalyzer.repository;

import com.student.resumeanalyzer.model.ResumeAnalysis;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Simple repository interface for MongoDB operations
 * Spring Data MongoDB will automatically implement this
 */
@Repository
public interface ResumeRepository extends MongoRepository<ResumeAnalysis, String> {
    // No custom methods needed - using default CRUD operations
}

