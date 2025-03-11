package dao;

import domainModel.Doctor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

class PostgreSQLDoctorDAOTest {
    private PostgreSQLDoctorDAO doctorDAO;

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        // Set up the test database
        Database.setDatabase("test.db");
        Database.initDatabase();
    }

    @BeforeEach
    public void init() throws SQLException {
        Connection connection = Database.getConnection();
        doctorDAO = new PostgreSQLDoctorDAO();

        // Clear the "doctors" table
        connection.prepareStatement("DELETE FROM doctors").executeUpdate();
        // Insert some test data
        connection.prepareStatement("INSERT INTO doctors (cf, name, surname, iban) VALUES ('test1', 'name1', 'surname1', 'iban1')").executeUpdate();
        connection.prepareStatement("INSERT INTO doctors (cf, name, surname, iban) VALUES ('test2', 'name2', 'surname2', 'iban2')").executeUpdate();
    }

    @Test
    void testGetDoctorByCF() throws SQLException {
        Doctor doctor = doctorDAO.get("test1");
        Assertions.assertNotNull(doctor);
        Assertions.assertEquals("test1", doctor.getCF());
        Assertions.assertEquals("name1", doctor.getName());
        Assertions.assertEquals("surname1", doctor.getSurname());
        Assertions.assertEquals("iban1", doctor.getIban());
    }

    @Test
    void testGetDoctorByCFNonExistent() throws SQLException {
        Doctor doctor = doctorDAO.get("nonexistent");
        Assertions.assertNull(doctor);
    }

    @Test
    void testGetAllDoctors() throws SQLException {
        List<Doctor> doctor = doctorDAO.getAll();
        Assertions.assertEquals(2, doctor.size());
    }

    @Test
    void testAddDoctor() throws SQLException {
        Doctor doctor = new Doctor("test3", "name3", "surname3", "iban3");
        doctorDAO.insert(doctor);
        List<Doctor> doctors = doctorDAO.getAll();
        Assertions.assertEquals(3, doctors.size());
    }

    @Test
    void testUpdateDoctor() throws SQLException {
        Doctor doctor = new Doctor("test1", "name1_updated", "surname1_updated", "iban1_updated");
        doctorDAO.update(doctor);
        Doctor updatedDoctor = doctorDAO.get("test1");
        Assertions.assertEquals("name1_updated", updatedDoctor.getName());
        Assertions.assertEquals("surname1_updated", updatedDoctor.getSurname());
        Assertions.assertEquals("iban1_updated", updatedDoctor.getIban());
    }

    @Test
    void testDeleteDoctor() throws SQLException {
        boolean result = doctorDAO.delete("test1");
        Assertions.assertTrue(result);
        List<Doctor> doctors = doctorDAO.getAll();
        Assertions.assertEquals(1, doctors.size());
    }

    @Test
    void testDeleteNonExistentDoctor() throws SQLException {
        boolean result = doctorDAO.delete("nonexistent");
        Assertions.assertFalse(result);
        List<Doctor> doctors = doctorDAO.getAll();
        Assertions.assertEquals(2, doctors.size());
    }
}
