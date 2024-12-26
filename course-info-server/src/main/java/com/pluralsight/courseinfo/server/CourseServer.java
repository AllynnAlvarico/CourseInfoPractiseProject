package com.pluralsight.courseinfo.server;

import com.pluralsight.courseinfo.repository.CourseRepository;
import org.glassfish.grizzly.Grizzly;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class CourseServer {
    private static final Logger LOG = LoggerFactory.getLogger(CourseServer.class);
    private static final String BASE_URI = "http://localhost:8080/";
    public static void main(String[] args) {
        LOG.info("Starting HTTP server");
        // This opens or creates a connection to a database file called courses.db in the current directory (./).
        CourseRepository courseRepository = CourseRepository.openCourseRepository("./courses.db");
        // ResourceConfig:
        //          Creates a configuration object for the server.
        //          Think of it as setting up the rules for what the server will do.
        // .register(new CourseResource(courseRepository)):
        //          Tells the server to use the CourseResource class
        //          (which handles requests to /courses) and provides it
        //          with the courseRepository to manage the course data.
        ResourceConfig config = new ResourceConfig().register(new CourseResource(courseRepository));
        // GrizzlyHttpServerFactory.createHttpServer(...):
        //          Creates and starts an HTTP server that can listen to requests.
        // URI.create(BASE_URI):
        //          Specifies the base address (like http://localhost:8080/) where the server will be available.
        // config:
        //          Passes the server configuration (including the /courses resource) to handle incoming requests.
        GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);

        //                          SUMMARY
        //      ->Opens a database (courses.db) to manage course data.
        //      ->Configures the server to respond to HTTP requests, specifically those handled by CourseResource.
        //      ->Starts the server at the specified base URL (BASE_URI).
    }
    /**
     * Analogy:
     * Imagine you’re opening a restaurant:
     *
     * CourseRepository: Like opening your pantry (database) where all ingredients (course data) are stored.
     * ResourceConfig: Setting up the kitchen rules, such as which chefs (resources) handle which tasks (requests).
     * GrizzlyHttpServerFactory.createHttpServer: Turning on the open sign and opening the restaurant to customers (HTTP requests).
     */
}
