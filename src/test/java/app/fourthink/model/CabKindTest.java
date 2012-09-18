package app.fourthink.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CabKindTest {

    @Test
    public void labelsAllCategoriesInTitleCase() {
        assertEquals("Normal", CabKind.label(CabCategory.NORMAL));
        assertEquals("Conforto", CabKind.label(CabCategory.CONFORTO));
        assertEquals("Grande", CabKind.label(CabCategory.GRANDE));
    }
}
