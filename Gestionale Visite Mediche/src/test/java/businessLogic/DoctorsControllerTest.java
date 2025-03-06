package test.java.businessLogic;

import dao.Database;
import dao.SQLiteDoctorDAO;
import dao.DoctorDAO;
import domainModel.Doctor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.io.IOException;
import java.sql.*;
import java.sql.SQLException;

class DoctorsControllerTest {
    private DoctorsController doctorsController;
    private Doctor testDoctor;

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        // Imposta il database di test
        Database.setDatabase("test.db");
        Database.initDatabase();
    }

    @BeforeEach
    public void init() throws Exception {
        Database.initDatabase();
        resetDatabase();

        DoctorDAO doctorDAO = new SQLiteDoctorDAO();
        doctorsController = new DoctorsController(doctorDAO);

        testDoctor = new Doctor("DOCTORCF123", "DoctorName", "DoctorSurname", "DoctorLevel");
        doctorDAO.insert(testDoctor);
    }

    private void resetDatabase() throws SQLException {
        Connection connection = Database.getConnection();

        // Cancella dati da tutte le tabelle
        connection.prepareStatement("DELETE FROM doctors").executeUpdate(); // Aggiungi altre tabelle se necessario

        // Reimposta i contatori di autoincremento
        connection.prepareStatement("DELETE FROM sqlite_sequence").executeUpdate();
        Database.closeConnection(connection);
    }


    @Test
    public void when_AddingNewDoctor_Expect_Success() throws Exception {
        Assertions.assertDoesNotThrow(() -> doctorsController.addPerson("DOCTORCF456", "NewDoctorName", "NewDoctorSurname", "NewDoctorLevel"));
        Doctor addedDoctor = doctorsController.getPerson("DOCTORCF456");
        Assertions.assertEquals("DOCTORCF456", addedDoctor.getCF());
    }

    @Test
    public void when_AddingAlreadyExistingDoctor_Expect_Exception() {
        Assertions.assertThrows(
                Exception.class,
                () -> doctorsController.addPerson("DOCTORCF123", "DuplicateDoctorName", "DuplicateDoctorSurname", "DuplicateDoctorLevel"),
                "Expected addPerson() to throw, but it didn't"
        );
    }

    @Test
    public void when_GettingExistingDoctor_Expect_ReturnDoctor() throws Exception {
        Doctor retrievedDoctor = doctorsController.getPerson("DOCTORCF123");
        Assertions.assertEquals(testDoctor, retrievedDoctor);
    }

    @Test
    public void when_GettingNonExistingDoctor_Expect_ReturnNull() throws Exception {
        Doctor nonExistingDoctor = doctorsController.getPerson("DOCTORCF999");
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
