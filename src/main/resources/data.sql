-- Sample Data for Online School Backend
-- This file contains sample data for development and testing

-- Insert sample Teachers
INSERT INTO teachers (id, name, email, created_at, updated_at, employee_id, department, hire_date) VALUES 
(1, 'Dr. John Smith', 'john.smith@school.edu', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'EMP001', 'Computer Science', '2020-01-15'),
(2, 'Prof. Sarah Johnson', 'sarah.johnson@school.edu', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'EMP002', 'Mathematics', '2019-08-20'),
(3, 'Dr. Michael Brown', 'michael.brown@school.edu', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'EMP003', 'Physics', '2021-03-10');

-- Insert sample Students
INSERT INTO students (id, name, email, created_at, updated_at, student_id, enrollment_date) VALUES 
(4, 'Alice Wilson', 'alice.wilson@student.edu', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'STU001', '2023-09-01'),
(5, 'Bob Davis', 'bob.davis@student.edu', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'STU002', '2023-09-01'),
(6, 'Carol Martinez', 'carol.martinez@student.edu', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'STU003', '2023-09-01'),
(7, 'David Lee', 'david.lee@student.edu', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'STU004', '2023-09-01');

-- Insert sample Courses
INSERT INTO courses (id, name, description, credits, duration, created_at, updated_at) VALUES 
(1, 'Introduction to Programming', 'Learn the basics of programming with Java', 3, 16, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Data Structures and Algorithms', 'Advanced programming concepts and problem solving', 4, 16, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Calculus I', 'Differential and integral calculus', 4, 16, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'Physics I', 'Mechanics and thermodynamics', 3, 16, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample Classes
INSERT INTO class_schedule (id, name, semester, class_year, max_capacity, teacher_id, created_at, updated_at) VALUES 
(1, 'CS-101-A', 'Fall', 2023, 30, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'CS-201-A', 'Fall', 2023, 25, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'MATH-101-A', 'Fall', 2023, 35, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'PHYS-101-A', 'Fall', 2023, 20, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Link Courses to Classes
INSERT INTO class_courses (class_id, course_id) VALUES 
(1, 1), -- CS-101-A teaches Introduction to Programming
(2, 2), -- CS-201-A teaches Data Structures and Algorithms
(3, 3), -- MATH-101-A teaches Calculus I
(4, 4); -- PHYS-101-A teaches Physics I

-- Enroll Students in Classes
INSERT INTO class_students (class_id, student_id) VALUES 
(1, 4), -- Alice in CS-101-A
(1, 5), -- Bob in CS-101-A
(1, 6), -- Carol in CS-101-A
(2, 4), -- Alice in CS-201-A
(3, 5), -- Bob in MATH-101-A
(3, 6), -- Carol in MATH-101-A
(3, 7), -- David in MATH-101-A
(4, 7); -- David in PHYS-101-A

-- Insert sample Registrations
INSERT INTO registrations (id, registration_date, status, grade, student_id, course_id, created_at, updated_at) VALUES 
(1, '2023-09-01', 'ENROLLED', NULL, 4, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- Alice -> Introduction to Programming
(2, '2023-09-01', 'ENROLLED', NULL, 5, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- Bob -> Introduction to Programming
(3, '2023-09-01', 'ENROLLED', NULL, 6, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- Carol -> Introduction to Programming
(4, '2023-09-01', 'ENROLLED', NULL, 4, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- Alice -> Data Structures and Algorithms
(5, '2023-09-01', 'ENROLLED', NULL, 5, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- Bob -> Calculus I
(6, '2023-09-01', 'ENROLLED', NULL, 6, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- Carol -> Calculus I
(7, '2023-09-01', 'ENROLLED', NULL, 7, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- David -> Calculus I
(8, '2023-09-01', 'ENROLLED', NULL, 7, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP); -- David -> Physics I