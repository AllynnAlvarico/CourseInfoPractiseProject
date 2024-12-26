package com.pluralsight.courseinfo.domain;

import java.util.Optional;
//Added today 26/12/2024 the Optional<String> Parameter

public record Course(String id, String name, long length, String url, Optional<String> notes) {
    public Course {
        filled(id);
        filled(name);
        filled(url);
        notes.ifPresent(Course::filled);
    }
//    Can you write test codes for the course that we've just written,
//    testing the validation code? As a hint, rather than using assertEquals
//    as we did in the previous test that we wrote, look into JUnit's assertThrows method,
//    where you can check whether a particular piece of code throws an exception.
    private static void filled(String s){
        if(s == null || s.isBlank()){
            throw new IllegalArgumentException("No value present!");
        }
    }
}
