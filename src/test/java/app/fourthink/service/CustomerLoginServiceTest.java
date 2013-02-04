package app.fourthink.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class CustomerLoginServiceTest {

    @Autowired
    private CustomerLoginService login;

    @Autowired
    private CustomerSignupService signup;

    @Before
    public void setUp() {
        signup.signup("Maria Silva", "maria@example.com",
                "(51) 99999-1234", "secret123", null);
    }

    @Test
    public void authenticatesValidCredentials() {
        assertNotNull(login.authenticate("maria@example.com", "secret123"));
    }

    @Test
    public void rejectsWrongPassword() {
        assertNull(login.authenticate("maria@example.com", "wrong"));
    }

    @Test
    public void rejectsUnknownEmail() {
        assertNull(login.authenticate("nobody@example.com", "secret123"));
    }

    @Test
    public void emailIsCaseInsensitive() {
        assertEquals("maria@example.com",
                login.authenticate("Maria@Example.COM", "secret123").getEmail());
    }
}
