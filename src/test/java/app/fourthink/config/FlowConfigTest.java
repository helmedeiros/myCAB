package app.fourthink.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
public class FlowConfigTest {

    @Autowired
    private FlowConfig config;

    @Test
    public void loadsDefaultsFromFlowsProperties() {
        assertTrue(config.isPhoneCallEnabled());
        assertTrue(config.isRequestEnabled());
        assertTrue(config.isAutoDispatchEnabled());
        assertEquals(3, config.getAutoDispatchDeclineCap());
    }

    @Test
    public void enabledCountReflectsToggles() {
        assertEquals(3, config.getEnabledCount());
        assertTrue(config.isAnyEnabled());
    }

    @Test
    public void manualBuildEchoesValues() {
        FlowConfig only = new FlowConfig(false, false, true, 5);
        assertEquals(1, only.getEnabledCount());
        assertEquals(5, only.getAutoDispatchDeclineCap());
        assertTrue(only.isAutoDispatchEnabled());
    }
}
