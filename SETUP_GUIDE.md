# Complete Setup Guide - MongoDB Atlas

## MongoDB Atlas Setup

1. **Create Account**: Go to [MongoDB Atlas](https://www.mongodb.com/cloud/atlas) and sign up (free tier available)

2. **Create Cluster**: 
   - Click "Build a Database"
   - Choose FREE tier (M0)
   - Select a cloud provider and region
   - Click "Create"

3. **Create Database User**:
   - Go to "Database Access"
   - Click "Add New Database User"
   - Choose "Password" authentication
   - Create username and password (SAVE THESE!)
   - Click "Add User"

4. **Configure Network Access**:
   - Go to "Network Access"
   - Click "Add IP Address"
   - For development, click "Allow Access from Anywhere" (0.0.0.0/0)
   - Click "Confirm"

5. **Get Connection String**:
   - Go back to "Database" → Click "Connect" on your cluster
   - Choose "Connect your application"
   - Copy the connection string
   - It looks like: `mongodb+srv://<username>:<password>@cluster0.xxxxx.mongodb.net/?retryWrites=true&w=majority`
   - Replace `<username>` and `<password>` with your actual credentials
   - Add database name: `mongodb+srv://username:password@cluster0.xxxxx.mongodb.net/resume_analyzer?retryWrites=true&w=majority`

6. **Update application.properties**:
   ```properties
   spring.data.mongodb.uri=mongodb+srv://YOUR_USERNAME:YOUR_PASSWORD@YOUR_CLUSTER.mongodb.net/resume_analyzer?retryWrites=true&w=majority
   ```

## Quick Start

1. Update `backend/src/main/resources/application.properties` with:
   - Your MongoDB Atlas connection string
   - Your OpenRouter API key

2. Run backend: `cd backend && mvn spring-boot:run`

3. Run frontend: `cd frontend && npm install && npm run dev`

4. Open: http://localhost:3000

## Troubleshooting

**MongoDB Connection Issues:**
- Verify connection string format
- Check IP address is whitelisted
- Ensure database user credentials are correct
- Password with special characters may need URL encoding (%40 for @, etc.)

