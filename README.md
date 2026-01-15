# AI Resume Analyzer - Backend

Spring Boot backend for the AI Resume Analyzer application.

## Setup Instructions

1. **Copy the example properties file:**
   ```bash
   cp src/main/resources/application.properties.example src/main/resources/application.properties
   ```

2. **Edit `application.properties` and add your actual values:**
   - MongoDB Atlas URI (get from MongoDB Atlas dashboard)
   - OpenRouter API key (get from https://openrouter.ai/)
   - JWT secret (use a strong random string)

3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

The backend will start on `http://localhost:8080`

## Important Security Note

**Never commit `application.properties` to Git!** It contains sensitive credentials.
The `.gitignore` file is configured to exclude it automatically.
