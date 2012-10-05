package app.fourthink.service;

import app.fourthink.model.CabCategory;
import app.fourthink.model.Customer;
import app.fourthink.model.DispatchSummary;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class DispatchActiveSummariesTest {

    @Autowired
    private DispatchService dispatch;

    @Autowired
    private CustomerService customers;

    @Test
    public void summarizesEachActive() {
        Customer maria = customers.register("Maria", "(51) 99999-1234", null);
        dispatch.request(maria.getId(), -30.0, -51.0, "Rua A", CabCategory.NORMAL);
        dispatch.request(maria.getId(), -30.0, -51.0, "Rua B", CabCategory.GRANDE);
        List<DispatchSummary> summaries = dispatch.activeSummaries();
        assertEquals(2, summaries.size());
    }
}
