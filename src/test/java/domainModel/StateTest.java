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
        // Initialize a state before each test
        state = new Available();
    }

    @Test
    void testAvailableState() {
        // Verify that the "Available" state is created correctly
        Assertions.assertEquals("Available", state.getState());
        Assertions.assertNull(state.getExtraInfo());
    }

    @Test
    void testCompletedState() {
        LocalDateTime completedTime = LocalDateTime.now();
        state = new Completed(completedTime);

        // Verify that the "Completed" state is created correctly
        Assertions.assertEquals("Completed", state.getState());
        Assertions.assertEquals(completedTime.toString(), state.getExtraInfo());
    }

    @Test
    void testCancelledState() {
        LocalDateTime cancelledTime = LocalDateTime.now();
        state = new Cancelled(cancelledTime);

        // Verify that the "Cancelled" state is created correctly
        Assertions.assertEquals("Cancelled", state.getState());
        Assertions.assertEquals(cancelledTime.toString(), state.getExtraInfo());
    }

    @Test
    void testBookedState() {
        String studentCF = "123456";
        state = new Booked(studentCF);

        // Verify that the "Booked" state is created correctly
        Assertions.assertEquals("Booked", state.getState());
        Assertions.assertEquals(studentCF, state.getExtraInfo());
    }
}
