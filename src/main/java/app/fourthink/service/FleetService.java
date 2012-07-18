package app.fourthink.service;

import app.fourthink.model.Cab;
import app.fourthink.model.CabModel;
import app.fourthink.model.CabStatus;
import app.fourthink.model.Location;
import app.fourthink.model.Plate;
import app.fourthink.persistence.CabModelRepository;
import app.fourthink.persistence.CabRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FleetService {

    private CabRepository cabs;
    private CabModelRepository models;

    public FleetService() {
    }

    @Autowired
    public FleetService(CabRepository cabs, CabModelRepository models) {
        this.cabs = cabs;
        this.models = models;
    }

    public Cab register(String plate, Long modelId) {
        CabModel model = models.findById(modelId);
        if (model == null) {
            throw new IllegalArgumentException("unknown cab model: " + modelId);
        }
        if (cabs.findByPlate(new Plate(plate).getValue()) != null) {
            throw new IllegalStateException("plate already registered");
        }
        return cabs.save(new Cab(new Plate(plate), model));
    }

    public Cab get(Long id) {
        Cab cab = cabs.findById(id);
        if (cab == null) {
            throw new IllegalArgumentException("unknown cab: " + id);
        }
        return cab;
    }

    public List<Cab> list() {
        return cabs.findAll();
    }

    public java.util.List<app.fourthink.model.CabSummary> listSummaries() {
        java.util.List<app.fourthink.model.CabSummary> out = new java.util.ArrayList<app.fourthink.model.CabSummary>();
        for (Cab cab : cabs.findAll()) {
            out.add(app.fourthink.model.CabSummary.of(cab));
        }
        return out;
    }

    public List<Cab> listFree() {
        return cabs.findByStatus(CabStatus.FREE);
    }

    public List<Cab> listByStatus(CabStatus status) {
        return cabs.findByStatus(status);
    }

    public Cab updateStatus(Long id, CabStatus status) {
        Cab cab = get(id);
        cab.setStatus(status);
        return cabs.save(cab);
    }

    public Cab updateLocation(Long id, double latitude, double longitude) {
        Cab cab = get(id);
        cab.updateLocation(new Location(latitude, longitude));
        return cabs.save(cab);
    }

    public void deregister(Long id) {
        cabs.delete(get(id));
    }

    public long readyCabCount() {
        return cabs.countFreeWithLocation();
    }

    public FleetStats stats() {
        int free = 0, busy = 0, offline = 0;
        for (Cab cab : cabs.findAll()) {
            switch (cab.getStatus()) {
                case FREE: free++; break;
                case BUSY: busy++; break;
                case OFFLINE: offline++; break;
            }
        }
        return new FleetStats(free + busy + offline, free, busy, offline);
    }
}
