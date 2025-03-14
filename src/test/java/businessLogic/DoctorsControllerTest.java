package businessLogic;

import dao.Database;
import dao.PostgreSQLDoctorDAO;
import dao.DoctorDAO;
import domainModel.Doctor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.*;
import java.sql.SQLException;
import java.util.Optional;

class DoctorsControllerTest {
    private DoctorsController doctorsController;
    private Doctor testDoctor;

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        Database.setDatabase("jdbc:postgresql://localhost:5432/GestionaleTest");
        Database.initDatabase();
    }

    @BeforeEach
    public void init() throws Exception {
        Database.initDatabase();
        resetDatabase();

        DoctorDAO doctorDAO = new PostgreSQLDoctorDAO();
        doctorsController = new DoctorsController(doctorDAO);

        testDoctor = new Doctor("DOCTORCF123", "DoctorName", "DoctorSurname", "DoctorUrgencyLevel");
        doctorDAO.insert(testDoctor);
    }

    private void resetDatabase() throws SQLException {
        try (Connection connection = Database.getConnection()) {
            connection.prepareStatement("DELETE FROM doctors CASCADE;").executeUpdate();
            connection.prepareStatement("ALTER SEQUENCE doctors_id_seq RESTART WITH 1;").executeUpdate();
        }
    }

    @Test
    public void when_AddingNewDoctor_Expect_Success() throws Exception {
        Assertions.assertDoesNotThrow(() -> doctorsController.addPerson("DOCTORCF456", "NewDoctorName", "NewDoctorSurname", "NewDoctorUrgencyLevel"));
        Optional<Doctor> addedDoctor = doctorsController.getPerson("DOCTORCF456");
        Assertions.assertEquals("DOCTORCF456", addedDoctor.get().getCF());
    }

    @Test
    public void when_AddingAlreadyExistingDoctor_Expect_Exception() {
        Assertions.assertThrows(
                Exception.class,
                () -> doctorsController.addPerson("DOCTORCF123", "DuplicateDoctorName", "DuplicateDoctorSurname", "DuplicateDoctorUrgencyLevel"),
                "Expected addPerson() to throw, but it didn't"
        );
    }

    @Test
    public void when_GettingExistingDoctor_Expect_ReturnDoctor() throws Exception {
        Optional<Doctor> retrievedDoctor = doctorsController.getPerson("DOCTORCF123");
        Assertions.assertEquals(testDoctor, retrievedDoctor);
    }

    @Test
    public void when_GettingNonExistingDoctor_Expect_ReturnNull() throws Exception {
        Optional<Doctor> nonExistingDoctor = doctorsController.getPerson("DOCTORCF999");
        Assertions.assertNull(nonExistingDoctor);
    }

    @Test
    public void when_RemovingExistingDoctor_Expect_True() throws Exception {
        boolean removed = doctorsController.removePerson("DOCTORCF123");
        Assertions.assertTrue(removed);
    }

    @Test
    public void when_RemovingNonExistingDoctor_Expect_False() throws Exception {
        boolean removed = doctorsController.removePerson("DOCTORCF999");
        Assertions.assertFalse(removed);
    }
}