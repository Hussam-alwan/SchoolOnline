-- Test Data for Online School Backend
-- This file contains minimal test data for unit and integration tests

-- Insert test Teachers (TABLE_PER_CLASS inheritance)
INSERT INTO teachers (id, name, email, created_at, updated_at, employee_id, department, hire_date) VALUES 
(100, 'Test Teacher', 'test.teacher@test.edu', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'TEST001', 'Test Department', '2023-01-01');

-- Insert test Students (TABLE_PER_CLASS inheritance)
INSERT INTO students (id, name, email, created_at, updated_at, student_id, enrollment_date) VALUES 
(101, 'Test Student', 'test.student@test.edu', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'TESTSTU001', '2023-09-01');

-- Insert test Course
INSERT INTO courses (id, name, description, credits, duration, created_at, updated_at) VALUES 
(100, 'Test Course', 'A course for testing purposes', 3, 16, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert test Class
INSERT INTO class_schedule (id, name, semester, class_year, max_capacity, teacher_id, created_at, updated_at) VALUES 
(100, 'TEST-101', 'Fall', 2023, 20, 100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert test Registration
INSERT INTO registrations (id, registration_date, status, grade, student_id, course_id, created_at, updated_at) VALUES 
(100, '2023-09-01', 'ENROLLED', NULL, 101, 100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);