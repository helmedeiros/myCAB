package app.fourthink.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RecipientKindTest {

    @Test
    public void hasThreeKinds() {
        assertEquals(3, RecipientKind.values().length);
    }

    @Test
    public void includesCabAndCustomer() {
        assertEquals(RecipientKind.CAB, RecipientKind.valueOf("CAB"));
        assertEquals(RecipientKind.CUSTOMER, RecipientKind.valueOf("CUSTOMER"));
        assertEquals(RecipientKind.OPERATOR, RecipientKind.valueOf("OPERATOR"));
    }
}
