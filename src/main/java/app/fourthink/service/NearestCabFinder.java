package app.fourthink.service;

import app.fourthink.model.Cab;
import app.fourthink.model.CabCategory;
import app.fourthink.model.CabStatus;
import app.fourthink.model.Distance;
import app.fourthink.model.Location;
import app.fourthink.persistence.CabRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NearestCabFinder {

    private final CabRepository cabs;

    @Autowired
    public NearestCabFinder(CabRepository cabs) {
        this.cabs = cabs;
    }

    public Cab findClosest(Location pickup, CabCategory category) {
        List<Cab> free = cabs.findByStatus(CabStatus.FREE);
        Cab best = null;
        double bestKm = Double.MAX_VALUE;
        for (Cab cab : free) {
            if (cab.getModel().getCategory() != category) continue;
            if (!cab.hasLocation()) continue;
            double km = Distance.kilometers(pickup, cab.getLocation());
            if (km < bestKm) {
                bestKm = km;
                best = cab;
            }
        }
        return best;
    }
}
