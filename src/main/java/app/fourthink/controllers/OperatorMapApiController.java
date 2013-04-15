package app.fourthink.controllers;

import app.fourthink.model.Cab;
import app.fourthink.model.Dispatch;
import app.fourthink.model.DispatchStatus;
import app.fourthink.persistence.CabRepository;
import app.fourthink.service.DispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/operator-map")
public class OperatorMapApiController {

    private final CabRepository cabs;
    private final DispatchService dispatches;

    @Autowired
    public OperatorMapApiController(CabRepository cabs, DispatchService dispatches) {
        this.cabs = cabs;
        this.dispatches = dispatches;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> snapshot() {
        Map<String, Object> out = new LinkedHashMap<String, Object>();
        out.put("cabs", cabMarkers());
        out.put("dispatches", dispatchMarkers());
        out.put("timestamp", System.currentTimeMillis());
        return out;
    }

    private List<Map<String, Object>> cabMarkers() {
        List<Map<String, Object>> out = new ArrayList<Map<String, Object>>();
        for (Cab cab : cabs.findAll()) {
            if (!cab.hasLocation()) continue;
            Map<String, Object> entry = new LinkedHashMap<String, Object>();
            entry.put("id", cab.getId());
            entry.put("fleetId", cab.getFleetId());
            entry.put("plate", cab.getPlate().getValue());
            entry.put("status", cab.getStatus().name());
            entry.put("category", cab.getModel().getCategory().name());
            entry.put("lat", cab.getLocation().getLatitude());
            entry.put("lon", cab.getLocation().getLongitude());
            out.add(entry);
        }
        return out;
    }

    private List<Map<String, Object>> dispatchMarkers() {
        List<Map<String, Object>> out = new ArrayList<Map<String, Object>>();
        for (Dispatch d : dispatches.active()) {
            Map<String, Object> entry = new LinkedHashMap<String, Object>();
            entry.put("id", d.getId());
            entry.put("status", d.getStatus().name());
            entry.put("lat", d.getPickup().getLatitude());
            entry.put("lon", d.getPickup().getLongitude());
            entry.put("anonymized", d.isCustomerInitiated()
                    && d.getStatus() == DispatchStatus.REQUESTED);
            if (!Boolean.TRUE.equals(entry.get("anonymized"))) {
                entry.put("customerName", d.getCustomer().getName());
            }
            entry.put("pickupAddress", d.getPickupAddress());
            entry.put("destinationAddress", d.getDestinationAddress());
            entry.put("category", d.getRequestedCategory().name());
            if (d.getAssignedCab() != null) {
                entry.put("assignedFleetId", d.getAssignedCab().getFleetId());
            }
            out.add(entry);
        }
        return out;
    }
}
