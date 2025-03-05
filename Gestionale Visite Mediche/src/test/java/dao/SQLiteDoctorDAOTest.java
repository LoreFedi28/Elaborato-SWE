package test.java.dao;

import domainModel.Tutor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

class SQLiteDoctorDAOTest {
    private SQLiteTutorDAO tutorDAO;

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        // Set up the test database
        Database.setDatabase("test.db");
        Database.initDatabase();
    }

    @BeforeEach
    public void init() throws SQLException {
        Connection connection = Database.getConnection();
        tutorDAO = new SQLiteTutorDAO();

        // Clear the "tutors" table
        connection.prepareStatement("DELETE FROM tutors").executeUpdate();
        // Insert some test data
        connection.prepareStatement("INSERT INTO tutors (cf, name, surname, iban) VALUES ('test1', 'name1', 'surname1', 'iban1')").executeUpdate();
        connection.prepareStatement("INSERT INTO tutors (cf, name, surname, iban) VALUES ('test2', 'name2', 'surname2', 'iban2')").executeUpdate();
    }

    @Test
    void testGetTutorByCF() throws SQLException {
        Tutor tutor = tutorDAO.get("test1");
        Assertions.assertNotNull(tutor);
        Assertions.assertEquals("test1", tutor.getCF());
        Assertions.assertEquals("name1", tutor.getName());
        Assertions.assertEquals("surname1", tutor.getSurname());
        Assertions.assertEquals("iban1", tutor.getIban());
    }

    @Test
    void testGetTutorByCFNonExistent() throws SQLException {
        Tutor tutor = tutorDAO.get("nonexistent");
        Assertions.assertNull(tutor);
    }

    @Test
    void testGetAllTutors() throws SQLException {
        List<Tutor> tutors = tutorDAO.getAll();
        Assertions.assertEquals(2, tutors.size());
    }

    @Test
    void testAddTutor() throws SQLException {
        Tutor tutor = new Tutor("test3", "name3", "surname3", "iban3");
        tutorDAO.insert(tutor);
        List<Tutor> tutors = tutorDAO.getAll();
        Assertions.assertEquals(3, tutors.size());
    }

    @Test
    void testUpdateTutor() throws SQLException {
        Tutor tutor = new Tutor("test1", "name1_updated", "surname1_updated", "iban1_updated");
        tutorDAO.update(tutor);
        Tutor updatedTutor = tutorDAO.get("test1");
        Assertions.assertEquals("name1_updated", updatedTutor.getName());
        Assertions.assertEquals("surname1_updated", updatedTutor.getSurname());
        Assertions.assertEquals("iban1_updated", updatedTutor.getIban());
    }

    @Test
    void testDeleteTutor() throws SQLException {
        boolean result = tutorDAO.delete("test1");
        Assertions.assertTrue(result);
        List<Tutor> tutors = tutorDAO.getAll();
        Assertions.assertEquals(1, tutors.size());
    }

    @Test
    void testDeleteNonExistentTutor() throws SQLException {
        boolean result = tutorDAO.delete("nonexistent");
        Assertions.assertFalse(result);
        List<Tutor> tutors = tutorDAO.getAll();
        Assertions.assertEquals(2, tutors.size());
    }
}
