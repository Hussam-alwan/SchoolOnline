package com.bootcamp.onlineschool;

import com.bootcamp.onlineschool.model.Student;
import com.bootcamp.onlineschool.repository.StudentRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Component
@Transactional
public class StudentRegistry {

    private final StudentRepository studentRepository;

    public StudentRegistry(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public void addStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        if (!student.isValidEmail()) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (student.getStudentId() != null
                && studentRepository.existsByStudentId(student.getStudentId())) {
            throw new IllegalArgumentException(
                    "Student with id already exists: " + student.getStudentId());
        }
        studentRepository.save(student);
    }

    public boolean removeStudent(String studentId) {
        return studentRepository.findByStudentId(studentId)
                .map(s -> {
                    studentRepository.delete(s);
                    return true;
                })
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public Student findStudentById(String studentId) {
        return studentRepository.findByStudentId(studentId).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Student> findStudentsByName(String name) {
        return studentRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional(readOnly = true)
    public List<Student> getAllStudentsSortedByName() {
        return studentRepository.findAll().stream()
                .sorted(Comparator.comparing(Student::getName))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Student> getAllStudentsSortedByGpa() {
        return studentRepository.findAll().stream()
                .sorted(Comparator.comparingDouble(Student::getGpa).reversed())
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Student> getStudentsWithHighGpa(double threshold) {
        return studentRepository.findByGpaGreaterThanEqual(threshold);
    }

    @Transactional(readOnly = true)
    public int getStudentCount() {
        return (int) studentRepository.count();
    }

    @Transactional(readOnly = true)
    public double getAverageGpa() {
        Double avg = studentRepository.getAverageGpa();
        return avg == null ? 0.0 : avg;
    }

    public void clear() {
        studentRepository.deleteAll();
    }
}
