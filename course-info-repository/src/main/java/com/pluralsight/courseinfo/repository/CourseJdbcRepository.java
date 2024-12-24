package com.pluralsight.courseinfo.repository;

import com.pluralsight.courseinfo.domain.Course;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class CourseJdbcRepository implements CourseRepository{

    /*
     * H2_DATABASE_URL is like a directions to a storage or like a
     * -> the "%s" is a placeholder or (variable string to be replaced)
     * -> AUTO_SERVER=TRUE
     *      (*Configuration) is that it lets you connect to that database and will be always open
     * -> INIT=RUNSCRIPT FROM './db_init.sql'
     *          Runs a script to make it ready to accept any data
     */
    private static final String H2_DATABASE_URL =
            "jdbc:h2:file%s;AUTO_SERVER=TRUE;INIT=RUNSCRIPT FROM './db_init.sql'";

    // remember to use this placeholder syntax and
    // the PreparedStatement API to craft SQL statements
    // rather than creating a SQL statement industry yourself.
    /*
     * This is a template for adding of updating the record in the database
     * The ? question mark symbols are just placeholder that will be filled in with data when the program runs
     */
    private static final String INSERT_COURSE = """
            MERGE INTO Courses (id, name, length, url)
            VALUES (?, ?, ?, ?)
            """;

    private final DataSource dataSource;


    public CourseJdbcRepository(String databaseFile) {
        // Creates a class of jdbcDataSource
        JdbcDataSource jdbcDataSource = new JdbcDataSource();
        // Then sets the location of the database
        jdbcDataSource.setURL(H2_DATABASE_URL.formatted(databaseFile));
        // Then assigned the jdbc to the DataSource Class which is in Line 37
        this.dataSource = jdbcDataSource;
    }
    @Override
    public void saveCourse(Course course) {
        // try(Connection connection = dataSource.getConnection())
        //          opens the connection to the database so that we can put something inside.
        try(Connection connection = dataSource.getConnection()) {
        // PreparedStatement statement = connection.prepareStatement(INSERT_COURSE);
        //          This creates a blank form (using the INSERT_COURSE template).
            PreparedStatement statement = connection.prepareStatement(INSERT_COURSE);
        // Each setString, setLong, etc., fills in one of the placeholders (?) with actual course data.
        //          Line 34
        // This block <--
            statement.setString(1, course.id());
            statement.setString(2, course.name());
            statement.setLong(3, course.length());
            statement.setString(4, course.url());
        // <--
        // statement.execute(); This sends the data into the database.
            statement.execute();
        // If something goes wrong (e.g., the database is down), the program throws an error to let you know what happened.
        // <-- This Block
        } catch (SQLException e) {
            throw new RepositoryException("Failed to save " + course, e );
        }
        // <--
        // To better understand this code is refer to "my-private-channel" as Analogy (Retail at ALDI)
    }

    @Override
    public List<Course> getAllCourses() {
        return null;
    }
}
