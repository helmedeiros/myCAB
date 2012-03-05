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

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class JpaCabModelRepositoryTest {

    @Autowired
    private CabModelRepository repository;

    @Test
    public void persistsAndRetrievesCabModel() {
        CabModel saved = repository.save(new CabModel("Volkswagen", "Gol", CabCategory.NORMAL));
        assertNotNull(saved.getId());
        CabModel found = repository.findById(saved.getId());
        assertEquals("Volkswagen", found.getMake());
    }

    @Test
    public void findsByCategory() {
        repository.save(new CabModel("Toyota", "Corolla", CabCategory.CONFORTO));
        repository.save(new CabModel("Fiat", "Palio", CabCategory.NORMAL));
        List<CabModel> conforto = repository.findByCategory(CabCategory.CONFORTO);
        assertEquals(1, conforto.size());
        assertEquals("Toyota", conforto.get(0).getMake());
    }

    @Test
    public void listsAllOrdered() {
        repository.save(new CabModel("Volkswagen", "Voyage", CabCategory.NORMAL));
        repository.save(new CabModel("Chevrolet", "Celta", CabCategory.NORMAL));
        List<CabModel> all = repository.findAll();
        assertEquals("Chevrolet", all.get(0).getMake());
    }
}
