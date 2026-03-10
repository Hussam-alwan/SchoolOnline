# Lab 5: ORM & JPA - Homework Assignment

## Overview

This homework assignment reinforces the JPA, Hibernate, and entity relationship concepts you learned in Lab 5. You'll practice entity mapping, relationship management, repository development, and transaction handling.

## Learning Objectives

By completing this homework, you will:
- Create JPA entities with proper annotations and relationships
- Implement One-to-Many, Many-to-One, and Many-to-Many relationships
- Develop Spring Data JPA repositories with custom queries
- Apply cascade operations and transaction management
- Use validation annotations to enforce data integrity
- Write comprehensive tests with @DataJpaTest
- Handle entity lifecycle and lazy loading

## Prerequisites

- Completed Lab 5: ORM & JPA
- Understanding of JPA annotations and entity relationships
- Familiarity with Spring Data JPA repositories
- Knowledge of JPQL and query methods
- Understanding of transaction management

---

## Assignment 1: Teacher Entity with One-to-Many Relationship

### Task
Create a `Teacher` entity with a one-to-many relationship to `Course` entities.

### Requirements

1. **Create Teacher entity** in `src/main/java/com/bootcamp/onlineschool/entity/Teacher.java`:
   - Annotate with `@Entity` and `@Table(name = "teachers")`
   - Fields:
     - `id` (Long) - Primary key with `@GeneratedValue(strategy = GenerationType.IDENTITY)`
     - `name` (String) - Not null, max 100 chars, with `@NotNull` and `@Size`
     - `email` (String) - Not null, unique, with `@Email` validation
     - `department` (String) - Not null, max 50 chars
     - `yearsOfExperience` (Integer) - Not null, min 0 with `@Min(0)`
     - `salary` (Double) - Not null, min 0 with `@Min(0)`
     - `courses` (Set<Course>) - One-to-many relationship
   - Implement one-to-many relationship:
     - Use `@OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)`
     - Initialize as `new HashSet<>()`
   - Add helper methods:
     - `addCourse(Course)` - Add course and set bidirectional relationship
     - `removeCourse(Course)` - Remove course and clear bidirectional relationship
   - Override `equals()` and `hashCode()` based on email
   - Override `toString()` (exclude courses to avoid circular reference)

2. **Update Course entity** to add Many-to-One relationship:
   - Add field: `teacher` (Teacher) - Many-to-one relationship
   - Annotate with `@ManyToOne(fetch = FetchType.LAZY)` and `@JoinColumn(name = "teacher_id")`
   - Update `toString()` to avoid circular reference

3. **Create TeacherRepository** in `src/main/java/com/bootcamp/onlineschool/repository/TeacherRepository.java`:
   - Extend `JpaRepository<Teacher, Long>`
   - Add query methods:
     - `Optional<Teacher> findByEmail(String email)`
     - `List<Teacher> findByDepartment(String department)`
     - `List<Teacher> findByYearsOfExperienceGreaterThanEqual(Integer years)`
     - `List<Teacher> findBySalaryBetween(Double min, Double max)`
   - Add JPQL queries:
     - `@Query("SELECT t FROM Teacher t LEFT JOIN FETCH t.courses WHERE t.id = :id")` for `findByIdWithCourses`
     - `@Query("SELECT t FROM Teacher t WHERE SIZE(t.courses) >= :minCourses")` for `findTeachersWithMinCourses`
     - `@Query("SELECT AVG(t.salary) FROM Teacher t WHERE t.department = :dept")` for `getAverageSalaryByDepartment`

4. **Create TeacherService** in `src/main/java/com/bootcamp/onlineschool/service/TeacherService.java`:
   - Annotate with `@Service` and `@Transactional`
   - Inject TeacherRepository and CourseRepository
   - Implement methods:
     - `createTeacher(TeacherDTO)` - Create new teacher
     - `getTeacherById(Long)` - Find by ID with exception handling
     - `assignCourseToTeacher(Long teacherId, Long courseId)` - Manage relationship
     - `removeCourseFromTeacher(Long teacherId, Long courseId)` - Remove relationship
     - `getTeacherWithCourses(Long)` - Fetch with courses (use JOIN FETCH)
     - `deleteTeacher(Long)` - Delete with cascade handling

5. **Create test classes**:
   - `TeacherEntityTest.java` - Test entity validation and relationships (minimum 8 tests)
   - `TeacherRepositoryTest.java` - Test repository methods (minimum 12 tests)
   - `TeacherServiceTest.java` - Test service layer with @SpringBootTest (minimum 10 tests)

### Expected Deliverables
- Teacher.java (entity with one-to-many relationship)
- Updated Course.java (with many-to-one relationship)
- TeacherRepository.java (with custom queries)
- TeacherService.java (with transaction management)
- TeacherEntityTest.java (minimum 8 tests)
- TeacherRepositoryTest.java (minimum 12 tests)
- TeacherServiceTest.java (minimum 10 tests)

---

## Assignment 2: Department Entity with Bidirectional Relationships

### Task
Create a `Department` entity with bidirectional one-to-many relationships to both `Teacher` and `Course`.

### Requirements

1. **Create Department entity** in `src/main/java/com/bootcamp/onlineschool/entity/Department.java`:
   - Annotate with `@Entity` and `@Table(name = "departments")`
   - Fields:
     - `id` (Long) - Primary key with auto-generation
     - `name` (String) - Not null, unique, max 100 chars
     - `code` (String) - Not null, unique, max 10 chars
     - `budget` (Double) - Not null, min 0
     - `location` (String) - Max 100 chars
     - `teachers` (Set<Teacher>) - One-to-many relationship
     - `courses` (Set<Course>) - One-to-many relationship
   - Implement relationships:
     - `@OneToMany(mappedBy = "department", cascade = CascadeType.ALL)`
   - Add helper methods for managing relationships
   - Add business methods: `increaseBudget()`, `decreaseBudget()`, `getTeacherCount()`, `getCourseCount()`

2. **Update Teacher entity**:
   - Add field: `department` (Department)
   - Annotate with `@ManyToOne(fetch = FetchType.LAZY)` and `@JoinColumn(name = "department_id")`

3. **Update Course entity**:
   - Add field: `department` (Department)
   - Annotate with `@ManyToOne(fetch = FetchType.LAZY)` and `@JoinColumn(name = "department_id")`

4. **Create DepartmentRepository**:
   - Extend `JpaRepository<Department, Long>`
   - Add query methods:
     - `Optional<Department> findByCode(String code)`
     - `Optional<Department> findByName(String name)`
     - `List<Department> findByBudgetGreaterThan(Double budget)`
   - Add JPQL queries:
     - Find department with all teachers (JOIN FETCH)
     - Find department with all courses (JOIN FETCH)
     - Count teachers by department
     - Calculate total budget across all departments

5. **Create DepartmentService**:
   - Implement CRUD operations
   - Implement methods to assign/remove teachers and courses
   - Handle cascade operations properly
   - Use `@Transactional` appropriately

6. **Create test classes**:
   - `DepartmentEntityTest.java` (minimum 10 tests)
   - `DepartmentRepositoryTest.java` (minimum 12 tests)
   - `DepartmentServiceTest.java` (minimum 10 tests)

### Expected Deliverables
- Department.java (entity with bidirectional relationships)
- Updated Teacher.java and Course.java
- DepartmentRepository.java
- DepartmentService.java
- Test classes (minimum 32 tests total)

---

## Assignment 3: Enrollment Entity with Many-to-Many Join Table

### Task
Create an `Enrollment` entity to represent the many-to-many relationship between students and courses with additional attributes.

### Requirements

1. **Create Enrollment entity** in `src/main/java/com/bootcamp/onlineschool/entity/Enrollment.java`:
   - Annotate with `@Entity` and `@Table(name = "enrollments")`
   - Use composite key or separate ID
   - Fields:
     - `id` (Long) - Primary key
     - `student` (Student) - Many-to-one relationship
     - `course` (Course) - Many-to-one relationship
     - `enrollmentDate` (LocalDate) - Not null
     - `grade` (String) - Max 2 chars (A, B, C, D, F)
     - `status` (EnrollmentStatus) - Enum (ENROLLED, COMPLETED, DROPPED, WITHDRAWN)
     - `completionDate` (LocalDate) - Nullable
   - Implement relationships:
     - `@ManyToOne` for both student and course
     - `@JoinColumn` for foreign keys
   - Add validation annotations
   - Create EnrollmentStatus enum

2. **Update Student entity**:
   - Replace `@ManyToMany` with `@OneToMany(mappedBy = "student")`
   - Change field to `Set<Enrollment> enrollments`
   - Add helper methods to work with enrollments

3. **Update Course entity**:
   - Replace `@ManyToMany` with `@OneToMany(mappedBy = "course")`
   - Change field to `Set<Enrollment> enrollments`
   - Add helper methods to work with enrollments

4. **Create EnrollmentRepository**:
   - Extend `JpaRepository<Enrollment, Long>`
   - Add query methods:
     - `List<Enrollment> findByStudent_Id(Long studentId)`
     - `List<Enrollment> findByCourse_Id(Long courseId)`
     - `List<Enrollment> findByStatus(EnrollmentStatus status)`
     - `Optional<Enrollment> findByStudent_IdAndCourse_Id(Long studentId, Long courseId)`
   - Add JPQL queries:
     - Find enrollments by date range
     - Find enrollments by grade
     - Count enrollments by status
     - Find students enrolled in a course with specific status

5. **Create EnrollmentService**:
   - Implement enrollment operations:
     - `enrollStudent(Long studentId, Long courseId)` - Create enrollment
     - `dropCourse(Long enrollmentId)` - Update status to DROPPED
     - `completeEnrollment(Long enrollmentId, String grade)` - Mark as completed
     - `getStudentEnrollments(Long studentId)` - Get all enrollments for student
     - `getCourseEnrollments(Long courseId)` - Get all enrollments for course
   - Use `@Transactional` for all operations
   - Validate business rules (e.g., can't enroll in same course twice)

6. **Create test classes**:
   - `EnrollmentEntityTest.java` (minimum 8 tests)
   - `EnrollmentRepositoryTest.java` (minimum 12 tests)
   - `EnrollmentServiceTest.java` (minimum 12 tests)
   - `EnrollmentIntegrationTest.java` (minimum 8 tests for complex scenarios)

### Expected Deliverables
- Enrollment.java (join entity with attributes)
- EnrollmentStatus.java (enum)
- Updated Student.java and Course.java
- EnrollmentRepository.java
- EnrollmentService.java
- Test classes (minimum 40 tests total)

---

## Bonus Challenges (Optional)

### Challenge 1: Audit Fields with @EntityListeners
Implement audit fields for all entities:
- Add `createdAt`, `updatedAt`, `createdBy`, `updatedBy` fields
- Create an `@EntityListeners` class with `@PrePersist` and `@PreUpdate`
- Apply to all entities using `@MappedSuperclass`
- Write tests to verify audit fields are populated

### Challenge 2: Soft Delete Implementation
Implement soft delete for entities:
- Add `deleted` boolean field to entities
- Override delete methods to set `deleted = true` instead of removing
- Add `@Where(clause = "deleted = false")` to filter deleted entities
- Create methods to permanently delete or restore entities
- Write tests for soft delete behavior

### Challenge 3: Custom Repository Implementation
Create custom repository implementations:
- Define custom interface with advanced query methods
- Implement using `EntityManager` and Criteria API
- Create dynamic queries based on search criteria
- Integrate with Spring Data JPA repositories
- Write tests for custom repository methods

### Challenge 4: Optimistic Locking
Implement optimistic locking:
- Add `@Version` field to entities
- Handle `OptimisticLockException` in services
- Write tests to verify concurrent update handling
- Implement retry logic for failed updates

---

## Running Your Tests

### Run all tests:
```bash
mvn clean test
```

### Run specific test class:
```bash
mvn test -Dtest=TeacherRepositoryTest
mvn test -Dtest=EnrollmentServiceTest
```

### Run tests with SQL logging:
```bash
mvn test -Dspring.jpa.show-sql=true
```

### Access H2 Console:
```bash
mvn spring-boot:run
```
Then open: `http://localhost:8080/h2-console`

---

## Submission Checklist

Before submitting, ensure you have:

- [ ] All entity classes with proper JPA annotations
- [ ] All relationships properly configured (bidirectional consistency)
- [ ] All repository interfaces with custom queries
- [ ] All service classes with @Transactional
- [ ] All validation annotations applied
- [ ] All test classes with proper annotations
- [ ] All tests passing (`mvn clean test` shows 0 failures)
- [ ] Minimum test counts met (Assignment 1: 30, Assignment 2: 32, Assignment 3: 40 = 102 tests)
- [ ] Helper methods for managing bidirectional relationships
- [ ] Proper cascade and orphan removal configuration
- [ ] JavaDoc comments for all public methods
- [ ] No circular references in toString() methods

### Expected Test Results
```
[INFO] Tests run: 102+, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

## Code Quality Guidelines

### Entity Best Practices

1. **Entity with Relationship**
   ```java
   @Entity
   @Table(name = "teachers")
   public class Teacher {
       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
       private Long id;
       
       @NotNull
       @Size(min = 1, max = 100)
       @Column(nullable = false, length = 100)
       private String name;
       
       @Email
       @Column(nullable = false, unique = true)
       private String email;
       
       @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
       private Set<Course> courses = new HashSet<>();
       
       // Helper methods for bidirectional relationship
       public void addCourse(Course course) {
           courses.add(course);
           course.setTeacher(this);
       }
       
       public void removeCourse(Course course) {
           courses.remove(course);
           course.setTeacher(null);
       }
   }
   ```

2. **Repository with Custom Queries**
   ```java
   @Repository
   public interface TeacherRepository extends JpaRepository<Teacher, Long> {
       Optional<Teacher> findByEmail(String email);
       
       @Query("SELECT t FROM Teacher t LEFT JOIN FETCH t.courses WHERE t.id = :id")
       Optional<Teacher> findByIdWithCourses(@Param("id") Long id);
       
       @Query("SELECT t FROM Teacher t WHERE SIZE(t.courses) >= :minCourses")
       List<Teacher> findTeachersWithMinCourses(@Param("minCourses") int minCourses);
   }
   ```

3. **Service with Transaction Management**
   ```java
   @Service
   @Transactional
   public class TeacherService {
       private final TeacherRepository teacherRepository;
       private final CourseRepository courseRepository;
       
       public TeacherService(TeacherRepository teacherRepository, 
                           CourseRepository courseRepository) {
           this.teacherRepository = teacherRepository;
           this.courseRepository = courseRepository;
       }
       
       public void assignCourseToTeacher(Long teacherId, Long courseId) {
           Teacher teacher = teacherRepository.findById(teacherId)
               .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
           Course course = courseRepository.findById(courseId)
               .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
           
           teacher.addCourse(course);
           // No explicit save needed - managed by transaction
       }
       
       @Transactional(readOnly = true)
       public Optional<Teacher> getTeacherWithCourses(Long id) {
           return teacherRepository.findByIdWithCourses(id);
       }
   }
   ```

4. **Repository Testing**
   ```java
   @DataJpaTest
   @DisplayName("TeacherRepository Tests")
   public class TeacherRepositoryTest {
       @Autowired
       private TeacherRepository teacherRepository;
       
       @Autowired
       private CourseRepository courseRepository;
       
       @BeforeEach
       public void setUp() {
           teacherRepository.deleteAll();
           courseRepository.deleteAll();
       }
       
       @Test
       @DisplayName("Should save teacher with courses")
       public void testSaveTeacherWithCourses() {
           Teacher teacher = new Teacher();
           teacher.setName("John Doe");
           teacher.setEmail("john@school.edu");
           
           Course course = new Course();
           course.setCourseName("Math 101");
           teacher.addCourse(course);
           
           Teacher saved = teacherRepository.save(teacher);
           
           assertNotNull(saved.getId());
           assertEquals(1, saved.getCourses().size());
       }
   }
   ```

### Relationship Guidelines
- Always manage both sides of bidirectional relationships
- Use helper methods to maintain consistency
- Set `mappedBy` on the non-owning side
- Configure cascade operations carefully
- Use `orphanRemoval = true` when appropriate
- Avoid circular references in `toString()`
- Use `FetchType.LAZY` for collections
- Use JOIN FETCH for N+1 query prevention

---

## Common Pitfalls to Avoid

- ❌ Forgetting to manage both sides of bidirectional relationships
- ❌ Not using `mappedBy` on the non-owning side
- ❌ Circular references in `toString()` causing stack overflow
- ❌ Not initializing collections (use `= new HashSet<>()`)
- ❌ Using `CascadeType.ALL` without understanding implications
- ❌ Not using `@Transactional` on service methods
- ❌ Forgetting to clear repository in `@BeforeEach`
- ❌ Not handling `Optional` properly (use `orElseThrow()`)
- ❌ Lazy loading exceptions outside transaction
- ❌ Not testing cascade and orphan removal behavior

---

## Tips for Success

1. **Start with entities** - Get relationships right before repositories
2. **Test relationships** - Verify bidirectional consistency
3. **Use H2 console** - Inspect database schema and data
4. **Enable SQL logging** - See generated queries
5. **Test cascade operations** - Verify delete behavior
6. **Handle lazy loading** - Use JOIN FETCH or @Transactional
7. **Write helper methods** - Manage bidirectional relationships
8. **Test with @DataJpaTest** - Faster than @SpringBootTest
9. **Verify orphan removal** - Test entity removal from collections
10. **Check for N+1 queries** - Use JOIN FETCH to optimize

---

## Resources

### Documentation
- [JPA Specification](https://jakarta.ee/specifications/persistence/)
- [Hibernate Documentation](https://hibernate.org/orm/documentation/)
- [Spring Data JPA Reference](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Entity Relationships Guide](https://www.baeldung.com/jpa-entity-relationships)
- Lab 5 LAB_5_ORM_JPA.md - Review ORM concepts

### Example Code
- Student.java and Course.java - Many-to-many relationship examples
- StudentRepository.java - Custom query examples
- StudentService.java - Transaction management examples

### Key Concepts
- JPA Entity Mapping
- Entity Relationships (One-to-Many, Many-to-One, Many-to-Many)
- Cascade Operations and Orphan Removal
- Transaction Management
- Lazy vs Eager Loading
- JPQL and Query Methods

---

## Questions and Support

If you encounter issues:

1. **Check entity annotations** - Verify @Entity, @Table, @Column
2. **Verify relationships** - Ensure bidirectional consistency
3. **Enable SQL logging** - See what queries are generated
4. **Use H2 console** - Inspect database state
5. **Check cascade configuration** - Understand cascade behavior
6. **Test in isolation** - Use @DataJpaTest for repository tests
7. **Handle lazy loading** - Use @Transactional or JOIN FETCH
8. **Ask for help** - Reach out during office hours

---

**Remember: Mastering JPA relationships and transaction management is essential for building robust, data-driven applications!** 🚀
