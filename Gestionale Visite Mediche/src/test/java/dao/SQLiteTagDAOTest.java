package test.java.dao;

import domainModel.Lesson;
import domainModel.Tags.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class SQLiteTagDAOTest {
    private SQLiteTagDAO tagDAO;

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        // Set up the database for testing
        Database.setDatabase("test.db");
        Database.initDatabase();
    }

    @BeforeEach
    void setUp() throws SQLException {
        // Clear the "tags" table before each test
        tagDAO = new SQLiteTagDAO();
        Database.getConnection().prepareStatement("DELETE FROM tags").executeUpdate();
    }

    @Test
    void testGetTag() throws SQLException {
        // Test retrieving a tag from the database
        Tag tag = new TagSubject("Math");
        tagDAO.addTag(tag);

        Tag retrievedTag = tagDAO.getTag("Math", "Subject");
        Assertions.assertNotNull(retrievedTag);
        Assertions.assertEquals("Math", retrievedTag.getTag());
    }

    @Test
    void testGetAllTags() throws SQLException {
        // Test retrieving all tags from the database
        Tag tag1 = new TagSubject("Math");
        Tag tag2 = new TagLevel("Intermediate");
        tagDAO.addTag(tag1);
        tagDAO.addTag(tag2);

        List<Tag> tags = tagDAO.getAllTags();
        Assertions.assertEquals(2, tags.size());
    }

    @Test
    void testAddTag() throws SQLException {
        // Test adding a new tag to the database
        Tag tag = new TagZone("Europe");
        tagDAO.addTag(tag);

        Tag retrievedTag = tagDAO.getTag("Europe", "Zone");
        Assertions.assertNotNull(retrievedTag);
        Assertions.assertEquals("Europe", retrievedTag.getTag());
    }

    @Test
    void testAttachTag() throws Exception {
        // Test attaching a tag to a lesson
        LessonDAO lessonDAO = new SQLiteLessonDAO(new SQLiteTagDAO());
        Lesson lesson = new Lesson(1, "Math Class", "Learn math", LocalDateTime.now(), LocalDateTime.now().plusHours(1), 50.0, "Tutor123");
        lessonDAO.insert(lesson);

        Tag tag = new TagSubject("Math");
        tagDAO.attachTag(lesson.getIdLesson(), tag);

        List<Tag> lessonTags = tagDAO.getTagsByLesson(lesson.getIdLesson());
        Assertions.assertEquals(1, lessonTags.size());
    }

    @Test
    void testRemoveTag() throws SQLException {
        // Test removing a tag from the database
        Tag tag = new TagZone("Firenze");
        tagDAO.addTag(tag);

        boolean removed = tagDAO.removeTag(tag.getTag(), tag.getTypeOfTag());
        Assertions.assertTrue(removed);

        Tag retrievedTag = tagDAO.getTag("Firenze", "Zone");
        Assertions.assertNull(retrievedTag);
    }

    @Test
    void testDetachTag() throws Exception {
        // Test detaching a tag from a lesson
        LessonDAO lessonDAO = new SQLiteLessonDAO(new SQLiteTagDAO());
        Lesson lesson = new Lesson(1, "English Class", "Learn English", LocalDateTime.now(), LocalDateTime.now().plusHours(1), 40.0, "Tutor456");
        lessonDAO.insert(lesson);

        Tag tag = new TagLevel("Advanced");
        tagDAO.attachTag(lesson.getIdLesson(), tag);

        boolean detached = tagDAO.detachTag(lesson.getIdLesson(), tag);
        Assertions.assertTrue(detached);

        List<Tag> lessonTags = tagDAO.getTagsByLesson(lesson.getIdLesson());
        Assertions.assertEquals(0, lessonTags.size());
    }

    @Test
    void testGetTagsByLesson() throws Exception {
        // Test retrieving tags associated with a lesson
        LessonDAO lessonDAO = new SQLiteLessonDAO(new SQLiteTagDAO());
        Lesson lesson = new Lesson(4, "Science Class", "Learn science", LocalDateTime.now(), LocalDateTime.now().plusHours(1), 60.0, "Tutor789");
        lessonDAO.insert(lesson);

        Tag tag1 = new TagSubject("Physics");
        Tag tag2 = new TagIsOnline("True");
        tagDAO.attachTag(lesson.getIdLesson(), tag1);
        tagDAO.attachTag(lesson.getIdLesson(), tag2);

        List<Tag> lessonTags = tagDAO.getTagsByLesson(lesson.getIdLesson());
        Assertions.assertEquals(2, lessonTags.size());
    }
}
