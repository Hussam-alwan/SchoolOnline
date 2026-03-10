# Lab 10: Full Stack Integration - Capstone Project

## Overview

This capstone homework brings together everything you've learned throughout the bootcamp. You'll build a complete feature from database to UI, implement authentication, optimize performance, and deploy the application.

## Learning Objectives

By completing this homework, you will:
- Integrate frontend and backend components
- Implement complete CRUD operations across the stack
- Configure authentication and authorization
- Handle errors gracefully across all layers
- Optimize application performance
- Write end-to-end tests
- Deploy a production-ready application
- Monitor and maintain the application

## Prerequisites

- Completed Labs 1-9
- Understanding of full-stack architecture
- Familiarity with React and Spring Boot
- Knowledge of Docker and deployment
- Understanding of REST APIs and HTTP

---

## Assignment 1: Teacher Management Feature (Full Stack)

### Task
Build a complete teacher management feature from database to UI, including all CRUD operations.

### Requirements

#### Backend Implementation

1. **Create Teacher Entity** (`src/main/java/com/bootcamp/onlineschool/entity/Teacher.java`):
   - Fields: `id`, `teacherId`, `name`, `email`, `department`, `yearsOfExperience`, `salary`, `hireDate`
   - JPA annotations with proper constraints
   - Relationships: One-to-Many with Course
   - Validation annotations

2. **Create TeacherRepository** (`src/main/java/com/bootcamp/onlineschool/repository/TeacherRepository.java`):
   - Extend JpaRepository
   - Custom query methods:
     - `findByDepartment(String department)`
     - `findByYearsOfExperienceGreaterThan(Integer years)`
     - `findByEmail(String email)`
   - JPQL queries for complex searches

3. **Create TeacherService** (`src/main/java/com/bootcamp/onlineschool/service/TeacherService.java`):
   - CRUD operations with business logic
   - DTO conversion methods
   - Exception handling
   - Transaction management with @Transactional

4. **Create TeacherDTO** (`src/main/java/com/bootcamp/onlineschool/dto/TeacherDTO.java`):
   - All teacher fields
   - Validation annotations
   - Computed fields (e.g., `yearsAtSchool`)

5. **Create TeacherController** (`src/main/java/com/bootcamp/onlineschool/controller/TeacherController.java`):
   - REST endpoints:
     - `GET /api/teachers` - Get all teachers
     - `GET /api/teachers/{id}` - Get teacher by ID
     - `POST /api/teachers` - Create teacher
     - `PUT /api/teachers/{id}` - Update teacher
     - `DELETE /api/teachers/{id}` - Delete teacher
     - `GET /api/teachers/department/{dept}` - Filter by department
   - Proper HTTP status codes
   - Exception handling

#### Frontend Implementation

6. **Create Teacher Service** (`online-school-frontend/src/services/teacherService.js`):
   - API calls using axios
   - Methods for all CRUD operations
   - Error handling
   - Request/response interceptors

7. **Create Teacher Components**:
   - `TeacherList.jsx` - Display all teachers in a table
   - `TeacherForm.jsx` - Create/edit teacher form
   - `TeacherDetails.jsx` - View teacher details
   - `TeacherCard.jsx` - Reusable teacher card component

8. **Create Teacher Pages**:
   - `Teachers.jsx` - Main teachers page with list and actions
   - Implement search and filter functionality
   - Add pagination (10 teachers per page)
   - Add sorting by name, department, experience

9. **Add Routing**:
   - Update `App.jsx` with teacher routes
   - `/teachers` - List all teachers
   - `/teachers/new` - Create new teacher
   - `/teachers/:id` - View teacher details
   - `/teachers/:id/edit` - Edit teacher

10. **Styling**:
    - Use Material-UI components
    - Responsive design
    - Loading states
    - Error states
    - Success notifications

#### Testing

11. **Backend Tests**:
    - `TeacherRepositoryTest.java` - Repository tests (minimum 8 tests)
    - `TeacherServiceTest.java` - Service tests (minimum 10 tests)
    - `TeacherControllerTest.java` - Controller tests with MockMvc (minimum 12 tests)

12. **Frontend Tests**:
    - `TeacherList.test.jsx` - Component tests (minimum 6 tests)
    - `TeacherForm.test.jsx` - Form validation tests (minimum 8 tests)
    - `teacherService.test.js` - API service tests (minimum 6 tests)

### Expected Deliverables
- Complete backend implementation (Entity, Repository, Service, DTO, Controller)
- Complete frontend implementation (Service, Components, Pages, Routes)
- All tests passing (minimum 50 tests total)
- Feature working end-to-end
- Documentation of the feature

---

## Assignment 2: Authentication & Authorization

### Task
Implement user authentication and role-based authorization across the full stack.

### Requirements

#### Backend Implementation

1. **Create User Entity**:
   - Fields: `id`, `username`, `password`, `email`, `role` (ADMIN, TEACHER, STUDENT)
   - Password encryption with BCrypt
   - Relationships as needed

2. **Create Authentication Service**:
   - User registration
   - User login with JWT token generation
   - Token validation
   - Password reset functionality

3. **Configure Spring Security**:
   - Create `SecurityConfig.java`
   - Configure authentication manager
   - Configure authorization rules:
     - `/api/auth/**` - Public
     - `/api/students/**` - Authenticated users
     - `/api/teachers/**` - ADMIN and TEACHER roles
     - `/api/admin/**` - ADMIN role only
   - Configure CORS for frontend

4. **Create Auth Controller**:
   - `POST /api/auth/register` - Register new user
   - `POST /api/auth/login` - Login and get token
   - `POST /api/auth/logout` - Logout
   - `GET /api/auth/me` - Get current user info

#### Frontend Implementation

5. **Create Auth Service** (`authService.js`):
   - Login function
   - Logout function
   - Register function
   - Token management (localStorage)
   - Auto-logout on 401 errors

6. **Create Auth Context** (`AuthContext.jsx`):
   - Global authentication state
   - User information
   - Login/logout functions
   - Protected route wrapper

7. **Create Auth Components**:
   - `Login.jsx` - Login form
   - `Register.jsx` - Registration form
   - `ProtectedRoute.jsx` - Route wrapper for authenticated pages
   - `RoleBasedRoute.jsx` - Route wrapper for role-based access

8. **Update API Service**:
   - Add JWT token to all requests
   - Handle 401 errors (redirect to login)
   - Handle 403 errors (show access denied)

9. **Update Navigation**:
   - Show/hide menu items based on authentication
   - Show/hide menu items based on user role
   - Display user info in navbar
   - Add logout button

#### Testing

10. **Backend Tests**:
    - `AuthServiceTest.java` - Authentication tests (minimum 10 tests)
    - `SecurityConfigTest.java` - Authorization tests (minimum 8 tests)

11. **Frontend Tests**:
    - `Login.test.jsx` - Login form tests (minimum 6 tests)
    - `AuthContext.test.jsx` - Context tests (minimum 8 tests)
    - `ProtectedRoute.test.jsx` - Route protection tests (minimum 4 tests)

### Expected Deliverables
- Complete authentication system
- Role-based authorization
- Protected routes
- All tests passing (minimum 36 tests)
- Login/logout working end-to-end

---

## Assignment 3: Error Handling & User Experience

### Task
Implement comprehensive error handling and improve user experience across the application.

### Requirements

#### Backend Error Handling

1. **Create Custom Exceptions**:
   - `ResourceNotFoundException`
   - `DuplicateResourceException`
   - `UnauthorizedException`
   - `ValidationException`

2. **Create Global Exception Handler**:
   - Handle all custom exceptions
   - Handle validation errors
   - Handle authentication errors
   - Return consistent error responses
   - Log all errors

3. **Create ErrorResponse DTO**:
   - `timestamp`, `status`, `error`, `message`, `path`, `errors`
   - Consistent format for all errors

#### Frontend Error Handling

4. **Create Error Boundary Component**:
   - Catch React errors
   - Display user-friendly error page
   - Log errors to console
   - Provide recovery options

5. **Create Notification System**:
   - Success notifications
   - Error notifications
   - Warning notifications
   - Info notifications
   - Use Material-UI Snackbar

6. **Implement Loading States**:
   - Loading spinners for API calls
   - Skeleton screens for data loading
   - Disable buttons during submission
   - Progress indicators

7. **Implement Form Validation**:
   - Client-side validation
   - Real-time validation feedback
   - Display validation errors
   - Prevent invalid submissions

8. **Create 404 Page**:
   - Custom not found page
   - Navigation back to home
   - Search functionality

#### User Experience Improvements

9. **Add Confirmation Dialogs**:
   - Confirm before delete operations
   - Confirm before leaving unsaved forms
   - Confirm before logout

10. **Add Empty States**:
    - Display message when no data
    - Provide action to add data
    - Use illustrations or icons

11. **Add Search and Filter**:
    - Search functionality on list pages
    - Filter by multiple criteria
    - Clear filters button
    - Show active filters

12. **Add Pagination**:
    - Paginate large lists
    - Show page numbers
    - Show total count
    - Configurable page size

### Expected Deliverables
- Global error handling
- User-friendly error messages
- Loading states throughout app
- Form validation
- Confirmation dialogs
- Empty states
- Search and filter functionality
- Pagination

---

## Assignment 4: Performance Optimization & Deployment

### Task
Optimize application performance and deploy to production.

### Requirements

#### Backend Optimization

1. **Database Optimization**:
   - Add database indexes on frequently queried fields
   - Optimize N+1 queries with JOIN FETCH
   - Use pagination for large datasets
   - Implement database connection pooling

2. **Caching**:
   - Add Spring Cache annotations
   - Cache frequently accessed data
   - Configure cache eviction policies
   - Use Redis for distributed caching (optional)

3. **API Optimization**:
   - Implement compression (Gzip)
   - Add ETag support for caching
   - Optimize JSON serialization
   - Use DTOs to reduce payload size

#### Frontend Optimization

4. **Code Splitting**:
   - Use React.lazy for route-based splitting
   - Lazy load heavy components
   - Measure bundle size reduction

5. **Image Optimization**:
   - Compress images
   - Use appropriate formats (WebP)
   - Implement lazy loading for images
   - Use responsive images

6. **Build Optimization**:
   - Minify CSS and JavaScript
   - Remove unused code
   - Optimize dependencies
   - Generate source maps for production

#### Docker & Deployment

7. **Create Production Dockerfiles**:
   - Multi-stage build for frontend
   - Optimize image size
   - Use appropriate base images
   - Configure health checks

8. **Update Docker Compose**:
   - Add PostgreSQL service
   - Add Nginx reverse proxy
   - Configure volumes for persistence
   - Set environment variables

9. **Configure Nginx**:
   - Reverse proxy to backend
   - Serve frontend static files
   - Enable Gzip compression
   - Configure caching headers
   - Add SSL/TLS (optional)

10. **Create Deployment Scripts**:
    - `deploy.sh` - Deploy to production
    - `backup.sh` - Backup database
    - `rollback.sh` - Rollback to previous version

#### Monitoring & Logging

11. **Add Logging**:
    - Configure Logback
    - Log all API requests
    - Log errors with stack traces
    - Use different log levels

12. **Add Health Checks**:
    - `/actuator/health` endpoint
    - Database connectivity check
    - Disk space check
    - Custom health indicators

13. **Add Metrics** (optional):
    - Use Spring Boot Actuator
    - Expose metrics endpoint
    - Monitor API response times
    - Track error rates

### Expected Deliverables
- Optimized backend with caching
- Optimized frontend with code splitting
- Production-ready Docker setup
- Nginx configuration
- Deployment scripts
- Health checks and monitoring
- Performance improvement documentation

---

## Bonus Challenges (Optional)

### Challenge 1: Real-time Features
Implement WebSocket for real-time updates:
- Real-time notifications
- Live enrollment updates
- Chat functionality
- Online user presence

### Challenge 2: Advanced Search
Implement Elasticsearch for advanced search:
- Full-text search across entities
- Fuzzy matching
- Search suggestions
- Search analytics

### Challenge 3: File Upload
Implement file upload functionality:
- Profile picture upload
- Document upload
- Image preview
- File validation
- Cloud storage integration (AWS S3)

### Challenge 4: Email Notifications
Implement email notifications:
- Welcome email on registration
- Password reset email
- Enrollment confirmation
- Weekly digest
- Use SendGrid or AWS SES

### Challenge 5: Analytics Dashboard
Create an analytics dashboard:
- Student enrollment trends
- Course popularity
- Teacher performance metrics
- Interactive charts with Chart.js
- Export reports to PDF

---

## Running the Complete Application

### Local Development

```bash
# Start backend
mvn spring-boot:run

# Start frontend (new terminal)
cd online-school-frontend
npm run dev

# Access application
# Frontend: http://localhost:3000
# Backend: http://localhost:8080/api
```

### Docker Deployment

```bash
# Build and start all services
docker-compose up --build

# Run in background
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

### Testing

```bash
# Backend tests
mvn test

# Frontend tests
cd online-school-frontend
npm test

# Integration tests
mvn verify

# E2E tests
npm run test:e2e
```

---

## Submission Checklist

Before submitting, ensure you have:

- [ ] Complete teacher management feature (Assignment 1)
- [ ] Authentication and authorization working (Assignment 2)
- [ ] Error handling throughout application (Assignment 3)
- [ ] Performance optimizations implemented (Assignment 4)
- [ ] All tests passing (minimum 122 tests)
- [ ] Docker setup working
- [ ] Application deployable
- [ ] Documentation complete
- [ ] Code follows best practices
- [ ] No console errors or warnings
- [ ] Responsive design working
- [ ] All features accessible and functional

### Expected Test Results

```bash
# Backend
[INFO] Tests run: 70+, Failures: 0, Errors: 0, Skipped: 0

# Frontend
Test Suites: 15 passed, 15 total
Tests:       52 passed, 52 total

# Total: 122+ tests passing
```

---

## Documentation Requirements

Create the following documentation:

1. **FEATURE_DOCUMENTATION.md**:
   - Description of teacher management feature
   - API endpoints documentation
   - Component hierarchy
   - Data flow diagrams

2. **DEPLOYMENT_GUIDE.md**:
   - Step-by-step deployment instructions
   - Environment variables
   - Database setup
   - Troubleshooting guide

3. **USER_GUIDE.md**:
   - How to use the application
   - Screenshots of key features
   - Common workflows
   - FAQ section

4. **DEVELOPER_GUIDE.md**:
   - Project structure
   - Development setup
   - Coding standards
   - Testing guidelines
   - Contributing guidelines

---

## Evaluation Criteria

Your capstone project will be evaluated on:

1. **Functionality (30%)**
   - All features working correctly
   - No critical bugs
   - Meets all requirements

2. **Code Quality (25%)**
   - Clean, readable code
   - Follows best practices
   - Proper error handling
   - Good naming conventions

3. **Testing (20%)**
   - Comprehensive test coverage
   - All tests passing
   - Good test organization

4. **User Experience (15%)**
   - Intuitive interface
   - Responsive design
   - Good error messages
   - Loading states

5. **Documentation (10%)**
   - Complete and clear
   - Well-organized
   - Includes examples
   - Up-to-date

---

## Tips for Success

1. **Start with backend** - Get API working before frontend
2. **Test incrementally** - Test each feature as you build it
3. **Use Git branches** - Create feature branches for each assignment
4. **Commit frequently** - Small, focused commits with clear messages
5. **Review requirements** - Check requirements before starting each assignment
6. **Ask for help** - Don't hesitate to ask questions
7. **Document as you go** - Write documentation while building
8. **Test on different devices** - Ensure responsive design works
9. **Optimize early** - Don't wait until the end to optimize
10. **Plan your time** - This is a large project, plan accordingly

---

## Common Pitfalls to Avoid

- ❌ Not testing authentication thoroughly
- ❌ Forgetting to handle errors in frontend
- ❌ Not implementing loading states
- ❌ Hardcoding configuration values
- ❌ Not using environment variables
- ❌ Skipping documentation
- ❌ Not testing on different browsers
- ❌ Ignoring performance optimization
- ❌ Not implementing proper validation
- ❌ Forgetting to test Docker setup

---

## Resources

### Documentation
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [React Documentation](https://react.dev)
- [Material-UI Documentation](https://mui.com)
- [Docker Documentation](https://docs.docker.com)
- [JWT Documentation](https://jwt.io)

### Tools
- [Postman](https://www.postman.com) - API testing
- [React DevTools](https://react.dev/learn/react-developer-tools) - React debugging
- [Docker Desktop](https://www.docker.com/products/docker-desktop) - Container management

### Previous Labs
- Review all previous lab materials
- Reference example code from Labs 1-9
- Use patterns learned throughout bootcamp

---

## Questions and Support

If you encounter issues:

1. **Review lab materials** - Check previous labs for examples
2. **Check documentation** - Read official docs for libraries
3. **Debug systematically** - Use browser/IDE debuggers
4. **Check logs** - Review console and server logs
5. **Test in isolation** - Test components/services individually
6. **Ask for help** - Reach out during office hours

---

**Congratulations on reaching the final lab! This capstone project demonstrates everything you've learned. Take your time, build something great, and be proud of your accomplishment!** 🎓🚀
