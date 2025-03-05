SQLiteLessonDAOTest
        SQLiteStudentDAOTest
        SQLiteTagDAOTest
        SQLiteTutorDAOTestpackage test.java.businessLogic;

import dao.Database;
import dao.SQLiteTutorDAO;
import dao.TutorDAO;
import domainModel.Tutor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

class DoctorsControllerTest {
    private TutorsController tutorsController;
    private Tutor testTutor;

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

        // Crea TutorDAO e controller per tutor
        TutorDAO tutorDAO = new SQLiteTutorDAO();
        tutorsController = new TutorsController(tutorDAO);

        // Crea dati di test (inserisce un tutor)
        testTutor = new Tutor("TUTORCF123", "TutorName", "TutorSurname", "TutorLevel");
        tutorDAO.insert(testTutor);
    }

    private void resetDatabase() throws SQLException {
        Connection connection = Database.getConnection();

        // Cancella dati da tutte le tabelle
        connection.prepareStatement("DELETE FROM tutors").executeUpdate(); // Aggiungi altre tabelle se necessario

        // Reimposta i contatori di autoincremento
        connection.prepareStatement("DELETE FROM sqlite_sequence").executeUpdate();
        Database.closeConnection(connection);
    }


    @Test
    public void when_AddingNewTutor_Expect_Success() throws Exception {
        Assertions.assertDoesNotThrow(() -> tutorsController.addPerson("TUTORCF456", "NewTutorName", "NewTutorSurname", "NewTutorLevel"));
        Tutor addedTutor = tutorsController.getPerson("TUTORCF456");
        Assertions.assertEquals("TUTORCF456", addedTutor.getCF());
    }

    @Test
    public void when_AddingAlreadyExistingTutor_Expect_Exception() {
        Assertions.assertThrows(
                Exception.class,
                () -> tutorsController.addPerson("TUTORCF123", "DuplicateTutorName", "DuplicateTutorSurname", "DuplicateTutorLevel"),
                "Expected addPerson() to throw, but it didn't"
        );
    }

    @Test
    public void when_GettingExistingTutor_Expect_ReturnTutor() throws Exception {
        Tutor retrievedTutor = tutorsController.getPerson("TUTORCF123");
        Assertions.assertEquals(testTutor, retrievedTutor);
    }

    @Test
    public void when_GettingNonExistingTutor_Expect_ReturnNull() throws Exception {
        Tutor nonExistingTutor = tutorsController.getPerson("TUTORCF999");
        Assertions.assertNull(nonExistingTutor);
    }

    @Test
    public void when_RemovingExistingTutor_Expect_True() throws Exception {
        boolean removed = tutorsController.removePerson("TUTORCF123");
        Assertions.assertTrue(removed);
    }

    @Test
    public void when_RemovingNonExistingTutor_Expect_False() throws Exception {
        boolean removed = tutorsController.removePerson("TUTORCF999");
        Assertions.assertFalse(removed);
    }
}
