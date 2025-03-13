package domainModel;

import domainModel.State.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class StateTest {
    private State state;

    @BeforeEach
    void setUp() {
        // Inizializza lo stato di default come Available
        state = new Available();
    }

    @Test
    void testAvailableState() {
        // Verifica che lo stato "Available" sia creato correttamente
        Assertions.assertEquals("Available", state.getState());
        Assertions.assertNull(state.getExtraInfo());
    }

    @Test
    void testCompletedState() {
        LocalDateTime completedTime = LocalDateTime.now();
        state = new Completed(completedTime);

        // Verifica che lo stato "Completed" sia creato correttamente
        Assertions.assertEquals("Completed", state.getState());
        Assertions.assertTrue(completedTime.isEqual(LocalDateTime.parse(state.getExtraInfo())));
    }

    @Test
    void testCancelledState() {
        LocalDateTime cancelledTime = LocalDateTime.now();
        state = new Cancelled(cancelledTime);

        // Verifica che lo stato "Cancelled" sia creato correttamente
        Assertions.assertEquals("Cancelled", state.getState());
        Assertions.assertTrue(cancelledTime.isEqual(LocalDateTime.parse(state.getExtraInfo())));
    }

    @Test
    void testBookedState() {
        String patientCF = "123456";
        state = new Booked(patientCF);

        // Verifica che lo stato "Booked" sia creato correttamente
        Assertions.assertEquals("Booked", state.getState());
        Assertions.assertEquals(patientCF, state.getExtraInfo());
    }
}