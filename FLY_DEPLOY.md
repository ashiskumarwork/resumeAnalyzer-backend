# Deploy Backend to Fly.io

## Prerequisites
1. Install Fly.io CLI: https://fly.io/docs/getting-started/installing-flyctl/
2. Login: `fly auth login`
3. Your backend code is pushed to GitHub repo: `resumeAnalyzer-backend`

## Step-by-Step Deployment

### 1. Initialize Fly.io App
```bash
cd backend
fly launch
```
- When asked "Would you like to copy its configuration to the new app?" → **No**
- When asked "Would you like to set up a Postgresql database now?" → **No**
- When asked "Would you like to set up an Upstash Redis database now?" → **No**
- App name: Use default or choose your own (e.g., `ai-resume-analyzer-backend`)
- Region: Choose closest to you (e.g., `iad` for Washington DC, `ord` for Chicago)

### 2. Set Environment Variables (Secrets)
```bash
# MongoDB Atlas connection string
fly secrets set MONGODB_URI="your-mongodb-atlas-connection-string"

# OpenRouter API key
fly secrets set OPENROUTER_API_KEY="your-openrouter-api-key"

# JWT secret (use a strong random string)
fly secrets set JWT_SECRET="your-strong-random-secret-key"

# Optional: JWT expiration (default is 86400000 = 1 day)
fly secrets set JWT_EXPIRATION_MS="86400000"

# Optional: OpenRouter URL (default is already set)
fly secrets set OPENROUTER_API_URL="https://openrouter.ai/api/v1/chat/completions"
```

### 3. Deploy
```bash
fly deploy
```

### 4. Get Your Backend URL
After deployment, Fly.io will show your app URL, like:
```
https://ai-resume-analyzer-backend.fly.dev
```

**Save this URL!** You'll need it for the frontend `VITE_API_BASE` environment variable.

### 5. Test Your Backend
```bash
# Check if app is running
fly status

# View logs
fly logs

# Open your app in browser
fly open
```

## Update Frontend Environment Variable

In Vercel, update the `VITE_API_BASE` environment variable:
```
VITE_API_BASE=https://your-app-name.fly.dev/api
```

## Troubleshooting

**Build fails?**
- Check logs: `fly logs`
- Make sure Dockerfile is in the backend root directory

**App won't start?**
- Check environment variables: `fly secrets list`
- Verify MongoDB Atlas IP whitelist includes Fly.io IPs (or use 0.0.0.0/0 for development)

**Port issues?**
- Fly.io automatically sets PORT env var
- Your app already uses `${PORT:8080}` so it should work

## Useful Commands

```bash
# View app status
fly status

# View logs
fly logs

# SSH into running container (for debugging)
fly ssh console

# Scale app (if needed)
fly scale count 1

# View secrets (names only, not values)
fly secrets list
```
