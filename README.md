"# KSCS" 
Job Management System
This is a full-stack application with a React-based frontend and a Java-based backend using Spring Boot. It serves as a job management system Kitchen Saver Employees, allowing users to create, update, and manage jobs.

Frontend Setup:

Prerequisites
-Node.js (version 14 or higher)
-npm (version 6 or higher)
-React (version 17 or higher)

Installation
-Clone the repository: git clone https://github.com/Hhertilien1/KSCS.git
-Navigate to the frontend directory: cd frontend-react
-Install dependencies: npm install
-Start the development server: npm start

Configuration
-The frontend uses React Router for client-side routing.
-The apiService module is used for making API calls to the backend.
-The useUser hook is used for user authentication.
-The REACT_APP_API_URL environment variable is set to http://localhost:8080 in the .env file.


Backend Setup:

Prerequisites
-Java (version 11 or higher)
-Maven (version 3.6 or higher)
-Spring Boot (version 2.5 or higher)

Installation
-Clone the repository: https://github.com/Hhertilien1/KSCS.git
-Navigate to the backend directory: cd backend
-Install dependencies: mvn clean install
-Start the Spring Boot application: mvn spring-boot:run

Configuration
-The backend uses Spring Boot for building the RESTful API.
-The JobController class handles job-related API requests.
-The JobService class provides business logic for job management.
-The JwtUtil class is used for JSON Web Token (JWT) authentication.

Database Setup
-The backend uses a database to store job data. You'll need to configure the database connection properties in the application.properties file.
-The UserRepo and JobRepo classes provide data access objects (DAOs) for user and job data, respectively.

API Documentation
-The API documentation is not provided in the code snippets. You can use tools like Swagger or API Blueprint to generate API documentation.

Troubleshooting
-If you encounter issues with the frontend, check the browser console for errors.
-If you encounter issues with the backend, check the Spring Boot console logs for errors.