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
public class DriverDirectoryTest {

    @Autowired
    private DriverDirectory directory;

    @Autowired
    private DriverSignupService signup;

    @Autowired
    private CabModelRepository models;

    private Long modelId;

    @Before
    public void setUp() {
        modelId = models.save(new CabModel("Volkswagen", "Gol", CabCategory.NORMAL)).getId();
    }

    @Test
    public void filtersByCategory() {
        signup.signup("Joao", "joao@example.com", "(51) 99999-1111", "ABC100001", "secret123", CabCategory.NORMAL, "ABC-1111", modelId);
        signup.signup("Maria", "maria@example.com", "(51) 99999-2222", "ABC100002", "secret123", CabCategory.CONFORTO, "ABC-2222", modelId);
        assertEquals(1, directory.byCategory(CabCategory.CONFORTO).size());
        assertEquals(2, directory.all().size());
    }
}
