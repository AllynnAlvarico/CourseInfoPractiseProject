package com.pluralsight.courseinfo.repository;

import com.pluralsight.courseinfo.domain.Course;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CourseInfoRepositoryTest {
    // id = ID
    // name = name
    // url = url.com

    @ParameterizedTest
    @CsvSource(textBlock = """
            null, validName, validUrl.com,
            validId, null, validUrl.com,
            validId, validName, null,
            '', validName, validUrl.com",
            validId, '', validUrl.com,
            validId, validName, ''
            """
    )
    // https://stackoverflow.com/questions/40268446/junit-5-how-to-assert-an-exception-is-thrown
    // ExpectedGeneralException referenced
    @DisplayName("Test IllegalArgumentException for invalid parameters")
    public void filled(String byTest_id, String byTest_name, String byTest_url) {
    // This code is provided by GPT as I do not know how to implement testing
    // But now I can add this to my tool as testing kit for a Class XD
        // -->
        String final_Id = "null".equals(byTest_id) ? null : byTest_id;
        String final_Name = "null".equals(byTest_name) ? null : byTest_name;
        String final_Url = "null".equals(byTest_url) ? null : byTest_url;

        assertThrows(
                IllegalArgumentException.class,
                () -> new Course(final_Id, final_Name, 123456, final_Url),
                "Expected IllegalArgumentException for invalid parameters"
        );
    // <--
    }
}
