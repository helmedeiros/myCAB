package app.fourthink.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CabCategoryTest {

    @Test
    public void coversNormalConfortoAndGrande() {
        assertEquals(3, CabCategory.values().length);
        assertEquals(CabCategory.NORMAL, CabCategory.valueOf("NORMAL"));
        assertEquals(CabCategory.CONFORTO, CabCategory.valueOf("CONFORTO"));
        assertEquals(CabCategory.GRANDE, CabCategory.valueOf("GRANDE"));
    }
}
