package com.pluralsight.courseinfo.cli.service;

import com.pluralsight.courseinfo.domain.Course;
import com.pluralsight.courseinfo.repository.CourseRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CourseStorageServiceTest {

    @Test
    void storePluralsightCourses(){
        // repository:
        //      An in-memory implementation of CourseRepository is created.
        //      This serves as a mock or fake database.
        CourseRepository repository = new InMemoryCourseService();
        // courseStorageService:
        //      The CourseStorageService is instantiated with the mock repository.
        CourseStorageService courseStorageService = new CourseStorageService(repository);

        PluralsightCourse ps1 =
                new PluralsightCourse(
                        "1",
                        "Title 1",
                        "01:40:00.123",
                        "/url-1",
                        false);
        courseStorageService.storePluralsightCourses(List.of(ps1));

        Course expected = // this is the expected results
                new Course(
                        "1",
                        "Title 1",
                        100,
                        "https://app.pluralsight.com/url-1",
                        Optional.empty());
        // test if they are all equal
        assertEquals(List.of(expected), repository.getAllCourses());

    }
    // Fake or Mock CourseRepository Class
    static class InMemoryCourseService implements CourseRepository {
        private final List<Course> courses = new ArrayList<>();

        @Override
        public void saveCourse(Course course) {
            courses.add(course);
        }

        @Override
        public List<Course> getAllCourses() {
            return courses;
        }

        @Override
        public void addNotes(String id, String notes) {
            throw new UnsupportedOperationException();
        }
    }
}
/** Analogy for this Test Class
 * Raw Recipes (PluralsightCourse): Messy cards with incomplete details.
 * Recipe Organizer (CourseStorageService): A system that tidies up the recipes and formats them properly.
 * Recipe Box (InMemoryCourseService): A test box where you temporarily store the organized recipes.
 * Test: Check if the recipes in the box are correctly formatted.
 * If the recipes look perfect in the test box, the system works!
 *
 */