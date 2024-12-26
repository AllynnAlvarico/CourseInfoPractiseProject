package com.pluralsight.courseinfo.repository;

import com.pluralsight.courseinfo.domain.Course;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.sql.*;
import java.util.*;

class CourseJdbcRepository implements CourseRepository{

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
    private static final  String ADD_NOTES = """
            UPDATE Courses SET notes = ?
            WHERE id = ?
            """;


    private final DataSource dataSource;


    public CourseJdbcRepository(String databaseFile) {
        // Creates a class of jdbcDataSource
        JdbcDataSource jdbcDataSource = new JdbcDataSource();
//        String absolutePath = new File(databaseFile).getAbsolutePath();
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
        // Refer to Line 51
        try(Connection connection = dataSource.getConnection()){

        //  A Statement is used to execute SQL queries on the database.
            Statement statement = connection.createStatement();
        //  This allows us to send a command like "Give me all the rows from the COURSES table."
        //  The SQL query "SELECT * FROM COURSES" retrieves all rows from the COURSES table.
            ResultSet resultSet = statement.executeQuery("SELECT * FROM COURSES");


            List<Course> courses = new ArrayList<>();
        //  The resultSet.next() moves to the next row in the results.
        //  The loop continues until all rows have been processed.
            while (resultSet.next()){
        /*
         *  For each row in the ResultSet:
         *  resultSet.getString(1) retrieves the value from the first column (e.g., id).
         *  resultSet.getString(2) retrieves the value from the second column (e.g., name).
         *  resultSet.getLong(3) retrieves the value from the third column (e.g., length).
         *  resultSet.getNString(4) retrieves the value from the fourth column (e.g., url).
         *  These values are used to create a new Course object.
         */
                Course course =
                        new Course(
                                resultSet.getString(1),
                                resultSet.getString(2),
                                resultSet.getLong(3),
                                resultSet.getNString(4),
                                Optional.ofNullable(resultSet.getString(5)));
                courses.add(course);

            }
        //  Collections.unmodifiableList(courses) makes the list read-only, preventing modifications outside this method.
        //  This ensures the integrity of the list.
            return Collections.unmodifiableList(courses);
        }catch (SQLException e) {
            throw  new RepositoryException("Failed to retrieve courses", e);
        }
        /*
         *      SUMMARY of List<Course> getAllCourses()
         * Open a database connection.
         * Create a statement to execute SQL queries.
         * Run a query to fetch all rows from the COURSES table.
         * For each row, create a Course object.
         * Add each Course object to a list.
         * Return the list as read-only.
         * If something goes wrong, handle the error gracefully.
         */
    }

    @Override
    public void addNotes(String id, String notes) {
        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(ADD_NOTES);
            statement.setString(1, notes);
            statement.setString(2, id);
            statement.execute();
        } catch (SQLException e) {
            throw new RepositoryException("Failed to add notes to " + id, e );
        }
    }
}
