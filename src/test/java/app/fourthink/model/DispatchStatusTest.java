package app.fourthink.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DispatchStatusTest {

    @Test
    public void hasFourStates() {
        assertEquals(4, DispatchStatus.values().length);
    }

    @Test
    public void canRoundTripThroughValueOf() {
        for (DispatchStatus s : DispatchStatus.values()) {
            assertEquals(s, DispatchStatus.valueOf(s.name()));
        }
    }
}
