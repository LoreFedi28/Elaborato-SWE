package dao;

import domainModel.Visit;
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
    private SQLiteVisitDAO visitDAO;
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
        visitDAO = new SQLiteVisitDAO(tagDAO);

        // Clear the "visits" and "tags" tables
        connection.prepareStatement("DELETE FROM visits").executeUpdate();
        connection.prepareStatement("DELETE FROM tags").executeUpdate();

        // Insert some test data
        connection.prepareStatement("INSERT INTO tags (tag, tagType) VALUES ('Math', 'Subject')").executeUpdate();
        connection.prepareStatement("INSERT INTO tags (tag, tagType) VALUES ('Physics', 'Subject')").executeUpdate();

        connection.prepareStatement("INSERT INTO visits (idVisit, title, description, startTime, endTime, price, doctorCF, state, stateExtraInfo) VALUES (1, 'Math Visit', 'Math basics', '2023-10-25T10:00:00', '2023-10-25T11:00:00', 50.0, 'doctor1', 'Available', NULL)").executeUpdate();
        connection.prepareStatement("INSERT INTO visits (idVisit, title, description, startTime, endTime, price, doctorCF, state, stateExtraInfo) VALUES (2, 'Physics Visit', 'Physics basics', '2023-10-26T10:00:00', '2023-10-26T11:00:00', 60.0, 'doctor2', 'Booked', 'patient1')").executeUpdate();
    }

    @Test
    void testGetVisitByID() throws Exception {
        // Test the get method to retrieve a visit by ID
        Visit visit = visitDAO.get(1);
        Assertions.assertNotNull(visit);
        Assertions.assertEquals("Math Visit", visit.getTitle());
        Assertions.assertEquals("Math basics", visit.getDescription());
        Assertions.assertEquals(50.0, visit.getPrice());
        Assertions.assertEquals("Available", visit.getState());
    }

    @Test
    void testGetVisitByIDNonExistent() throws Exception {
        // Test the get method with a non-existent ID
        Visit visit = visitDAO.get(3);
        Assertions.assertNull(visit);
    }

    @Test
    void testGetAllVisits() throws Exception {
        // Test the getAll method to retrieve all visits
        List<Visit> visits = visitDAO.getAll();
        Assertions.assertEquals(2, visits.size());
    }

    @Test
    void testInsertVisit() throws Exception {
        // Test the insert method to add a new visit
        Visit visit = new Visit(3, "English Visit", "English basics", LocalDateTime.now(), LocalDateTime.now(), 40.0, "doctor3");
        visitDAO.insert(visit);
        List<Visit> visits = visitDAO.getAll();
        Assertions.assertEquals(3, visits.size());
    }

    @Test
    void testUpdateVisit() throws Exception {
        // Test the update method to update a visit's information
        Visit visit = new Visit(1, "Updated Math Visit", "Updated Math basics", LocalDateTime.now(), LocalDateTime.now(), 55.0, "doctor1");
        visitDAO.update(visit);
        Visit updatedVisit = visitDAO.get(1);
        Assertions.assertEquals("Updated Math Visit", updatedVisit.getTitle());
        Assertions.assertEquals("Updated Math basics", updatedVisit.getDescription());
        Assertions.assertEquals(55.0, updatedVisit.getPrice());
    }

    @Test
    void testDeleteVisit() throws Exception {
        // Test the delete method to delete a visit
        boolean result = visitDAO.delete(1);
        Assertions.assertTrue(result);
        List<Visit> visits = visitDAO.getAll();
        Assertions.assertEquals(1, visits.size());
    }

    @Test
    void testDeleteNonExistentVisit() throws Exception {
        // Test the delete method with a non-existent visit
        boolean result = visitDAO.delete(3);
        Assertions.assertFalse(result);
        List<Visit> visits = visitDAO.getAll();
        Assertions.assertEquals(2, visits.size());
    }

    @Test
    void testGetDoctorVisitsByState() throws Exception {
        // Test the method to get visits by doctor and state
        List<Visit> visits = visitDAO.getDoctorVisitsByState("doctor2", new Booked("patient1"));
        Assertions.assertEquals(1, visits.size());
        Assertions.assertEquals("Physics Visit", visits.get(0).getTitle());
        Assertions.assertEquals("Booked", visits.get(0).getState());
    }

    @Test
    void testGetPatientBookedVisits() throws Exception {
        // Test the method to get visits booked by a patient
        List<Visit> visits = visitDAO.getPatientBookedVisits("patient1");
        Assertions.assertEquals(1, visits.size());
        Assertions.assertEquals("Physics Visit", visits.get(0).getTitle());
        Assertions.assertEquals("Booked", visits.get(0).getState());
    }
}