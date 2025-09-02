package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.dto.ClazzDTO;
import com.bootcamp.onlineschool.entity.Clazz;
import com.bootcamp.onlineschool.entity.Teacher;
import com.bootcamp.onlineschool.entity.Student;
import com.bootcamp.onlineschool.entity.Course;
import com.bootcamp.onlineschool.exception.ResourceNotFoundException;
import com.bootcamp.onlineschool.exception.ValidationException;
import com.bootcamp.onlineschool.repository.ClazzRepository;
import com.bootcamp.onlineschool.repository.TeacherRepository;
import com.bootcamp.onlineschool.repository.StudentRepository;
import com.bootcamp.onlineschool.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing Clazz entities
 * Provides CRUD operations, relationship management, and business logic validation
 */
@Service
@Transactional
public class ClazzService {

    private final ClazzRepository clazzRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public ClazzService(ClazzRepository clazzRepository, TeacherRepository teacherRepository,
                       StudentRepository studentRepository, CourseRepository courseRepository) {
        this.clazzRepository = clazzRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    /**
     * Create a new class
     * @param clazzDTO the class data
     * @return the created class
     * @throws ValidationException if validation fails
     */
    public ClazzDTO create(ClazzDTO clazzDTO) {
        validateClazzForCreation(clazzDTO);
        
        // Get the teacher
        Teacher teacher = teacherRepository.findById(clazzDTO.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", clazzDTO.getTeacherId()));
        
        Clazz clazz = clazzDTO.toEntity();
        clazz.setTeacher(teacher);
        
        Clazz savedClazz = clazzRepository.save(clazz);
        return ClazzDTO.fromEntity(savedClazz);
    }

    /**
     * Retrieve all classes
     * @return List of all classes as DTOs
     */
    @Transactional(readOnly = true)
    public List<ClazzDTO> findAll() {
        return clazzRepository.findAll().stream()
                .map(ClazzDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Find class by ID
     * @param id the class ID
     * @return the class DTO if found
     * @throws ResourceNotFoundException if class not found
     */
    @Transactional(readOnly = true)
    public ClazzDTO findById(Long id) {
        Clazz clazz = clazzRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Class", "id", id));
        return ClazzDTO.fromEntity(clazz);
    }

    /**
     * Find class by name
     * @param name the class name
     * @return Optional containing the class DTO if found
     */
    @Transactional(readOnly = true)
    public Optional<ClazzDTO> findByName(String name) {
        validateClassName(name);
        return clazzRepository.findByName(name)
                .map(ClazzDTO::fromEntity);
    }

    /**
     * Find classes by semester and year
     * @param semester the semester
     * @param year the year
     * @return List of classes in the specified semester and year
     */
    @Transactional(readOnly = true)
    public List<ClazzDTO> findBySemesterAndYear(String semester, Integer year) {
        validateSemester(semester);
        validateYear(year);
        return clazzRepository.findClassesBySemesterAndYear(semester, year).stream()
                .map(ClazzDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Find classes by teacher ID
     * @param teacherId the teacher ID
     * @return List of classes taught by the teacher
     */
    @Transactional(readOnly = true)
    public List<ClazzDTO> findByTeacherId(Long teacherId) {
        if (teacherId == null) {
            throw new ValidationException("Teacher ID cannot be null");
        }
        return clazzRepository.findByTeacherId(teacherId).stream()
                .map(ClazzDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Update an existing class
     * @param id the class ID
     * @param clazzDTO the updated class data
     * @return the updated class DTO
     * @throws ResourceNotFoundException if class not found
     * @throws ValidationException if validation fails
     */
    public ClazzDTO update(Long id, ClazzDTO clazzDTO) {
        Clazz existingClazz = clazzRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Class", "id", id));
        
        validateClazzForUpdate(clazzDTO, existingClazz);
        
        // Update fields
        existingClazz.setName(clazzDTO.getName());
        existingClazz.setSemester(clazzDTO.getSemester());
        existingClazz.setYear(clazzDTO.getYear());
        existingClazz.setMaxCapacity(clazzDTO.getMaxCapacity());
        
        // Update teacher if changed
        if (!existingClazz.getTeacher().getId().equals(clazzDTO.getTeacherId())) {
            Teacher newTeacher = teacherRepository.findById(clazzDTO.getTeacherId())
                    .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", clazzDTO.getTeacherId()));
            existingClazz.setTeacher(newTeacher);
        }
        
        Clazz savedClazz = clazzRepository.save(existingClazz);
        return ClazzDTO.fromEntity(savedClazz);
    }

    /**
     * Delete class by ID
     * @param id the class ID
     * @throws ResourceNotFoundException if class not found
     * @throws ValidationException if class has students or courses
     */
    public void deleteById(Long id) {
        Clazz clazz = clazzRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Class", "id", id));
        
        // Check if class has students
        if (!clazz.getStudents().isEmpty()) {
            throw new ValidationException("Cannot delete class with enrolled students. Please remove students first.");
        }
        
        // Check if class has courses
        if (!clazz.getCourses().isEmpty()) {
            throw new ValidationException("Cannot delete class with assigned courses. Please remove courses first.");
        }
        
        clazzRepository.delete(clazz);
    }

    /**
     * Add student to class
     * @param clazzId the class ID
     * @param studentId the student ID
     * @return the updated class DTO
     * @throws ResourceNotFoundException if class or student not found
     * @throws ValidationException if enrollment validation fails
     */
    public ClazzDTO addStudent(Long clazzId, Long studentId) {
        Clazz clazz = clazzRepository.findById(clazzId)
                .orElseThrow(() -> new ResourceNotFoundException("Class", "id", clazzId));
        
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));
        
        validateStudentEnrollment(clazz, student);
        
        clazz.addStudent(student);
        Clazz savedClazz = clazzRepository.save(clazz);
        return ClazzDTO.fromEntity(savedClazz);
    }

    /**
     * Remove student from class
     * @param clazzId the class ID
     * @param studentId the student ID
     * @return the updated class DTO
     * @throws ResourceNotFoundException if class or student not found
     */
    public ClazzDTO removeStudent(Long clazzId, Long studentId) {
        Clazz clazz = clazzRepository.findById(clazzId)
                .orElseThrow(() -> new ResourceNotFoundException("Class", "id", clazzId));
        
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));
        
        clazz.removeStudent(student);
        Clazz savedClazz = clazzRepository.save(clazz);
        return ClazzDTO.fromEntity(savedClazz);
    }

    /**
     * Add course to class
     * @param clazzId the class ID
     * @param courseId the course ID
     * @return the updated class DTO
     * @throws ResourceNotFoundException if class or course not found
     * @throws ValidationException if course assignment validation fails
     */
    public ClazzDTO addCourse(Long clazzId, Long courseId) {
        Clazz clazz = clazzRepository.findById(clazzId)
                .orElseThrow(() -> new ResourceNotFoundException("Class", "id", clazzId));
        
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));
        
        validateCourseAssignment(clazz, course);
        
        clazz.addCourse(course);
        Clazz savedClazz = clazzRepository.save(clazz);
        return ClazzDTO.fromEntity(savedClazz);
    }

    /**
     * Remove course from class
     * @param clazzId the class ID
     * @param courseId the course ID
     * @return the updated class DTO
     * @throws ResourceNotFoundException if class or course not found
     */
    public ClazzDTO removeCourse(Long clazzId, Long courseId) {
        Clazz clazz = clazzRepository.findById(clazzId)
                .orElseThrow(() -> new ResourceNotFoundException("Class", "id", clazzId));
        
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));
        
        clazz.removeCourse(course);
        Clazz savedClazz = clazzRepository.save(clazz);
        return ClazzDTO.fromEntity(savedClazz);
    }

    /**
     * Check if class exists by name
     * @param name the class name
     * @return true if class exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        validateClassName(name);
        return clazzRepository.existsByName(name);
    }

    // Private validation methods

    private void validateClazzForCreation(ClazzDTO clazzDTO) {
        if (clazzDTO == null) {
            throw new ValidationException("Class data cannot be null");
        }

        validateRequiredFields(clazzDTO);

        // Check for duplicate class name
        if (clazzRepository.existsByName(clazzDTO.getName())) {
            throw new ValidationException("Class name already exists: " + clazzDTO.getName());
        }

        // Validate teacher exists
        if (clazzDTO.getTeacherId() == null) {
            throw new ValidationException("Teacher ID is required");
        }
    }

    private void validateClazzForUpdate(ClazzDTO clazzDTO, Clazz existingClazz) {
        if (clazzDTO == null) {
            throw new ValidationException("Class data cannot be null");
        }

        validateRequiredFields(clazzDTO);

        // Check if name is being changed to an existing one
        if (!existingClazz.getName().equals(clazzDTO.getName())) {
            if (clazzRepository.existsByName(clazzDTO.getName())) {
                throw new ValidationException("Class name already exists: " + clazzDTO.getName());
            }
        }

        // Validate teacher exists
        if (clazzDTO.getTeacherId() == null) {
            throw new ValidationException("Teacher ID is required");
        }
    }

    private void validateRequiredFields(ClazzDTO clazzDTO) {
        if (clazzDTO.getName() == null || clazzDTO.getName().trim().isEmpty()) {
            throw new ValidationException("Class name is required");
        }

        if (clazzDTO.getSemester() == null || clazzDTO.getSemester().trim().isEmpty()) {
            throw new ValidationException("Semester is required");
        }

        if (clazzDTO.getYear() == null) {
            throw new ValidationException("Year is required");
        }

        if (clazzDTO.getMaxCapacity() == null) {
            throw new ValidationException("Max capacity is required");
        }

        validateYear(clazzDTO.getYear());
        validateMaxCapacity(clazzDTO.getMaxCapacity());
    }

    private void validateClassName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Class name cannot be null or empty");
        }
    }

    private void validateSemester(String semester) {
        if (semester == null || semester.trim().isEmpty()) {
            throw new ValidationException("Semester cannot be null or empty");
        }
    }

    private void validateYear(Integer year) {
        if (year == null) {
            throw new ValidationException("Year cannot be null");
        }
        if (year < 2000) {
            throw new ValidationException("Year must be at least 2000");
        }
    }

    private void validateMaxCapacity(Integer maxCapacity) {
        if (maxCapacity == null) {
            throw new ValidationException("Max capacity cannot be null");
        }
        if (maxCapacity < 1) {
            throw new ValidationException("Max capacity must be at least 1");
        }
    }

    private void validateStudentEnrollment(Clazz clazz, Student student) {
        // Check if student is already enrolled
        if (clazz.getStudents().contains(student)) {
            throw new ValidationException("Student is already enrolled in class: " + clazz.getName());
        }

        // Check class capacity
        if (clazz.getStudents().size() >= clazz.getMaxCapacity()) {
            throw new ValidationException("Class has reached maximum capacity: " + clazz.getMaxCapacity());
        }
    }

    private void validateCourseAssignment(Clazz clazz, Course course) {
        // Check if course is already assigned to the class
        if (clazz.getCourses().contains(course)) {
            throw new ValidationException("Course is already assigned to class: " + clazz.getName());
        }
    }
}