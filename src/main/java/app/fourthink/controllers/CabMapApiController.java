package app.fourthink.controllers;

import app.fourthink.model.Cab;
import app.fourthink.model.Dispatch;
import app.fourthink.model.DispatchStatus;
import app.fourthink.persistence.CabRepository;
import app.fourthink.persistence.DispatchRepository;
import app.fourthink.service.DispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/cab-map")
public class CabMapApiController {

    private final CabRepository cabs;
    private final DispatchService dispatches;
    private final DispatchRepository dispatchRepository;

    @Autowired
    public CabMapApiController(CabRepository cabs, DispatchService dispatches,
                                DispatchRepository dispatchRepository) {
        this.cabs = cabs;
        this.dispatches = dispatches;
        this.dispatchRepository = dispatchRepository;
    }

    @RequestMapping(value = "/{cabId}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> snapshot(@PathVariable Long cabId) {
        Map<String, Object> out = new LinkedHashMap<String, Object>();
        Cab cab = cabs.findById(cabId);
        if (cab == null) {
            out.put("cab", null);
            return out;
        }
        Map<String, Object> cabPayload = new LinkedHashMap<String, Object>();
        cabPayload.put("id", cab.getId());
        cabPayload.put("fleetId", cab.getFleetId());
        cabPayload.put("plate", cab.getPlate().getValue());
        cabPayload.put("status", cab.getStatus().name());
        if (cab.hasLocation()) {
            cabPayload.put("lat", cab.getLocation().getLatitude());
            cabPayload.put("lon", cab.getLocation().getLongitude());
        }
        out.put("cab", cabPayload);
        out.put("currentDispatch", currentDispatchFor(cab.getId()));
        out.put("timestamp", System.currentTimeMillis());
        return out;
    }

    private Map<String, Object> currentDispatchFor(Long cabId) {
        Dispatch proposed = dispatchRepository.findProposedFor(cabId);
        if (proposed != null) {
            return project(proposed, false);
        }
        for (Dispatch d : dispatches.forCab(cabId)) {
            if (d.getStatus() == DispatchStatus.ASSIGNED) {
                return project(d, true);
            }
        }
        return null;
    }

    private Map<String, Object> project(Dispatch d, boolean revealCustomer) {
        Map<String, Object> out = new LinkedHashMap<String, Object>();
        out.put("id", d.getId());
        out.put("status", d.getStatus().name());
        out.put("pickupLat", d.getPickup().getLatitude());
        out.put("pickupLon", d.getPickup().getLongitude());
        out.put("pickupAddress", d.getPickupAddress());
        out.put("destinationAddress", d.getDestinationAddress());
        out.put("category", d.getRequestedCategory().name());
        if (revealCustomer && d.getCustomer() != null) {
            out.put("customerName", d.getCustomer().getName());
            out.put("customerPhone", d.getCustomer().getPhone().getValue());
        }
        return out;
    }
}
