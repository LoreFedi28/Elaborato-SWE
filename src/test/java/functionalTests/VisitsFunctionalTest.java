package functionalTests;

import businessLogic.VisitsController;
import businessLogic.TagsController;
import businessLogic.DoctorsController;
import dao.*;
import domainModel.Tags.Tag;
import domainModel.Tags.TagSpecialty;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("ALL")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VisitsFunctionalTest {

    private VisitsController visitsController;

    @BeforeAll
    void setUp() throws Exception {
        Database.setDatabase("jdbc:postgresql://localhost:5432/GestionaleVisiteMediche_test");
        Database.initDatabase(true);

        PostgreSQLTagDAO tagDAO = new PostgreSQLTagDAO();
        PostgreSQLVisitDAO visitDAO = new PostgreSQLVisitDAO(tagDAO);
        visitsController = new VisitsController(visitDAO, tagDAO, new DoctorsController(new PostgreSQLDoctorDAO()));
        TagsController tagsController = new TagsController(tagDAO);
    }

    @BeforeEach
    void setUpDatabase() throws Exception {
        try (Connection connection = Database.getConnection()) {
            connection.setAutoCommit(false);

            connection.prepareStatement("DELETE FROM visitsTags CASCADE;").executeUpdate();
            connection.prepareStatement("DELETE FROM visits CASCADE;").executeUpdate();
            connection.prepareStatement("DELETE FROM tags CASCADE;").executeUpdate();
            connection.prepareStatement("DELETE FROM doctors CASCADE;").executeUpdate();
            connection.prepareStatement("DELETE FROM patients CASCADE;").executeUpdate();

            connection.prepareStatement("INSERT INTO doctors (cf, name, surname, iban) VALUES ('doctor1', 'Doctor', 'Uno', '1234')").executeUpdate();
            connection.prepareStatement("INSERT INTO doctors (cf, name, surname, iban) VALUES ('doctor2', 'Doctor', 'Due', '5678')").executeUpdate();

            connection.prepareStatement("INSERT INTO patients (cf, name, surname, urgencyLevel) VALUES ('patient1', 'Patient', 'Uno', 'Basso')").executeUpdate();

            connection.prepareStatement("INSERT INTO tags (tag, tagType) VALUES ('Cardiologia', 'Specialty')").executeUpdate();
            connection.prepareStatement("INSERT INTO tags (tag, tagType) VALUES ('Oncologia', 'Specialty')").executeUpdate();

            connection.commit();
        }
    }

    @AfterEach
    void tearDownDatabase() throws Exception {
        try (Connection connection = Database.getConnection()) {
            connection.prepareStatement("DELETE FROM visitsTags CASCADE;").executeUpdate();
            connection.prepareStatement("DELETE FROM visits CASCADE;").executeUpdate();
            connection.prepareStatement("DELETE FROM tags CASCADE;").executeUpdate();
            connection.prepareStatement("DELETE FROM doctors CASCADE;").executeUpdate();
            connection.prepareStatement("DELETE FROM patients CASCADE;").executeUpdate();
        }
    }

    @Test
    void testCreateVisitWithNonExistentDoctor() {
        List<Tag> tags = new ArrayList<>();
        TagSpecialty ts = new TagSpecialty("Neurologia");
        tags.add(ts);

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> visitsController.addVisit("Visita Neurologica", "Controllo neurologico",
                        LocalDateTime.now(), LocalDateTime.now().plusHours(1), 50.0, "nonexistentDoctor", tags)
        );

        assertEquals("Medico non trovato", exception.getMessage());
    }

    @Test
    void testDoctorCannotHaveOverlappingVisits() throws Exception {
        List<Tag> tags = new ArrayList<>();
        visitsController.addVisit("Visita Cardiologica", "Check-up cardiologico",
                LocalDateTime.of(2025, 3, 22, 10, 0),
                LocalDateTime.of(2025, 3, 22, 11, 0),
                60.0, "doctor1", tags);

        Exception exception = assertThrows(
                RuntimeException.class,
                () -> visitsController.addVisit("Visita Neurologica", "Esame neurologico",
                        LocalDateTime.of(2025, 3, 22, 10, 30),
                        LocalDateTime.of(2025, 3, 22, 11, 30),
                        70.0, "doctor1", tags)
        );

        assertTrue(exception.getMessage().contains("Il medico selezionato è già occupato"));
    }
}