package com.pluralsight.courseinfo.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        } catch (Exception ex) {
            LOG.error("Unexpected Error!", ex);
        }
    }

    private static void retrieveCourse(String authorId) {
        LOG.info("Retrieving course for author '{}'", authorId);
    }
}
