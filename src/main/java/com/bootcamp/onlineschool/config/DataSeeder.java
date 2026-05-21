package com.bootcamp.onlineschool.config;

import com.bootcamp.onlineschool.entity.*;
import com.bootcamp.onlineschool.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final ClazzRepository clazzRepository;
    private final RegistrationRepository registrationRepository;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(TeacherRepository teacherRepository,
                      StudentRepository studentRepository,
                      CourseRepository courseRepository,
                      ClazzRepository clazzRepository,
                      RegistrationRepository registrationRepository,
                      AppUserRepository appUserRepository,
                      PasswordEncoder passwordEncoder) {
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.clazzRepository = clazzRepository;
        this.registrationRepository = registrationRepository;
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        seedAuthUsers();
        seedSchoolData();
    }

    private void seedAuthUsers() {
        if (appUserRepository.count() > 0) {
            log.info("DataSeeder: auth users already present, skipping");
            return;
        }
        appUserRepository.save(new AppUser("admin", passwordEncoder.encode("admin123"),
                "admin@school.edu", Role.ADMIN));
        appUserRepository.save(new AppUser("teacher", passwordEncoder.encode("teacher123"),
                "teacher@school.edu", Role.TEACHER));
        appUserRepository.save(new AppUser("student", passwordEncoder.encode("student123"),
                "student@school.edu", Role.STUDENT));
        log.info("DataSeeder: seeded 3 demo auth users (admin/admin123, teacher/teacher123, student/student123)");
    }

    private void seedSchoolData() {
        if (teacherRepository.count() > 0) {
            log.info("DataSeeder: school data already present, skipping");
            return;
        }

        Teacher smith = teacherRepository.save(new Teacher(
                "Dr. John Smith", "john.smith@school.edu",
                "EMP001", "Computer Science", LocalDate.of(2020, 1, 15)));
        Teacher johnson = teacherRepository.save(new Teacher(
                "Prof. Sarah Johnson", "sarah.johnson@school.edu",
                "EMP002", "Mathematics", LocalDate.of(2019, 8, 20)));
        Teacher brown = teacherRepository.save(new Teacher(
                "Dr. Michael Brown", "michael.brown@school.edu",
                "EMP003", "Physics", LocalDate.of(2021, 3, 10)));

        Student alice = studentRepository.save(new Student(
                "Alice Wilson", "alice.wilson@student.edu",
                "STU001", LocalDate.of(2023, 9, 1)));
        Student bob = studentRepository.save(new Student(
                "Bob Davis", "bob.davis@student.edu",
                "STU002", LocalDate.of(2023, 9, 1)));
        Student carol = studentRepository.save(new Student(
                "Carol Martinez", "carol.martinez@student.edu",
                "STU003", LocalDate.of(2023, 9, 1)));
        Student david = studentRepository.save(new Student(
                "David Lee", "david.lee@student.edu",
                "STU004", LocalDate.of(2023, 9, 1)));

        Course intro = courseRepository.save(new Course(
                "Introduction to Programming",
                "Learn the basics of programming with Java", 3, 16));
        Course dsa = courseRepository.save(new Course(
                "Data Structures and Algorithms",
                "Advanced programming concepts and problem solving", 4, 16));
        Course calc = courseRepository.save(new Course(
                "Calculus I",
                "Differential and integral calculus", 4, 16));
        Course physics = courseRepository.save(new Course(
                "Physics I",
                "Mechanics and thermodynamics", 3, 16));

        Clazz cs101 = newClazz("CS-101-A", "Fall", 2023, 30, smith, Set.of(alice, bob, carol), Set.of(intro));
        Clazz cs201 = newClazz("CS-201-A", "Fall", 2023, 25, smith, Set.of(alice), Set.of(dsa));
        Clazz math101 = newClazz("MATH-101-A", "Fall", 2023, 35, johnson, Set.of(bob, carol, david), Set.of(calc));
        Clazz phys101 = newClazz("PHYS-101-A", "Fall", 2023, 20, brown, Set.of(david), Set.of(physics));
        clazzRepository.saveAll(List.of(cs101, cs201, math101, phys101));

        registrationRepository.save(newReg(alice, intro));
        registrationRepository.save(newReg(bob, intro));
        registrationRepository.save(newReg(carol, intro));
        registrationRepository.save(newReg(alice, dsa));
        registrationRepository.save(newReg(bob, calc));
        registrationRepository.save(newReg(carol, calc));
        registrationRepository.save(newReg(david, calc));
        registrationRepository.save(newReg(david, physics));

        log.info("DataSeeder: seeded 3 teachers, 4 students, 4 courses, 4 classes, 8 registrations");
    }

    private Clazz newClazz(String name, String semester, int year, int capacity,
                           Teacher teacher, Set<Student> students, Set<Course> courses) {
        Clazz c = new Clazz();
        c.setName(name);
        c.setSemester(semester);
        c.setYear(year);
        c.setMaxCapacity(capacity);
        c.setTeacher(teacher);
        c.setStudents(new HashSet<>(students));
        c.setCourses(new HashSet<>(courses));
        return c;
    }

    private Registration newReg(Student student, Course course) {
        return new Registration(LocalDate.of(2023, 9, 1), "ACTIVE", student, course);
    }
}
