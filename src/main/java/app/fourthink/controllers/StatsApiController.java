package app.fourthink.controllers;

import app.fourthink.service.DispatchService;
import app.fourthink.service.DriverDirectory;
import app.fourthink.service.FleetService;
import app.fourthink.service.FleetStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/stats")
public class StatsApiController {

    private final FleetService fleet;
    private final DispatchService dispatches;
    private final DriverDirectory drivers;

    @Autowired
    public StatsApiController(FleetService fleet, DispatchService dispatches,
                               DriverDirectory drivers) {
        this.fleet = fleet;
        this.dispatches = dispatches;
        this.drivers = drivers;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> overview() {
        FleetStats stats = fleet.stats();
        Map<String, Object> out = new HashMap<String, Object>();
        out.put("fleetTotal", stats.getTotal());
        out.put("fleetFree", stats.getFree());
        out.put("fleetBusy", stats.getBusy());
        out.put("fleetOffline", stats.getOffline());
        out.put("activeDispatches", dispatches.activeCount());
        out.put("readyCabs", fleet.readyCabCount());
        out.put("driverCount", drivers.count());
        out.put("timestamp", System.currentTimeMillis());
        return out;
    }
}
