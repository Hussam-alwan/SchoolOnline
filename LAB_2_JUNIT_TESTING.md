# Lab 2: JUnit & Testing

## Overview
This lab focuses on advanced testing concepts and best practices using JUnit 5. Building on Lab 1's foundation, you'll learn how to write comprehensive, maintainable test suites that ensure code quality and correctness.

## Learning Objectives
- ✅ JUnit 5 advanced features and annotations
- ✅ Test organization with nested test classes
- ✅ Parameterized tests with multiple data sources
- ✅ Repeated tests for stress testing
- ✅ Test fixtures and lifecycle management
- ✅ Assertion methods and complex assertions
- ✅ Test naming conventions and display names
- ✅ Edge cases and boundary testing
- ✅ Test coverage analysis
- ✅ Best practices for maintainable tests

## Project Structure

```
src/main/java/com/bootcamp/onlineschool/
├── model/
│   ├── Student.java          # From Lab 1
│   └── Course.java           # NEW: Course management
└── StudentRegistry.java      # From Lab 1

src/test/java/com/bootcamp/onlineschool/
├── model/
│   ├── StudentTest.java      # From Lab 1
│   └── CourseTest.java       # NEW: Advanced course tests
├── StudentRegistryTest.java  # From Lab 1
└── StudentRegistryAdvancedTest.java  # NEW: Advanced registry tests
```

## Key Concepts

### 1. JUnit 5 Annotations

```java
@DisplayName("Descriptive test name")
@BeforeEach              // Runs before each test
@AfterEach               // Runs after each test
@BeforeAll               // Runs once before all tests
@AfterAll                // Runs once after all tests
@Nested                  // Organize related tests
@ParameterizedTest       // Run test with multiple inputs
@RepeatedTest            // Run test multiple times
@Test                    // Mark as test method
```

### 2. Nested Test Classes

Organize related tests into logical groups:

```java
@Nested
@DisplayName("Enrollment Tests")
class EnrollmentTests {
    // Related enrollment tests
}
```

**Benefits:**
- Better organization
- Clearer test structure
- Easier to understand relationships
- Improved readability

### 3. Parameterized Tests

Run the same test with different inputs:

```java
@ParameterizedTest
@ValueSource(ints = {1, 5, 10, 15, 20})
void testWithMultipleValues(int value) {
    // Test logic
}

@ParameterizedTest
@CsvSource({
    "STU001, Alice, 3.8",
    "STU002, Bob, 3.5"
})
void testWithCsv(String id, String name, double gpa) {
    // Test logic
}
```

**Advantages:**
- Reduce code duplication
- Test multiple scenarios
- Easier to add new test cases
- Better coverage with less code

### 4. Repeated Tests

Stress test code by running it multiple times:

```java
@RepeatedTest(5)
void testMultipleTimes(RepetitionInfo info) {
    int currentRepetition = info.getCurrentRepetition();
    int totalRepetitions = info.getTotalRepetitions();
    // Test logic
}
```

**Use Cases:**
- Concurrency testing
- Performance testing
- State management verification
- Stress testing

### 5. Test Lifecycle

```
@BeforeAll (static)
    ↓
@BeforeEach
    ↓
@Test / @ParameterizedTest / @RepeatedTest
    ↓
@AfterEach
    ↓
@AfterAll (static)
```

### 6. Assertion Methods

```java
// Basic assertions
assertEquals(expected, actual)
assertNotEquals(unexpected, actual)
assertTrue(condition)
assertFalse(condition)
assertNull(object)
assertNotNull(object)

// Complex assertions
assertThrows(Exception.class, () -> { /* code */ })
assertDoesNotThrow(() -> { /* code */ })
assertAll(
    () -> assertEquals(1, 1),
    () -> assertTrue(true)
)

// Message assertions
assertEquals(expected, actual, "Custom message")
```

## New Components in Lab 2

### Course.java
A new domain model demonstrating:
- Enrollment management
- State tracking (enrolled students)
- Business logic (isFull, getAvailableSeats)
- Validation

### CourseTest.java
Advanced testing patterns:
- Nested test classes for organization
- Parameterized tests with @ValueSource
- Parameterized tests with @CsvSource
- Edge case testing
- Boundary value testing

### StudentRegistryAdvancedTest.java
Comprehensive testing strategies:
- Multiple parameterized test sources
- Repeated tests with RepetitionInfo
- Complex search and filter testing
- Sorting verification
- Statistics calculation
- Error handling
- State management

## Running the Tests

### Run all tests
```bash
mvn test
```

### Run specific test class
```bash
mvn test -Dtest=CourseTest
mvn test -Dtest=StudentRegistryAdvancedTest
```

### Run with coverage report
```bash
mvn test jacoco:report
```

### Run tests with verbose output
```bash
mvn test -X
```

## Test Results

**Expected Output:**
```
[INFO] Running com.bootcamp.onlineschool.model.StudentTest
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0

[INFO] Running com.bootcamp.onlineschool.model.CourseTest
[INFO] Tests run: 23, Failures: 0, Errors: 0, Skipped: 0

[INFO] Running com.bootcamp.onlineschool.StudentRegistryTest
[INFO] Tests run: 12, Failures: 0, Errors: 0, Skipped: 0

[INFO] Running com.bootcamp.onlineschool.StudentRegistryAdvancedTest
[INFO] Tests run: 18, Failures: 0, Errors: 0, Skipped: 0

[INFO] Results:
[INFO] Tests run: 60, Failures: 0, Errors: 0, Skipped: 0
```

**Total: 60 tests passing ✅**

## Best Practices

### 1. Test Naming
```java
// Good
@DisplayName("Should enroll student successfully")
void testEnrollStudent() { }

// Avoid
void test1() { }
void enrollTest() { }
```

### 2. Arrange-Act-Assert Pattern
```java
@Test
void testExample() {
    // Arrange: Set up test data
    Student student = new Student("STU001", "Alice", "alice@school.edu");
    
    // Act: Perform the action
    registry.addStudent(student);
    
    // Assert: Verify the result
    assertEquals(1, registry.getStudentCount());
}
```

### 3. One Assertion Per Test (When Possible)
```java
// Good: Focused test
@Test
void testEnrollmentIncrementsCount() {
    course.enrollStudent();
    assertEquals(1, course.getEnrolledStudents());
}

// Acceptable: Related assertions
@Test
void testEnrollmentUpdatesState() {
    assertTrue(course.enrollStudent());
    assertEquals(1, course.getEnrolledStudents());
    assertFalse(course.isFull());
}
```

### 4. Use Descriptive Assertions
```java
// Good
assertTrue(course.isFull(), "Course should be full after max enrollments");

// Better
assertEquals(30, course.getEnrolledStudents(), 
    "Expected 30 enrolled students but got " + course.getEnrolledStudents());
```

### 5. Test Edge Cases
```java
@Test
void testSingleSeatCourse() {
    Course singleSeat = new Course("CS999", "Special", 1, "Dr. X", 1);
    assertTrue(singleSeat.enrollStudent());
    assertTrue(singleSeat.isFull());
    assertFalse(singleSeat.enrollStudent());
}
```

## Exercises

### Exercise 1: Add Parameterized Tests
Add parameterized tests to StudentTest for various GPA values:
```java
@ParameterizedTest
@ValueSource(doubles = {0.0, 2.0, 3.5, 4.0})
void testValidGpaValues(double gpa) {
    // Test implementation
}
```

### Exercise 2: Create Nested Test Classes
Organize StudentRegistryTest into nested classes:
- SearchTests
- SortingTests
- StatisticsTests
- ErrorHandlingTests

### Exercise 3: Add Repeated Tests
Create repeated tests for concurrent enrollment:
```java
@RepeatedTest(10)
void testConcurrentEnrollment(RepetitionInfo info) {
    // Test implementation
}
```

### Exercise 4: Test Error Conditions
Add comprehensive error handling tests:
```java
@Test
void testInvalidInputHandling() {
    // Test various invalid inputs
}
```

### Exercise 5: Implement Custom Assertions
Create helper methods for common assertions:
```java
private void assertCourseIsFull(Course course) {
    assertTrue(course.isFull());
    assertEquals(0, course.getAvailableSeats());
}
```

## Advanced Topics

### 1. Test Fixtures
```java
@BeforeEach
void setupTestData() {
    // Initialize common test data
}

@AfterEach
void cleanupTestData() {
    // Clean up resources
}
```

### 2. Conditional Test Execution
```java
@Test
@EnabledOnOs(OS.WINDOWS)
void testWindowsSpecific() { }

@Test
@EnabledIfEnvironmentVariable(named = "ENV", matches = "prod")
void testProductionOnly() { }
```

### 3. Test Tagging
```java
@Test
@Tag("integration")
void testIntegration() { }

// Run: mvn test -Dgroups=integration
```

## Common Pitfalls to Avoid

1. **Tests that depend on each other**
   - Each test should be independent
   - Don't rely on test execution order

2. **Tests that are too complex**
   - Keep tests simple and focused
   - One concept per test

3. **Hardcoded values**
   - Use parameterized tests instead
   - Make tests data-driven

4. **Ignoring test failures**
   - Fix failing tests immediately
   - Don't skip tests with @Disabled

5. **Poor test names**
   - Use @DisplayName for clarity
   - Test names should describe what's being tested

## Resources

- [JUnit 5 Documentation](https://junit.org/junit5/docs/current/user-guide/)
- [Parameterized Tests Guide](https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests)
- [Test Organization](https://junit.org/junit5/docs/current/user-guide/#writing-tests-nested)

## Next Steps

After completing Lab 2, proceed to **Lab 3: Spring Boot Basics** to learn how to integrate testing with the Spring Framework.

## Summary

Lab 2 introduces advanced testing concepts that make your test suites more maintainable, readable, and comprehensive. Key takeaways:

- Use nested classes to organize related tests
- Leverage parameterized tests to reduce duplication
- Follow the Arrange-Act-Assert pattern
- Test edge cases and boundary conditions
- Use descriptive test names and display names
- Keep tests independent and focused
- Verify both success and failure scenarios

**Total Tests in Lab 2: 60 ✅**
