# AI Resume Analyzer - Backend

Spring Boot backend for the AI Resume Analyzer project.

## Quick Setup

1. **Set up MongoDB Atlas**: 
   - Create a free account at https://www.mongodb.com/cloud/atlas
   - Create a cluster (free tier is fine)
   - Create a database user
   - Get your connection string from "Connect" → "Connect your application"
   - Add your IP address to the network access list (or use 0.0.0.0/0 for development)

2. **Update configuration**: Edit `src/main/resources/application.properties`:
   - Add your MongoDB Atlas connection string
   - Add your OpenRouter API key

3. **Run**: `mvn spring-boot:run`

## Configuration

Edit `src/main/resources/application.properties`:

```properties
# MongoDB Atlas Connection String
# Get this from MongoDB Atlas dashboard: Connect → Connect your application
# Format: mongodb+srv://username:password@cluster.mongodb.net/database?retryWrites=true&w=majority
spring.data.mongodb.uri=mongodb+srv://username:password@cluster.mongodb.net/resume_analyzer?retryWrites=true&w=majority

# OpenRouter API (REQUIRED - get key from https://openrouter.ai/)
openrouter.api.key=your-api-key-here
```

## API Endpoint

**POST** `/api/analyze`

Accepts:
- `resumeFile` (optional): PDF or text file
- `resumeText` (optional): Resume text
- `jobDescription` (required): Job description

Returns: JSON with score, strengths, weaknesses, missing skills, suggestions

## Project Structure

- `Application.java` - Main Spring Boot class
- `controller/ResumeController.java` - REST endpoint
- `service/ResumeService.java` - Business logic + OpenRouter API calls
- `model/ResumeAnalysis.java` - MongoDB document model
- `repository/ResumeRepository.java` - MongoDB repository

## Troubleshooting

- **Port 8080 in use**: Change `server.port` in `application.properties`
- **MongoDB Atlas connection error**: 
  - Verify your connection string is correct
  - Check that your IP address is whitelisted in Atlas Network Access
  - Ensure your database user has proper permissions
- **API key error**: Verify OpenRouter API key is correct
