package com.pluralsight.courseinfo.cli;

import com.pluralsight.courseinfo.cli.service.CourseRetrievalService;
import com.pluralsight.courseinfo.cli.service.PluralsightCourse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static java.util.function.Predicate.not; // used in line 46

public class CourseRetriever {
    private static final Logger LOG = LoggerFactory.getLogger(CourseRetriever.class);
    public static void main(String[] args) {

        // LOG.info -> is a version of System.ou.println in slf4j API
        LOG.info("Hello");


        if(args.length == 0){
            LOG.warn("Please enter an author name as a first argument.");
            return;
        }
        try {
            retrieveCourse(args[0]);
            // Example of using record NOTICE! Record class is Immutable meaning it cannot be changed!
//            PluralsightCourse course =
//                    new PluralsightCourse("id", "title", "00:54:57", "http://url", false);
//            LOG.info("Course: {}", course);

        } catch (Exception ex) {
            LOG.error("Unexpected Error!", ex);
        }
    }

    private static void retrieveCourse(String authorId) {
        LOG.info("Retrieving course for author '{}'", authorId);
        // this is the class created for the service class which holds the request and retrieval methods
        CourseRetrievalService courseRetrievalService = new CourseRetrievalService();

        // this was used for holding the response body payload of the requested id on the API
        List<PluralsightCourse> coursesToStore = courseRetrievalService.getCoursesFor(authorId)
                .stream()
                // 1st approach to this filter is
                // .filter(course -> !course.isRetired())
                // 2nd approach
                .filter(not(PluralsightCourse::isRetired))
                .toList();

        // this is to display the payload of the API Object requested
        LOG.info("Retrieved the following {} courses {}", coursesToStore.size() , coursesToStore);
    }
}
