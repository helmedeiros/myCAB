package app.fourthink.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CabStatusTest {

    @Test
    public void hasThreeValues() {
        assertEquals(3, CabStatus.values().length);
    }

    @Test
    public void freeIsDistinctFromBusy() {
        assertEquals(CabStatus.FREE, CabStatus.valueOf("FREE"));
        assertEquals(CabStatus.BUSY, CabStatus.valueOf("BUSY"));
    }
}
