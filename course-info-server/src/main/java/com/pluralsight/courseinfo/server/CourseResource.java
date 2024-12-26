package com.pluralsight.courseinfo.server;

import com.pluralsight.courseinfo.domain.Course;
import com.pluralsight.courseinfo.repository.CourseRepository;
import com.pluralsight.courseinfo.repository.RepositoryException;
import jakarta.ws.rs.*;


import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Path("/courses"):
 *
 * This tells the system that this class (CourseResource) handles HTTP requests sent to the /courses URL.
 * For example, if someone accesses http://example.com/courses, this class will handle it.
 *
 */

@Path("/courses")
public class CourseResource {
    private static final Logger LOG = LoggerFactory.getLogger(CourseResource.class);

    private final CourseRepository courseRepository;

    public CourseResource(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     *
     *  * @GET:
     *  *
     *  * This annotation specifies that the getCourses() method responds to HTTP GET requests.
     *  * A GET request is used to retrieve information (e.g., fetching a list of courses).
     *
     */
    @GET
    @Path("/all") // this is needed if you will have multiple return
    // kailangan mo ung @Path para madistinguish ng code kung alin ang nireretrieve mo na data!
    @Produces(MediaType.APPLICATION_JSON) // This returns the String or any other data to return as a JSON file
    public Response getCourses(){ // Returns a Response Object which represents the HTTP response sent back to the client
        LOG.info("Fetching all courses");
//        System.out.println("Fetching all Courses");
        try{
            /*          First Approach this does not able to be "Pretty-Print"
//            String courses =
//                    courseRepository
//                            .getAllCourses()
//                            .stream()
//                            .map(Course::toString)
//                            .collect(Collectors.joining(", ")); // joins all the strings into a single string, separated by commas
            */
            //          Second Approach with this approach can now be "Pretty-Print" on the Browser
            List<Course> courses = courseRepository.getAllCourses();
            return Response.ok(courses).build(); // Creates an HTTP response with a 200 status code (OK).
            // build(): Finalizes the response object.
        } catch (Exception e) {
            LOG.error("Error fetching courses", e);
            return Response.serverError().entity("Error fetching courses").build();
        }
    }
    @GET
    @Path("/sorted")
    @Produces(MediaType.APPLICATION_JSON)
    public Stream<Course> getSortedCourses(){
        System.out.println("Fetching all Courses Then Sorted by Length");
        try {
            return courseRepository
                    .getAllCourses()
                    .stream()
                    .sorted(Comparator.comparing(Course::length));
        }catch (RepositoryException e) {
            LOG.error("Could not retrieve courses from the database", e);
            throw new NotFoundException();
        }
    }

    @POST
    @Path("/{id}/notes")
    @Consumes(MediaType.TEXT_PLAIN)
    // @PathParam is whatever value the caller puts in the id placeholder
    // location will be put into the id string parameter that we have in the AddNotes method
    public Response addNotes(@PathParam("id") String id, String notes) {
        try{
            courseRepository.addNotes(id, notes);
            return Response.status(Response.Status.CREATED)  // 201 Created
                    .entity("Notes added for course " + id)
                    .build();
        } catch (Exception e) {
            // If there is an error, respond with a 500 Internal Server Error
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error adding notes for course " + id)
                    .build();
        }
    }
}
