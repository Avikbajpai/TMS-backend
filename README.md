# NexusTMS - Full-Stack Ticket Management System

Modern, high-density Ticket Management System with dual-stack implementation options (TypeScript/Express and Java/Spring Boot).

## 📦 Project Versions

### 1. TypeScript Version (Active Preview)
- **Backend**: Express.js + Layered Architecture.
- **Frontend**: React + High Density Theme.
- **Location**: Root directory.
- **Run**: `npm run dev`

### 2. Java Spring Boot Version (Replatformed)
- **Backend**: Spring Boot 3 + Spring Data JPA + H2.
- **Frontend**: Shares the root React frontend.
- **Location**: `/backend-java/`
- **Run**: See instructions in `/backend-java/README.md`

## 🚀 Quick Start (Local)

### Prerequisites
- [Node.js](https://nodejs.org/) (v18 or higher)
- npm

### 1. Installation
```bash
npm install
```

### 2. Environment Setup
The application is built with internal business logic and does not strictly require an AI API key for its core features.
```bash
cp .env.example .env
```

### 3. Development
Starts the backend server and the frontend HMR:
```bash
npm run dev
```
Open [http://localhost:3000](http://localhost:3000)

## 🏗️ Architecture
- **Controller Layer**: REST API endpoints and validation (`src/api/controllers.ts`).
- **Service Layer**: Business rules and state transitions (`src/api/services.ts`).
- **Repository Layer**: In-memory data persistence (`src/api/repositories.ts`).
- **Frontend**: React + Tailwind CSS with high-density theme.

## 🔑 Role-Based Access
Use the simulator in the sidebar/navigation to test three distinct user perspectives:
- **Customer**: Create and track tickets.
- **Engineer**: Manage assignments and resolve technical issues.
- **Admin**: System-wide control and engineer dispatching.

## 🤖 AI Features (Optional)
This project is configured to work with the Google Gemini SDK. To enable AI-powered features:
1. Get a free API key at [Google AI Studio](https://aistudio.google.com/).
2. Add it to your `.env` file as `GEMINI_API_KEY`.
