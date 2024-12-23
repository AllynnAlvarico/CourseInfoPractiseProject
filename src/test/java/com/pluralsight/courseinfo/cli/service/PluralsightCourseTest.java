package com.pluralsight.courseinfo.cli.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PluralsightCourseTest {

    @Test
    void durationInMinutes() {
        PluralsightCourse course =
                new PluralsightCourse("id", "Test Course", "00:05:37", "url", false);
        assertEquals(5, course.durationInMinutes());
    }
    @Test
    void durationInMinutesOverHour() {
        PluralsightCourse course =
                new PluralsightCourse("id", "Test Course", "01:08:54.9613330", "url", false);
        assertEquals(68, course.durationInMinutes());
    }
    @Test
    void durationInMinutesZero() {
        PluralsightCourse course =
                new PluralsightCourse("id", "Test Course", "00:00:00", "url", false);
        assertEquals(0, course.durationInMinutes());
    }
}