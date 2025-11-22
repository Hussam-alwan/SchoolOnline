-- Lab 4: Database & SQL
-- Sample data for testing

-- Insert sample students
INSERT INTO students (student_id, name, email, gpa, enrollment_date, created_at, updated_at) VALUES
('STU001', 'Alice Johnson', 'alice@school.edu', 3.8, '2023-09-01', NOW(), NOW()),
('STU002', 'Bob Smith', 'bob@school.edu', 3.5, '2023-09-01', NOW(), NOW()),
('STU003', 'Charlie Brown', 'charlie@school.edu', 3.9, '2023-09-01', NOW(), NOW()),
('STU004', 'Diana Prince', 'diana@school.edu', 4.0, '2023-09-01', NOW(), NOW()),
('STU005', 'Eve Wilson', 'eve@school.edu', 3.2, '2023-09-01', NOW(), NOW());

-- Insert sample courses
INSERT INTO courses (course_id, course_name, credits, instructor, max_students, enrolled_students, created_at, updated_at) VALUES
('CS101', 'Introduction to Java', 3, 'Dr. Smith', 30, 5, NOW(), NOW()),
('CS102', 'Advanced Java', 4, 'Dr. Brown', 25, 3, NOW(), NOW()),
('CS103', 'Web Development', 3, 'Dr. Johnson', 20, 2, NOW(), NOW()),
('CS104', 'Database Design', 3, 'Dr. Williams', 25, 4, NOW(), NOW()),
('CS105', 'Data Structures', 4, 'Dr. Davis', 30, 6, NOW(), NOW());
