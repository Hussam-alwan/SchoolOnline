package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.entity.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CourseRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CourseRepository courseRepository;

    private Course testCourse1;
    private Course testCourse2;
    private Course testCourse3;

    @BeforeEach
    void setUp() {
        testCourse1 = new Course("Java Programming", "Introduction to Java programming language", 3, 40);
        testCourse2 = new Course("Data Structures", "Fundamental data structures and algorithms", 4, 60);
        testCourse3 = new Course("Web Development", "Modern web development with HTML, CSS, and JavaScript", 3, 50);
        
        entityManager.persistAndFlush(testCourse1);
        entityManager.persistAndFlush(testCourse2);
        entityManager.persistAndFlush(testCourse3);
    }

    @Test
    void findByName_ShouldReturnCourse_WhenNameExists() {
        // When
        Optional<Course> foundCourse = courseRepository.findByName("Java Programming");

        // Then
        assertThat(foundCourse).isPresent();
        assertThat(foundCourse.get().getName()).isEqualTo("Java Programming");
        assertThat(foundCourse.get().getCredits()).isEqualTo(3);
    }

    @Test
    void findByName_ShouldReturnEmpty_WhenNameDoesNotExist() {
        // When
        Optional<Course> foundCourse = courseRepository.findByName("Nonexistent Course");

        // Then
        assertThat(foundCourse).isEmpty();
    }

    @Test
    void findByCredits_ShouldReturnCoursesWithSpecificCredits() {
        // When
        List<Course> threeCredits = courseRepository.findByCredits(3);

        // Then
        assertThat(threeCredits).hasSize(2);
        assertThat(threeCredits).extracting(Course::getName)
                .containsExactlyInAnyOrder("Java Programming", "Web Development");
    }

    @Test
    void findByDuration_ShouldReturnCoursesWithSpecificDuration() {
        // When
        List<Course> sixtyHours = courseRepository.findByDuration(60);

        // Then
        assertThat(sixtyHours).hasSize(1);
        assertThat(sixtyHours.get(0).getName()).isEqualTo("Data Structures");
    }

    @Test
    void existsByName_ShouldReturnTrue_WhenNameExists() {
        // When
        boolean exists = courseRepository.existsByName("Web Development");

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void existsByName_ShouldReturnFalse_WhenNameDoesNotExist() {
        // When
        boolean exists = courseRepository.existsByName("Nonexistent Course");

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void findCoursesByCreditsOrDuration_ShouldReturnCoursesMatchingEitherCriteria() {
        // When
        List<Course> courses = courseRepository.findCoursesByCreditsOrDuration(4, 40);

        // Then
        assertThat(courses).hasSize(2);
        assertThat(courses).extracting(Course::getName)
                .containsExactlyInAnyOrder("Java Programming", "Data Structures");
    }

    @Test
    void findCoursesByCreditsOrDuration_ShouldReturnEmptyList_WhenNoCriteriaMatch() {
        // When
        List<Course> courses = courseRepository.findCoursesByCreditsOrDuration(5, 100);

        // Then
        assertThat(courses).isEmpty();
    }

    @Test
    void findByCreditsGreaterThanEqual_ShouldReturnCoursesWithMinimumCredits() {
        // When
        List<Course> courses = courseRepository.findByCreditsGreaterThanEqual(4);

        // Then
        assertThat(courses).hasSize(1);
        assertThat(courses.get(0).getName()).isEqualTo("Data Structures");
    }

    @Test
    void findByDurationLessThanEqual_ShouldReturnCoursesWithMaximumDuration() {
        // When
        List<Course> courses = courseRepository.findByDurationLessThanEqual(50);

        // Then
        assertThat(courses).hasSize(2);
        assertThat(courses).extracting(Course::getName)
                .containsExactlyInAnyOrder("Java Programming", "Web Development");
    }
}