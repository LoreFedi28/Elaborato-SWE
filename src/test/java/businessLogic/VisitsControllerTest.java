package java.businessLogic;

import businessLogic.DoctorsController;
import businessLogic.TagsController;
import businessLogic.VisitsController;
import dao.*;
import domainModel.Visit;
import domainModel.State.*;
import domainModel.Tags.*;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VisitsControllerTest {

    private static VisitsController visitsController;
    private static SQLiteTagDAO tagDAO;
    private static SQLiteVisitDAO visitDAO;

    private static TagsController tagsController;

    @BeforeAll
    static void setUp() throws Exception {
        Database.setDatabase("test.db");
        Database.initDatabase();
        tagDAO = new SQLiteTagDAO();
        visitDAO = new SQLiteVisitDAO(tagDAO);
        visitsController = new VisitsController(visitDAO, tagDAO, new DoctorsController(new SQLiteDoctorDAO()));
        tagsController = new TagsController(tagDAO);
    }

    @BeforeEach
    void setUpDatabase() throws Exception {
        Connection connection = Database.getConnection();

        connection.prepareStatement("DELETE FROM visits").executeUpdate();
        connection.prepareStatement("DELETE FROM tags").executeUpdate();
        connection.prepareStatement("DELETE FROM doctors").executeUpdate();
        connection.prepareStatement("DELETE FROM patients").executeUpdate();
        connection.prepareStatement("DELETE FROM visitsTags").executeUpdate();

        connection.prepareStatement("INSERT INTO doctors (cf, name, surname, iban) VALUES ('doctor1', 'Doctor', 'Uno', 1234)").executeUpdate();
        connection.prepareStatement("INSERT INTO doctors (cf, name, surname, iban) VALUES ('doctor2', 'Doctor', 'Due', 5678)").executeUpdate();

        connection.prepareStatement("INSERT INTO patients (cf, name, surname, level) VALUES ('patient1', 'Patient', 'Uno', 'elementary')").executeUpdate();

        connection.prepareStatement("INSERT INTO tags (tag, tagType) VALUES ('Math', 'Subject')").executeUpdate();
        connection.prepareStatement("INSERT INTO tags (tag, tagType) VALUES ('Physics', 'Subject')").executeUpdate();

        connection.prepareStatement("INSERT INTO visits (idVisit, title, description, startTime, endTime, price, doctorCF, state, stateExtraInfo) VALUES (1, 'Math Lesson', 'Math basics', '2023-10-25T10:00:00', '2023-10-25T11:00:00', 50.0, 'doctor1', 'Available', NULL)").executeUpdate();
        connection.prepareStatement("INSERT INTO visits (idVisit, title, description, startTime, endTime, price, doctorCF, state, stateExtraInfo) VALUES (2, 'Physics Lesson', 'Physics basics', '2023-10-26T10:00:00', '2023-10-26T11:00:00', 60.0, 'doctor2', 'Booked', 'patient1')").executeUpdate();
    }

    @AfterEach
    void tearDownDatabase() throws Exception {
        Connection connection = Database.getConnection();

        connection.prepareStatement("DELETE FROM visits").executeUpdate();
        connection.prepareStatement("DELETE FROM tags").executeUpdate();
        connection.prepareStatement("DELETE FROM doctors").executeUpdate();
        connection.prepareStatement("DELETE FROM patients").executeUpdate();
        connection.prepareStatement("DELETE FROM visitsTags").executeUpdate();
    }

    @Test
    void testGetVisitByID() throws Exception {
        Visit visit = visitsController.getVisit(1);
        assertNotNull(visit);
        assertEquals("Math Lesson", visit.getTitle());
        assertEquals("Math basics", visit.getDescription());
        assertEquals(50.0, visit.getPrice());
        assertEquals("Available", visit.getState());
    }

    @Test
    void testGetVisitByIDNonExistent() throws Exception {
        Visit visit = visitsController.getVisit(3);
        assertNull(visit);
    }

    @Test
    void testGetAllVisits() throws Exception {
        List<Visit> visits = visitsController.getAll();
        assertEquals(2, visits.size());
    }

    @Test
    void testInsertVisit() throws Exception {
        List<Tag> tags = new ArrayList<>();
        TagSubject ts = new TagSubject("English");
        tags.add(ts);
        int visitId = visitsController.addVisit("English Visit", "English basics", LocalDateTime.now(), LocalDateTime.now(), 40.0, "doctor2", tags);

        assertTrue(visitId > 0);

        // Assicurati che la lezione sia stata inserita correttamente nel database
        Visit insertedVisit = visitsController.getVisit(visitId);
        assertNotNull(insertedVisit);
    }

    @Test
    void testUpdateVisit() throws Exception {
        visitsController.updateVisit(1, "Updated Math Visit", "Updated Math basics", LocalDateTime.now(), LocalDateTime.now(), 55.0, "doctor1");
        Visit updatedVisit = visitsController.getVisit(1);
        assertEquals("Updated Math Lesson", updatedVisit.getTitle());
        assertEquals("Updated Math basics", updatedVisit.getDescription());
        assertEquals(55.0, updatedVisit.getPrice());
    }

    @Test
    void testDeleteVisit() throws Exception {
        assertTrue(visitsController.removeVisit(1));
        assertNull(visitDAO.get(1));
    }

    @Test
    void testDeleteNonExistentVisit() throws Exception {
        assertFalse(visitsController.removeVisit(99));
    }

    @Test
    void testGetDoctorVisitsByState() throws Exception {
        List<Visit> visits = visitsController.getDoctorVisitByState("doctor2", new Booked("patient1"));
        assertEquals(1, visits.size());
        assertEquals("Physics Visit", visits.get(0).getTitle());
        assertEquals("Booked", visits.get(0).getState());
    }

    @Test
    void testGetStudentBookedVisits() throws Exception {
        List<Visit> visits = visitDAO.getPatientBookedVisits("patient1");
        assertEquals(1, visits.size());
        assertEquals("Physics Visit", visits.get(0).getTitle());
        assertEquals("Booked", visits.get(0).getState());
    }

    @Test
    void testAttachTagToVisit() throws Exception {
        TagZone tz = new TagZone("Firenze");
        TagIsOnline tio = new TagIsOnline("True");
        List<Tag> visitTags = new ArrayList<>();
        visitTags.add(tz);
        visitTags.add(tio);
        int idVisit = visitsController.addVisit("Math Visit", "Math basics", LocalDateTime.now(), LocalDateTime.now(), 50.0, "doctor1", visitTags);

        TagSubject tagSubjectMath = new TagSubject("Math");

        visitsController.attachTag(idVisit, tagSubjectMath);

        List<Tag> tags = tagDAO.getTagsByVisit(idVisit);
        assertEquals(3,tags.size());
    }

    @Test
    void testDetachTagFromVisit() throws Exception {
        TagZone tz = new TagZone("Prato");
        TagIsOnline tio = new TagIsOnline("False");
        tagsController.createTag("Prato", "Zone");
        tagsController.createTag("False", "IsOnline");

        List<Tag> visitTags = new ArrayList<>();
        visitTags.add(tz);
        visitTags.add(tio);
        int idVisit = visitsController.addVisit("Math Visit", "Math basics", LocalDateTime.now(), LocalDateTime.now(), 50.0, "doctor1", visitTags);
        TagSubject tagSubjectMath = new TagSubject("Math");
        visitsController.attachTag(idVisit, tagSubjectMath);

        List<Tag> tagsPre = tagDAO.getTagsByVisit(idVisit);
        assertEquals(3, tagsPre.size());

        boolean result = visitsController.detachTag(idVisit, tagSubjectMath);

        assertTrue(result);
        List<Tag> tagsPost = tagDAO.getTagsByVisit(idVisit);
        assertEquals(2, tagsPost.size());
    }

    @Test
    void testDetachNonExistentTagFromVisit() throws Exception {
        List<Tag> tags = new ArrayList<>();
        int visitId = visitsController.addVisit("Math Visit", "Math basics", LocalDateTime.now(), LocalDateTime.now(), 50.0, "doctor1", tags);

        Tag tag = new TagSubject("TestTag");

        boolean result = visitsController.detachTag(visitId, tag);

        assertFalse(result);
    }
}
