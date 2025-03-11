package dao;

import domainModel.Visit;
import domainModel.Tags.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class PostgreSQLTagDAOTest {
    private PostegreSQLTagDAO tagDAO;

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        // Set up the database for testing
        Database.setDatabase("test.db");
        Database.initDatabase();
    }

    @BeforeEach
    void setUp() throws SQLException {
        // Clear the "tags" table before each test
        tagDAO = new PostegreSQLTagDAO();
        Database.getConnection().prepareStatement("DELETE FROM tags").executeUpdate();
    }

    @Test
    void testGetTag() throws SQLException {
        // Test retrieving a tag from the database
        Tag tag = new TagSpecialty("Math");
        tagDAO.addTag(tag);

        Tag retrievedTag = tagDAO.getTag("Math", "Subject");
        Assertions.assertNotNull(retrievedTag);
        Assertions.assertEquals("Math", retrievedTag.getTag());
    }

    @Test
    void testGetAllTags() throws SQLException {
        // Test retrieving all tags from the database
        Tag tag1 = new TagSpecialty("Math");
        Tag tag2 = new TagUrgencyLevel("Intermediate");
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
        // Test attaching a tag to a visit
        VisitDAO visitDAO = new PostgreSQLVisitDAO(new PostegreSQLTagDAO());
        Visit visit = new Visit(1, "Math Class", "Learn math", LocalDateTime.now(), LocalDateTime.now().plusHours(1), 50.0, "Tutor123");
        visitDAO.insert(visit);

        Tag tag = new TagSpecialty("Math");
        tagDAO.attachTag(visit.getIdVisit(), tag);

        List<Tag> visitTags = tagDAO.getTagsByVisit(visit.getIdVisit());
        Assertions.assertEquals(1, visitTags.size());
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
        // Test detaching a tag from a visit
        VisitDAO visitDAO = new PostgreSQLVisitDAO(new PostegreSQLTagDAO());
        Visit visit = new Visit(1, "English Class", "Learn English", LocalDateTime.now(), LocalDateTime.now().plusHours(1), 40.0, "Tutor456");
        visitDAO.insert(visit);

        Tag tag = new TagUrgencyLevel("Advanced");
        tagDAO.attachTag(visit.getIdVisit(), tag);

        boolean detached = tagDAO.detachTag(visit.getIdVisit(), tag);
        Assertions.assertTrue(detached);

        List<Tag> visitTags = tagDAO.getTagsByVisit(visit.getIdVisit());
        Assertions.assertEquals(0, visitTags.size());
    }

    @Test
    void testGetTagsByVisit() throws Exception {
        // Test retrieving tags associated with a visit
        VisitDAO visitDAO = new PostgreSQLVisitDAO(new PostegreSQLTagDAO());
        Visit visit = new Visit(4, "Science Class", "Learn science", LocalDateTime.now(), LocalDateTime.now().plusHours(1), 60.0, "Tutor789");
        visitDAO.insert(visit);

        Tag tag1 = new TagSpecialty("Physics");
        Tag tag2 = new TagIsOnline("True");
        tagDAO.attachTag(visit.getIdVisit(), tag1);
        tagDAO.attachTag(visit.getIdVisit(), tag2);

        List<Tag> visitTags = tagDAO.getTagsByVisit(visit.getIdVisit());
        Assertions.assertEquals(2, visitTags.size());
    }
}
