package java.dao;

import domainModel.Patient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

class SQLitePatientDAOTest {
    private SQLitePatientDAO patientDAO;

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        // Set up the test database
        Database.setDatabase("test.db");
        Database.initDatabase();
    }

    @BeforeEach
    public void init() throws SQLException {
        Connection connection = Database.getConnection();
        patientDAO = new SQLitePatientDAO();

        // Clear the "patients" table
        connection.prepareStatement("DELETE FROM patients").executeUpdate();
        // Insert some test data
        connection.prepareStatement("INSERT INTO patients (cf, name, surname, level) VALUES ('test1', 'name1', 'surname1', 'level1')").executeUpdate();
        connection.prepareStatement("INSERT INTO patients (cf, name, surname, level) VALUES ('test2', 'name2', 'surname2', 'level2')").executeUpdate();
    }

    @Test
    void testGetPatientByCF() throws SQLException {
        // Test the get method to retrieve a patient by CF
        Patient patient = patientDAO.get("test1");
        Assertions.assertNotNull(patient);
        Assertions.assertEquals("test1", patient.getCF());
        Assertions.assertEquals("name1", patient.getName());
        Assertions.assertEquals("surname1", patient.getSurname());
        Assertions.assertEquals("level1", patient.getLevel());
    }

    @Test
    void testGetPatientByCFNonExistent() throws SQLException {
        // Test the get method with a non-existent CF
        Patient patient = patientDAO.get("nonexistent");
        Assertions.assertNull(patient);
    }

    @Test
    void testGetAllPatients() throws SQLException {
        // Test the getAll method to retrieve all patients
        List<Patient> patients = patientDAO.getAll();
        Assertions.assertEquals(2, patients.size());
    }

    @Test
    void testAddPatient() throws SQLException {
        // Test the insert method to add a new patient
        Patient patient = new Patient("test3", "name3", "surname3", "level3");
        patientDAO.insert(patient);
        List<Patient> patients = patientDAO.getAll();
        Assertions.assertEquals(3, patients.size());
    }

    @Test
    void testUpdatePatient() throws SQLException {
        // Test the update method to update a patient's information
        Patient patient = new Patient("test1", "name1_updated", "surname1_updated", "level1_updated");
        patientDAO.update(patient);
        Patient updatedPatient = patientDAO.get("test1");
        Assertions.assertEquals("name1_updated", updatedPatient.getName());
        Assertions.assertEquals("surname1_updated", updatedPatient.getSurname());
        Assertions.assertEquals("level1_updated", updatedPatient.getLevel());
    }

    @Test
    void testDeletePatient() throws SQLException {
        // Test the delete method to delete a patient
        boolean result = patientDAO.delete("test1");
        Assertions.assertTrue(result);
        List<Patient> patients = patientDAO.getAll();
        Assertions.assertEquals(1, patients.size());
    }

    @Test
    void testDeleteNonExistentPatient() throws SQLException {
        // Test the delete method with a non-existent patient
        boolean result = patientDAO.delete("nonexistent");
        Assertions.assertFalse(result);
        List<Patient> patients = patientDAO.getAll();
        Assertions.assertEquals(2, patients.size());
    }
}
