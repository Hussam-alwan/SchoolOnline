# Java Fundamentals - Homework Exercises

This document contains homework exercises designed to reinforce the concepts covered in Lab 1. Complete these exercises to practice object-oriented programming, collections, streams, and unit testing.

## Beginner Level

### Exercise 1: Add Student Age Field

**Objective:** Practice adding fields with validation to an existing class.

**Tasks:**
- Add an `age` field to the Student class
- Implement validation: age must be between 16 and 100
- Update all constructors to include the age parameter
- Add getter and setter methods with proper validation
- Update the `toString()` method to include age
- Write JUnit tests covering:
  - Valid age values (16, 50, 100)
  - Invalid age values (15, 101, -5)
  - Age updates through setter

**Expected Learning:**
- Field encapsulation
- Input validation
- Constructor overloading
- Exception handling

---

### Exercise 2: Email Domain Validation

**Objective:** Enhance validation logic with more specific rules.

**Tasks:**
- Modify the `isValidEmail()` method in Student class
- Accept only emails ending with "@school.edu"
- Keep existing validation (must contain "@" and ".")
- Write JUnit tests for:
  - Valid emails: "student@school.edu", "john.doe@school.edu"
  - Invalid emails: "student@gmail.com", "student@school.com", "invalid"
- Update StudentRegistry tests to use valid email format

**Expected Learning:**
- String manipulation methods
- Boolean logic
- Test-driven development

---

### Exercise 3: Find Student by Email

**Objective:** Implement search functionality using collections.

**Tasks:**
- Add `findStudentByEmail(String email)` method to StudentRegistry
- Return the Student object if found, null otherwise
- Consider case-insensitive search
- Write JUnit tests for:
  - Finding existing student by exact email
  - Finding with different case (e.g., "JOHN@school.edu")
  - Searching for non-existent email
  - Handling null or empty email parameter

**Expected Learning:**
- Collection traversal
- String comparison
- Null handling
- Edge case testing

---

## Intermediate Level

### Exercise 4: Course Enrollment System

**Objective:** Create a new class and establish relationships between objects.

**Tasks:**
- Create a `Course` class in the model package with:
  - Fields: `courseId` (String), `name` (String), `credits` (int)
  - Constructor, getters, setters
  - `toString()`, `equals()`, and `hashCode()` methods
- Add `List<Course> enrolledCourses` field to Student class
- Implement methods in Student:
  - `enrollInCourse(Course course)` - add course to list
  - `dropCourse(String courseId)` - remove course by ID
  - `getEnrolledCourses()` - return list of courses
  - `getTotalCredits()` - sum of all enrolled course credits
- Write comprehensive JUnit tests:
  - CourseTest: test Course class functionality
  - StudentTest: test enrollment, dropping, credit calculation
  - Test duplicate enrollment prevention
  - Test dropping non-existent course

**Expected Learning:**
- Class design
- Object relationships (composition)
- List operations
- Complex object testing

---

### Exercise 5: GPA Calculator

**Objective:** Implement business logic with maps and calculations.

**Tasks:**
- Add `calculateGpa(Map<Course, String> grades)` method to Student class
- Grade mapping: A=4.0, B=3.0, C=2.0, D=1.0, F=0.0
- Calculate weighted GPA based on course credits
- Automatically update the student's GPA field
- Handle invalid grades gracefully
- Write JUnit tests for:
  - Perfect GPA (all A's)
  - Mixed grades calculation
  - Invalid grade handling
  - Empty grade map
  - Courses with different credit values

**Expected Learning:**
- Map operations
- Mathematical calculations
- Weighted averages
- Data validation

---

### Exercise 6: Student Search Filters

**Objective:** Practice streams and filtering operations.

**Tasks:**
- Add to StudentRegistry:
  - `findStudentsByGpaRange(double min, double max)` - returns students within GPA range
  - `findStudentsByEmailDomain(String domain)` - returns students with specific email domain
- Use Java Streams API for implementation
- Write JUnit tests for:
  - GPA range: inclusive boundaries, no matches, all matches
  - Email domain: exact matches, case sensitivity
  - Edge cases: min > max, null domain, empty registry

**Expected Learning:**
- Streams API
- Lambda expressions
- Filter operations
- Boundary testing

---

## Advanced Level

### Exercise 7: Teacher Class with Inheritance

**Objective:** Implement inheritance hierarchy and polymorphism.

**Tasks:**
- Create abstract `User` base class with:
  - Fields: `id` (String), `name` (String), `email` (String)
  - Abstract method: `String getRole()`
  - Common methods: getters, setters, `toString()`, `equals()`, `hashCode()`
- Refactor Student class to extend User:
  - Remove duplicate fields
  - Implement `getRole()` to return "Student"
  - Keep student-specific fields (GPA, courses)
- Create `Teacher` class extending User:
  - Additional fields: `department` (String), `List<Course> coursesTaught`
  - Implement `getRole()` to return "Teacher"
  - Methods: `assignCourse()`, `removeCourse()`, `getCoursesTaught()`
- Write tests for:
  - User polymorphism
  - Student and Teacher specific functionality
  - Inheritance behavior

**Expected Learning:**
- Abstract classes
- Inheritance
- Polymorphism
- Method overriding
- Code reuse

---

### Exercise 8: Custom Comparators

**Objective:** Master sorting with custom comparison logic.

**Tasks:**
- Create separate Comparator classes:
  - `StudentNameComparator` - case-insensitive name sorting
  - `StudentGpaNameComparator` - sort by GPA descending, then name ascending
  - `StudentEmailDomainComparator` - sort by email domain, then name
- Add methods to StudentRegistry:
  - `getAllStudentsSorted(Comparator<Student> comparator)`
  - Keep existing sorting methods
- Write JUnit tests for:
  - Each comparator with various data sets
  - Tie-breaking scenarios
  - Empty and single-element lists
  - Null handling

**Expected Learning:**
- Comparator interface
- Custom sorting logic
- Multi-field comparison
- Functional interfaces

---

### Exercise 9: Registry Statistics

**Objective:** Implement complex data analysis and aggregation.

**Tasks:**
- Add statistical methods to StudentRegistry:
  - `getGpaDistribution()` - returns `Map<String, Integer>` with counts:
    - "A" (3.7-4.0), "B" (2.7-3.69), "C" (1.7-2.69), "D" (1.0-1.69), "F" (0.0-0.99)
  - `getTopStudents(int n)` - returns top N students by GPA
  - `getStudentsByGpaPercentile(double percentile)` - returns students above given percentile (0-100)
- Use Streams API for calculations
- Write comprehensive JUnit tests:
  - Distribution with various GPA spreads
  - Top N with ties, N > total students, N = 0
  - Percentile calculations: 0th, 50th, 100th percentiles
  - Edge cases: empty registry, single student

**Expected Learning:**
- Statistical calculations
- Stream aggregations
- Map operations
- Complex filtering
- Percentile computation

---

## Submission Guidelines

1. Ensure all code compiles without errors
2. All tests must pass: `mvn clean test`
3. Follow existing code style and naming conventions
4. Include JavaDoc comments for public methods
5. Commit your changes with meaningful commit messages

## Testing Your Work

Run all tests:
```bash
mvn clean test
```

Run specific test class:
```bash
mvn test -Dtest=StudentTest
mvn test -Dtest=CourseTest
```

Check test coverage and ensure your new code is tested.

## Tips for Success

- Start with beginner exercises and progress sequentially
- Write tests before or alongside your implementation (TDD approach)
- Test edge cases and error conditions
- Use meaningful variable names and add comments
- Refactor your code to keep it clean and maintainable
- Ask questions if you get stuck

Good luck with your homework!
