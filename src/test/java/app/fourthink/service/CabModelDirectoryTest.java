package app.fourthink.service;

import app.fourthink.model.CabCategory;
import app.fourthink.model.CabModel;
import app.fourthink.persistence.CabModelRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class CabModelDirectoryTest {

    @Autowired
    private CabModelDirectory directory;

    @Autowired
    private CabModelRepository models;

    @Before
    public void setUp() {
        models.save(new CabModel("Volkswagen", "Gol", CabCategory.NORMAL));
        models.save(new CabModel("Toyota", "Corolla", CabCategory.CONFORTO));
        models.save(new CabModel("Fiat", "Doblo", CabCategory.GRANDE));
    }

    @Test
    public void filtersByCategory() {
        assertEquals(1, directory.byCategory(CabCategory.CONFORTO).size());
    }

    @Test
    public void searchByMakeIsCaseInsensitive() {
        assertEquals(1, directory.searchByMake("volkswagen").size());
    }

    @Test
    public void filtersByCategoryAndMake() {
        assertEquals(1, directory.byCategoryAndMake(CabCategory.CONFORTO, "toyota").size());
        assertEquals(0, directory.byCategoryAndMake(CabCategory.NORMAL, "toyota").size());
    }
}
