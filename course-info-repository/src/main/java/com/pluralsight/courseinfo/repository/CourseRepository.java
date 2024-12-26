package com.pluralsight.courseinfo.repository;

import com.pluralsight.courseinfo.domain.Course;

import java.util.List;

public interface CourseRepository {

    void saveCourse(Course course);
    List<Course> getAllCourses();
    void addNotes(String id, String notes);

    // This method that is responsible for instantiating
    // the CourseJdbcRepository which is a private class

    // you can try and use this method inorder not to expose your implementation of code
    static CourseRepository openCourseRepository(String databaseFile){
        return new CourseJdbcRepository(databaseFile);
    }
    //  The return type of the static factory method on the interface is CourseRepository,
    //  so we're still not leaking details about the implementation.
}
