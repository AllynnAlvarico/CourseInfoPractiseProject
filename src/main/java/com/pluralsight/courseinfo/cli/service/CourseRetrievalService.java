package com.pluralsight.courseinfo.cli.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class CourseRetrievalService {
    // the PS_URI -> I am setting up the URL of where I am going to request the API
    // The %s is a variable that can be change using PS_URI.formatted(myString)
    // This will replace the %s with the string like for example the pass parameter which is authorId
    private static final String PS_URI = "https://app.pluralsight.com/profile/data/author/%s/all-content";

    // Next, we need an HttpClient instance.
    // Declaring this as a private static final fields,
    // because a single HttpClient instance is thread safe
    // and can be shared among many calls, and it is more efficient to only instantiate the HttpClient once.
    // private static final HttpClient CLIENT = HttpClient.newHttpClient();<================================
    //======================================================================================================
    //  making the HttpClient interactions a bit more robust
    //  configure the HttpClient to always follow Http redirects if the server returns those.
    //  That way, if in the future the location of this API changes
    //  and redirects have been set up correctly on the server side,
    //  the Java HttpClients will transparently call this new URL that is returned as a redirect URL from the server.
    private static final HttpClient CLIENT = HttpClient
            .newBuilder()
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .build();
    // ======================================================================================================



    public List<PluralsightCourse> getCoursesFor(String authorId){
        //HttpRequest is the Class that will build and request the data that you wanted.
        // and with this, it would allow to build and invoke the request
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(PS_URI.formatted(authorId)))
                .GET()
                .build();
        try {
            // This block or the HttpResponse will hold all the data payload from the API requested
            // and then you will use that variable to get the response body of that object payload.
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            // I did not know you can return like this with switch statement which is amazing!
            // so this is a standard or basic for having a status code so if it 200 or status is OK then return the body
            // if not found return nothing
            // if nothing else give exception to exit gracefully!
            return switch (response.statusCode()) {
//                case 200 -> response.body();
                case 200 -> null;
//                case 404 -> "";
                case 404 -> List.of();
                default -> throw  new RuntimeException("Pluralsight API call failed with status code " + response.statusCode());
            };
        }
        // Now I learn something new which you can have an or operator on catch block
        catch (IOException | InterruptedException e){
            throw new RuntimeException("Could not call Pluralsight API", e);
        }
    }

}
