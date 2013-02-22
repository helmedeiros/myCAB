package app.fourthink.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FlowConfig {

    private boolean phoneCallEnabled;
    private boolean requestEnabled;
    private boolean autoDispatchEnabled;
    private int autoDispatchDeclineCap;

    public FlowConfig() {
    }

    public FlowConfig(boolean phoneCallEnabled, boolean requestEnabled,
                       boolean autoDispatchEnabled, int autoDispatchDeclineCap) {
        this.phoneCallEnabled = phoneCallEnabled;
        this.requestEnabled = requestEnabled;
        this.autoDispatchEnabled = autoDispatchEnabled;
        this.autoDispatchDeclineCap = autoDispatchDeclineCap;
    }

    @Value("${flows.phoneCallEnabled:true}")
    public void setPhoneCallEnabled(boolean phoneCallEnabled) {
        this.phoneCallEnabled = phoneCallEnabled;
    }

    @Value("${flows.requestEnabled:true}")
    public void setRequestEnabled(boolean requestEnabled) {
        this.requestEnabled = requestEnabled;
    }

    @Value("${flows.autoDispatchEnabled:false}")
    public void setAutoDispatchEnabled(boolean autoDispatchEnabled) {
        this.autoDispatchEnabled = autoDispatchEnabled;
    }

    @Value("${flows.autoDispatchDeclineCap:3}")
    public void setAutoDispatchDeclineCap(int autoDispatchDeclineCap) {
        this.autoDispatchDeclineCap = autoDispatchDeclineCap;
    }

    public boolean isPhoneCallEnabled() {
        return phoneCallEnabled;
    }

    public boolean isRequestEnabled() {
        return requestEnabled;
    }

    public boolean isAutoDispatchEnabled() {
        return autoDispatchEnabled;
    }

    public int getAutoDispatchDeclineCap() {
        return autoDispatchDeclineCap;
    }

    public int getEnabledCount() {
        int count = 0;
        if (phoneCallEnabled) count++;
        if (requestEnabled) count++;
        if (autoDispatchEnabled) count++;
        return count;
    }

    public boolean isAnyEnabled() {
        return getEnabledCount() > 0;
    }
}
