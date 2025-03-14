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
import java.util.Optional;

class DoctorsControllerTest {
    private DoctorsController doctorsController;
    private Doctor testDoctor;

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        Database.setDatabase("jdbc:postgresql://localhost:5432/GestionaleVisiteMediche_test");
        Database.initDatabase(true);
    }

    @BeforeEach
    public void init() throws Exception {
        resetDatabase();

        DoctorDAO doctorDAO = new PostgreSQLDoctorDAO();
        doctorsController = new DoctorsController(doctorDAO);

        testDoctor = new Doctor("DOCTORCF123", "DoctorName", "DoctorSurname", "DoctorIBAN");
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
        Assertions.assertDoesNotThrow(() -> doctorsController.addPerson("DOCTORCF456", "NewDoctorName", "NewDoctorSurname", "NewDoctorIBAN"));

        Optional<Doctor> addedDoctor = doctorsController.getPerson("DOCTORCF456");
        Assertions.assertTrue(addedDoctor.isPresent(), "Il dottore non è stato trovato nel database");
        Assertions.assertEquals("DOCTORCF456", addedDoctor.get().getCF());
    }

    @Test
    public void when_AddingAlreadyExistingDoctor_Expect_Exception() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> doctorsController.addPerson("DOCTORCF123", "DuplicateDoctorName", "DuplicateDoctorSurname", "DuplicateDoctorIBAN"),
                "Expected addPerson() to throw an IllegalArgumentException, but it didn't"
        );
    }

    @Test
    public void when_GettingExistingDoctor_Expect_ReturnDoctor() throws Exception {
        Optional<Doctor> retrievedDoctor = doctorsController.getPerson("DOCTORCF123");
        Assertions.assertTrue(retrievedDoctor.isPresent(), "Il dottore non è stato trovato");
        Assertions.assertEquals(testDoctor, retrievedDoctor.get());
    }

    @Test
    public void when_GettingNonExistingDoctor_Expect_ReturnEmptyOptional() throws Exception {
        Optional<Doctor> nonExistingDoctor = doctorsController.getPerson("DOCTORCF999");
        Assertions.assertTrue(nonExistingDoctor.isEmpty());
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