package app.fourthink.persistence;

import app.fourthink.model.CabCategory;
import app.fourthink.model.CabModel;
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
public class CabModelSearchTest {

    @Autowired
    private CabModelRepository repository;

    @Test
    public void searchIsCaseInsensitive() {
        repository.save(new CabModel("Volkswagen", "Gol", CabCategory.NORMAL));
        assertEquals(1, repository.searchByMake("VOLKSWAGEN").size());
        assertEquals(1, repository.searchByMake("volkswagen").size());
    }
}
