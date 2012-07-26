package app.fourthink.service;

import app.fourthink.model.CabCategory;
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

    @Test
    public void filtersByCategory() {
        signup.signup("Joao", "joao@example.com", "(51) 99999-1111", "ABC100001", "secret123", CabCategory.NORMAL);
        signup.signup("Maria", "maria@example.com", "(51) 99999-2222", "ABC100002", "secret123", CabCategory.CONFORTO);
        assertEquals(1, directory.byCategory(CabCategory.CONFORTO).size());
        assertEquals(2, directory.all().size());
    }
}
