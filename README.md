# AYUSH Startup Portal

A fullstack application for AYUSH (Ayurveda, Yoga & Naturopathy, Unani, Siddha, and Homeopathy) startup registration and management.

## Project Structure

- **Backend**: Spring Boot application with REST APIs
- **Frontend**: React application with Material UI

## Prerequisites

- Java 17 or higher
- Maven
- Node.js 16 or higher
- MySQL database

## Database Setup

1. Make sure MySQL is running
2. The application will automatically create the database `ayush_db` if it doesn't exist
3. Default credentials (update in `application.properties` if needed):
   - Username: `root`
   - Password: `root`

## Running the Application

### 1. Start the Backend (Spring Boot)

Open a terminal in the project root directory and run:

```bash
./mvnw spring-boot:run
```

The backend will start on: http://localhost:8080

### 2. Start the Frontend (React)

Open another terminal and navigate to the frontend directory:

```bash
cd frontend
npm install
npm start
```

The frontend will start on: http://localhost:3000

### 3. Access the Application

Open your browser and go to: http://localhost:3000

## Features

- **User Registration**: Register new AYUSH startups
- **User Login**: Secure authentication
- **Application Management**: Create and manage startup applications
- **Document Upload**: Upload required documents
- **Dashboard**: View application status and progress
- **AI Chatbot**: Get assistance with the application process
- **Profile Management**: View and update user profile

## API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/logout` - User logout
- `GET /api/auth/check-session` - Check session status

### Registration
- `POST /api/registration/register` - User registration

### Application
- `GET /api/application/form-data` - Get application form data
- `POST /api/application/save` - Save application
- `POST /api/application/submit` - Submit application
- `GET /api/application/status` - Get application status
- `POST /api/application/upload-document` - Upload documents
- `GET /api/application/download-document/{filename}` - Download documents

### Dashboard
- `GET /api/dashboard/data` - Get dashboard data
- `GET /api/dashboard/profile` - Get user profile

### Chatbot
- `POST /api/chatbot/chat` - Send message to chatbot
- `POST /api/chatbot/clear` - Clear chat history

## Technology Stack

### Backend
- Spring Boot 3.2.0
- Spring Data JPA
- MySQL Database
- Spring Validation

### Frontend
- React 19
- React Router DOM
- Material UI
- Axios for API calls

## Development

### Backend Development
- The backend uses Spring Boot with JPA for database operations
- Controllers are REST-based and return JSON responses
- CORS is configured to allow frontend communication

### Frontend Development
- React components are organized in the `src/pages` directory
- Material UI is used for styling and components
- Axios is used for API communication

## Troubleshooting

1. **Database Connection Issues**: Ensure MySQL is running and credentials are correct
2. **Port Conflicts**: Make sure ports 8080 (backend) and 3000 (frontend) are available
3. **CORS Issues**: The backend is configured to allow requests from http://localhost:3000

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request 