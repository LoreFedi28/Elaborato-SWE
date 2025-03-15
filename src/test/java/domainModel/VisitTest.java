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
        visit = new Visit(1, "Visita Pneumologica", "Controllo Asmatico", LocalDateTime.now(), LocalDateTime.now().plusHours(1), 10.0, "12345");
    }

    @Test
    void testVisitCreationWithValidDates() {
        Assertions.assertDoesNotThrow(() -> new Visit(2, "Visita Diabetologica", "Controllo Di Routine Diabetologico", LocalDateTime.now(), LocalDateTime.now().plusHours(2), 20.0, "67890"));
    }

    @Test
    void testVisitCreationWithInvalidDates() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Visit(3, "Invalid Visit", "Invalid dates", LocalDateTime.now(), LocalDateTime.now().minusHours(1), 5.0, "54321"));
    }

    @Test
    void testGetters() {
        Assertions.assertEquals(1, visit.getIdVisit());
        Assertions.assertEquals("Visita Pneumologica", visit.getTitle());
        Assertions.assertEquals("Controllo Asmatico", visit.getDescription());
        Assertions.assertNotNull(visit.getStartTime());
        Assertions.assertNotNull(visit.getEndTime());
        Assertions.assertEquals(10.0, visit.getPrice());
        Assertions.assertEquals("12345", visit.getDoctorCF());
    }

    @Test
    void testSetState() {
        Available availableState = new Available();
        visit.setState(availableState);
        Assertions.assertEquals("Available", visit.getState());
        Assertions.assertNull(visit.getStateExtraInfo());
    }

    @Test
    void testTagOperations() {
        TagZone tagZone = new TagZone("Firenze");
        TagSpecialty tagSpecialty = new TagSpecialty("Pneumologia");

        visit.addTag(tagZone);
        visit.addTag(tagSpecialty);
        Assertions.assertTrue(visit.getTags().contains(tagZone));

        visit.removeTag("Zone", "Firenze");
        Assertions.assertFalse(visit.getTags().contains(tagZone));

        Assertions.assertThrows(IllegalArgumentException.class, () -> visit.removeTag("InvalidType", "Tag1"));

        boolean removed = visit.removeTag("Zone", "Tag2");
        Assertions.assertFalse(removed);
    }
}