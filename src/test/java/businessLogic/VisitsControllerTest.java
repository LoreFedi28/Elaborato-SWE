package businessLogic;

import dao.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import domainModel.State.Booked;
import domainModel.Tags.Tag;
import domainModel.Tags.TagIsOnline;
import domainModel.Tags.TagSpecialty;
import domainModel.Tags.TagZone;
import domainModel.Visit;
import org.junit.jupiter.api.*;

class VisitsControllerTest {

    private static VisitsController visitsController;
    private static PostgreSQLTagDAO tagDAO;
    private static PostgreSQLVisitDAO visitDAO;
    private static TagsController tagsController;

    @BeforeAll
    static void setUp() throws Exception {
        Database.setDatabase("jdbc:postgresql://localhost:5432/GestionaleVisiteMediche_test");
        Database.initDatabase(true);

        tagDAO = new PostgreSQLTagDAO();
        visitDAO = new PostgreSQLVisitDAO(tagDAO);
        visitsController = new VisitsController(visitDAO, tagDAO, new DoctorsController(new PostgreSQLDoctorDAO()));
        tagsController = new TagsController(tagDAO);
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

            connection.prepareStatement("INSERT INTO doctors (cf, name, surname, iban) VALUES ('doctor1', 'Doctor', 'Uno', 1234)").executeUpdate();
            connection.prepareStatement("INSERT INTO doctors (cf, name, surname, iban) VALUES ('doctor2', 'Doctor', 'Due', 5678)").executeUpdate();

            connection.prepareStatement("INSERT INTO patients (cf, name, surname, urgencyLevel) VALUES ('patient1', 'Patient', 'Uno', 'Basso')").executeUpdate();

            connection.prepareStatement("INSERT INTO tags (tag, tagType) VALUES ('Cardiologia', 'Specialty')").executeUpdate();
            connection.prepareStatement("INSERT INTO tags (tag, tagType) VALUES ('Oncologia', 'Specialty')").executeUpdate();

            connection.prepareStatement("INSERT INTO visits (title, description, startTime, endTime, price, doctorCF, state, stateExtraInfo) VALUES ('Visita Cardiologica', 'Prima visita cardiologica', '2023-10-25 10:00:00', '2023-10-25 11:00:00', 50.0, 'doctor1', 'Available', NULL)").executeUpdate();
            connection.prepareStatement("INSERT INTO visits (title, description, startTime, endTime, price, doctorCF, state, stateExtraInfo) VALUES ('Visita Oncologica', 'Prima visita oncologica', '2023-10-26 10:00:00', '2023-10-26 11:00:00', 60.0, 'doctor2', 'Booked', 'patient1')").executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la preparazione del database di test", e);
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
    void testGetVisitByID() throws Exception {
        List<Visit> visits = visitsController.getAll();
        Assertions.assertFalse(visits.isEmpty(), "Non ci sono visite nel database!");

        int visitId = visits.get(0).getIdVisit(); // Ottieni il primo ID
        System.out.println("DEBUG: Test sulla visita con ID " + visitId);

        Visit visit = visitsController.getVisit(visitId);
        Assertions.assertNotNull(visit, "La visita non esiste nel database!");
        Assertions.assertEquals("Visita Cardiologica", visit.getTitle());
        Assertions.assertEquals("Prima visita cardiologica", visit.getDescription());
        Assertions.assertEquals(50.0, visit.getPrice());
        Assertions.assertEquals("Available", visit.getState());
    }

    @Test
    void testGetVisitByIDNonExistent() throws Exception {
        Visit visit = visitsController.getVisit(3);
        Assertions.assertNull(visit);
    }

    @Test
    void testGetAllVisits() throws Exception {
        List<Visit> visits = visitsController.getAll();
        Assertions.assertEquals(2, visits.size());
    }

    @Test
    void testInsertVisit() throws Exception {
        List<Tag> tags = new ArrayList<>();
        TagSpecialty ts = new TagSpecialty("Ortopedia");
        tags.add(ts);
        int visitId = visitsController.addVisit("Visita Ortopedica", "Prima visita ortopedica", LocalDateTime.now(), LocalDateTime.now(), 40.0, "doctor2", tags);

        Assertions.assertTrue(visitId > 0);

        Visit insertedVisit = visitsController.getVisit(visitId);
        Assertions.assertNotNull(insertedVisit);
    }

    @Test
    void testUpdateVisit() throws Exception {
        List<Visit> visits = visitsController.getAll();
        Assertions.assertFalse(visits.isEmpty(), "Non ci sono visite nel database!");

        int visitId = visits.get(0).getIdVisit(); // Ottieni un ID valido
        System.out.println("DEBUG: Tentativo di aggiornare la visita con ID " + visitId);

        visitsController.updateVisit(visitId, "Updated Visita Cardiologica", "Updated Prima visita cardiologica",
                LocalDateTime.now(), LocalDateTime.now().plusHours(1), 55.0, "doctor1");

        Visit updatedVisit = visitsController.getVisit(visitId);
        Assertions.assertNotNull(updatedVisit, "La visita aggiornata non esiste!");
        Assertions.assertEquals("Updated Visita Cardiologica", updatedVisit.getTitle());
        Assertions.assertEquals("Updated Prima visita cardiologica", updatedVisit.getDescription());
        Assertions.assertEquals(55.0, updatedVisit.getPrice());
    }


    @Test
    void testDeleteVisit() throws Exception {
        List<Visit> visits = visitsController.getAll();
        Assertions.assertFalse(visits.isEmpty(), "Non ci sono visite nel database!");

        int visitId = visits.get(0).getIdVisit(); // Ottieni l'ID della prima visita
        System.out.println("DEBUG: Tentativo di eliminare la visita con ID " + visitId);

        boolean deleted = visitsController.removeVisit(visitId);
        System.out.println("DEBUG: Risultato eliminazione: " + deleted);

        Assertions.assertTrue(deleted, "La visita non è stata eliminata!");
        Assertions.assertNull(visitDAO.get(visitId), "La visita dovrebbe essere null dopo la rimozione!");
    }

    @Test
    void testDeleteNonExistentVisit() throws Exception {
        Assertions.assertFalse(visitsController.removeVisit(99));
    }

    @Test
    void testGetDoctorVisitsByState() throws Exception {
        List<Visit> visits = visitsController.getDoctorVisitByState("doctor2", new Booked("patient1"));
        Assertions.assertEquals(1, visits.size());
        Assertions.assertEquals("Visita Oncologica", visits.get(0).getTitle());
        Assertions.assertEquals("Booked", visits.get(0).getState());
    }

    @Test
    void testGetPatientBookedVisits() throws Exception {
        List<Visit> visits = visitDAO.getPatientBookedVisits("patient1");
        Assertions.assertEquals(1, visits.size());
        Assertions.assertEquals("Visita Oncologica", visits.get(0).getTitle());
        Assertions.assertEquals("Booked", visits.get(0).getState());
    }

    @Test
    void testAttachTagToVisit() throws Exception {
        TagSpecialty tagSpecialty = new TagSpecialty("Neurologia");

        // Assicuriamoci che il tag esista prima di attaccarlo
        tagsController.createTag("Neurologia", "Specialty");

        List<Tag> visitTags = new ArrayList<>();
        visitTags.add(tagSpecialty);

        int idVisit = visitsController.addVisit("Visita Neurologica", "Prima visita neurologica", LocalDateTime.now(), LocalDateTime.now().plusHours(1), 70.0, "doctor1", visitTags);

        System.out.println("DEBUG: Tentativo di attaccare il tag 'Neurologia' alla visita con ID " + idVisit);

        visitsController.attachTag(idVisit, tagSpecialty);

        List<Tag> tags = tagDAO.getTagsByVisit(idVisit);
        Assertions.assertFalse(tags.isEmpty(), "Il tag non è stato attaccato correttamente!");
    }

    @Test
    void testDetachTagFromVisit() throws Exception {
        // Creazione dei tag
        tagsController.createTag("Firenze", "Zone");
        tagsController.createTag("False", "Online");
        tagsController.createTag("Dermatologia", "Specialty");

        // Verifica che i tag siano stati creati
        List<Tag> allTags = tagDAO.getAllTags();
        System.out.println("DEBUG: Tag presenti nel database: " + allTags);
        Assertions.assertFalse(allTags.isEmpty(), "I tag non sono stati creati correttamente!");

        // Creazione degli oggetti tag
        TagZone tz = new TagZone("Firenze");
        TagIsOnline tio = new TagIsOnline("False");
        TagSpecialty tagSpecialty = new TagSpecialty("Dermatologia");

        // Aggiunta della visita con i tag
        List<Tag> visitTags = new ArrayList<>();
        visitTags.add(tz);
        visitTags.add(tio);
        int idVisit = visitsController.addVisit("Visita Dermatologica", "Controllo nei", LocalDateTime.now(), LocalDateTime.now().plusHours(1), 45.0, "doctor1", visitTags);

        // Attacca il terzo tag alla visita
        visitsController.attachTag(idVisit, tagSpecialty);

        // Controllo dei tag prima della rimozione
        System.out.println("DEBUG: Controllo tag prima della rimozione per la visita con ID: " + idVisit);
        List<Tag> tagsPre = tagDAO.getTagsByVisit(idVisit);
        System.out.println("DEBUG: Tag attualmente associati alla visita: " + tagsPre);
        Assertions.assertEquals(3, tagsPre.size(), "Il numero di tag prima della rimozione non è corretto!");

        // Rimuoviamo il tag
        boolean result = visitsController.detachTag(idVisit, tagSpecialty);
        Assertions.assertTrue(result, "La rimozione del tag non ha avuto successo!");

        // Controllo finale
        List<Tag> tagsPost = tagDAO.getTagsByVisit(idVisit);
        Assertions.assertEquals(2, tagsPost.size(), "Il numero di tag dopo la rimozione non è corretto!");
    }

    @Test
    void testDetachNonExistentTagFromVisit() throws Exception {
        List<Tag> tags = new ArrayList<>();
        int visitId = visitsController.addVisit("Visita Cardiologica", "Prima visita cardiologica", LocalDateTime.now(), LocalDateTime.now(), 50.0, "doctor1", tags);

        Tag tag = new TagSpecialty("TestTag");

        boolean result = visitsController.detachTag(visitId, tag);

        Assertions.assertFalse(result);
    }
}