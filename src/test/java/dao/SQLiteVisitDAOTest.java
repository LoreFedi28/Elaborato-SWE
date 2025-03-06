package test.java.dao;


import domainModel.Lesson;
import domainModel.State.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
class SQLiteVisitDAOTest {
    private SQLiteLessonDAO lessonDAO;
    private SQLiteTagDAO tagDAO;

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        // Set up the test database
        Database.setDatabase("test.db");
        Database.initDatabase();
    }

    @BeforeEach
    public void init() throws SQLException {
        Connection connection = Database.getConnection();
        tagDAO = new SQLiteTagDAO();
        lessonDAO = new SQLiteLessonDAO(tagDAO);

        // Clear the "lessons" and "tags" tables
        connection.prepareStatement("DELETE FROM lessons").executeUpdate();
        connection.prepareStatement("DELETE FROM tags").executeUpdate();

        // Insert some test data
        connection.prepareStatement("INSERT INTO tags (tag, tagType) VALUES ('Math', 'Subject')").executeUpdate();
        connection.prepareStatement("INSERT INTO tags (tag, tagType) VALUES ('Physics', 'Subject')").executeUpdate();

        connection.prepareStatement("INSERT INTO lessons (idLesson, title, description, startTime, endTime, price, tutorCF, state, stateExtraInfo) VALUES (1, 'Math Lesson', 'Math basics', '2023-10-25T10:00:00', '2023-10-25T11:00:00', 50.0, 'tutor1', 'Available', NULL)").executeUpdate();
        connection.prepareStatement("INSERT INTO lessons (idLesson, title, description, startTime, endTime, price, tutorCF, state, stateExtraInfo) VALUES (2, 'Physics Lesson', 'Physics basics', '2023-10-26T10:00:00', '2023-10-26T11:00:00', 60.0, 'tutor2', 'Booked', 'student1')").executeUpdate();
    }

    @Test
    void testGetLessonByID() throws Exception {
        // Test the get method to retrieve a lesson by ID
        Lesson lesson = lessonDAO.get(1);
        Assertions.assertNotNull(lesson);
        Assertions.assertEquals("Math Lesson", lesson.getTitle());
        Assertions.assertEquals("Math basics", lesson.getDescription());
        Assertions.assertEquals(50.0, lesson.getPrice());
        Assertions.assertEquals("Available", lesson.getState());
    }

    @Test
    void testGetLessonByIDNonExistent() throws Exception {
        // Test the get method with a non-existent ID
        Lesson lesson = lessonDAO.get(3);
        Assertions.assertNull(lesson);
    }

    @Test
    void testGetAllLessons() throws Exception {
        // Test the getAll method to retrieve all lessons
        List<Lesson> lessons = lessonDAO.getAll();
        Assertions.assertEquals(2, lessons.size());
    }

    @Test
    void testInsertLesson() throws Exception {
        // Test the insert method to add a new lesson
        Lesson lesson = new Lesson(3, "English Lesson", "English basics", LocalDateTime.now(), LocalDateTime.now(), 40.0, "tutor3");
        lessonDAO.insert(lesson);
        List<Lesson> lessons = lessonDAO.getAll();
        Assertions.assertEquals(3, lessons.size());
    }

    @Test
    void testUpdateLesson() throws Exception {
        // Test the update method to update a lesson's information
        Lesson lesson = new Lesson(1, "Updated Math Lesson", "Updated Math basics", LocalDateTime.now(), LocalDateTime.now(), 55.0, "tutor1");
        lessonDAO.update(lesson);
        Lesson updatedLesson = lessonDAO.get(1);
        Assertions.assertEquals("Updated Math Lesson", updatedLesson.getTitle());
        Assertions.assertEquals("Updated Math basics", updatedLesson.getDescription());
        Assertions.assertEquals(55.0, updatedLesson.getPrice());
    }

    @Test
    void testDeleteLesson() throws Exception {
        // Test the delete method to delete a lesson
        boolean result = lessonDAO.delete(1);
        Assertions.assertTrue(result);
        List<Lesson> lessons = lessonDAO.getAll();
        Assertions.assertEquals(1, lessons.size());
    }

    @Test
    void testDeleteNonExistentLesson() throws Exception {
        // Test the delete method with a non-existent lesson
        boolean result = lessonDAO.delete(3);
        Assertions.assertFalse(result);
        List<Lesson> lessons = lessonDAO.getAll();
        Assertions.assertEquals(2, lessons.size());
    }

    @Test
    void testGetTutorLessonsByState() throws Exception {
        // Test the method to get lessons by tutor and state
        List<Lesson> lessons = lessonDAO.getTutorLessonsByState("tutor2", new Booked("student1"));
        Assertions.assertEquals(1, lessons.size());
        Assertions.assertEquals("Physics Lesson", lessons.get(0).getTitle());
        Assertions.assertEquals("Booked", lessons.get(0).getState());
    }

    @Test
    void testGetStudentBookedLessons() throws Exception {
        // Test the method to get lessons booked by a student
        List<Lesson> lessons = lessonDAO.getStudentBookedLessons("student1");
        Assertions.assertEquals(1, lessons.size());
        Assertions.assertEquals("Physics Lesson", lessons.get(0).getTitle());
        Assertions.assertEquals("Booked", lessons.get(0).getState());
    }
}

