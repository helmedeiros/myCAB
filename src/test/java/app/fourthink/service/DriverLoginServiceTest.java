package app.fourthink.service;

import app.fourthink.model.CabCategory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class DriverLoginServiceTest {

    @Autowired
    private DriverLoginService login;

    @Autowired
    private DriverSignupService signup;

    @Before
    public void setUp() {
        signup.signup("Jose Motorista", "jose@example.com", "(51) 99999-0000",
                "ABC123456", "secret123", CabCategory.NORMAL);
    }

    @Test
    public void authenticatesValidCredentials() {
        assertNotNull(login.authenticate("jose@example.com", "secret123"));
    }

    @Test
    public void rejectsWrongPassword() {
        assertNull(login.authenticate("jose@example.com", "wrong"));
    }

    @Test
    public void rejectsUnknownEmail() {
        assertNull(login.authenticate("nobody@example.com", "secret123"));
    }

    @Test
    public void emailIsCaseInsensitive() {
        assertEquals("jose@example.com",
                login.authenticate("JOSE@Example.com", "secret123").getEmail());
    }
}
