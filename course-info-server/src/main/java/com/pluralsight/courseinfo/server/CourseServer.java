package com.pluralsight.courseinfo.server;

import com.pluralsight.courseinfo.repository.CourseRepository;
import org.glassfish.grizzly.Grizzly;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;
import java.util.logging.LogManager;

public class CourseServer {
    static {
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.install();
    }
    private static final Logger LOG = LoggerFactory.getLogger(CourseServer.class);
    private static final String BASE_URI = "http://localhost:8080/";
    public static void main(String[] args) {
        // with this we can externalise the configuration in line 30
        String databaseFilename = loadDatabaseFilename();

        LOG.info("Starting HTTP server with database {}", databaseFilename);
        // This opens or creates a connection to a database file called courses.db in the current directory (./).
//        CourseRepository courseRepository = CourseRepository.openCourseRepository("./courses.db");
        CourseRepository courseRepository = CourseRepository.openCourseRepository(databaseFilename);
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

    // new method 27/12/2024 1 am
    // this method allows you to call or use external configuration to call a specific file
    // for example is the File of the database path that is stored here.
    private static String loadDatabaseFilename() {
        try (InputStream propertiesStream = CourseServer.class.getResourceAsStream("/server.properties")){

            if (propertiesStream == null) {
                throw new IllegalArgumentException("Could not find server.properties file.");
            }

            Properties properties = new Properties();
            properties.load(propertiesStream);

            return properties.getProperty("course-info.database");
        } catch (IOException e){
            throw new IllegalArgumentException("Could not load database filename");
        }
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
