package domainModel;

import domainModel.State.*;
import domainModel.Tags.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class VisitTest {
    private Visit visit;

    @BeforeEach
    void setUp() {
        // Inizializza una nuova visita prima di ogni test
        visit = new Visit(1, "Medical Checkup", "General health checkup", LocalDateTime.now(), LocalDateTime.now().plusHours(1), 10.0, "12345");
    }

    @Test
    void testVisitCreationWithValidDates() {
        // Assicura che la creazione di una visita con date valide non lanci un'eccezione
        Assertions.assertDoesNotThrow(() -> new Visit(2, "Dental Checkup", "Routine dental checkup", LocalDateTime.now(), LocalDateTime.now().plusHours(2), 20.0, "67890"));
    }

    @Test
    void testVisitCreationWithInvalidDates() {
        // Assicura che la creazione di una visita con data di fine precedente a quella di inizio generi un'eccezione
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Visit(3, "Invalid Visit", "Invalid dates", LocalDateTime.now(), LocalDateTime.now().minusHours(1), 5.0, "54321"));
    }

    @Test
    void testGetters() {
        // Verifica i metodi di getter
        Assertions.assertEquals(1, visit.getIdVisit());
        Assertions.assertEquals("Medical Checkup", visit.getTitle());
        Assertions.assertEquals("General health checkup", visit.getDescription());
        Assertions.assertNotNull(visit.getStartTime());
        Assertions.assertNotNull(visit.getEndTime());
        Assertions.assertEquals(10.0, visit.getPrice());
        Assertions.assertEquals("12345", visit.getDoctorCF());
    }

    @Test
    void testSetState() {
        // Verifica il metodo di impostazione dello stato
        Available availableState = new Available();
        visit.setState(availableState);
        Assertions.assertEquals("Available", visit.getState());
        Assertions.assertNull(visit.getStateExtraInfo());
    }

    @Test
    void testTagOperations() {
        // Verifica i metodi di gestione dei tag
        TagZone tagZone = new TagZone("Firenze");
        TagSubject tagSubject = new TagSubject("General Medicine");

        // Aggiungi un tag
        visit.addTag(tagZone);
        visit.addTag(tagSubject);
        Assertions.assertTrue(visit.getTags().contains(tagZone));

        // Rimuovi un tag
        visit.removeTag("Zone", "Firenze");
        Assertions.assertFalse(visit.getTags().contains(tagZone));

        // Verifica che il metodo removeTag lanci un'eccezione quando si cerca di rimuovere un tag con un tagType non valido
        Assertions.assertThrows(IllegalArgumentException.class, () -> visit.removeTag("InvalidType", "Tag1"));

        // Tentativo di rimuovere un tag che non esiste
        boolean removed = visit.removeTag("Zone", "Tag2");
        Assertions.assertFalse(removed);
    }
}