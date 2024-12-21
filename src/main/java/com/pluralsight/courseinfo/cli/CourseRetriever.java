package com.pluralsight.courseinfo.cli;

public class CourseRetriever {
    public static void main(String[] args) {

        System.out.println("Hello");

        if(args.length == 0){
            System.out.println("Please enter an author name as a first argument.");
            return;
        }
        try {
            retrieveCourse(args[0]);
        } catch (Exception ex) {
            System.out.println("Unexpected Error!");
            ex.printStackTrace();
        }



    }

    private static void retrieveCourse(String authorId) {

        System.out.println("Retrieving course for author " + authorId);

    }
}
