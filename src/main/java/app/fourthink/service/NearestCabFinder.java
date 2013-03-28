package app.fourthink.service;

import app.fourthink.model.Cab;
import app.fourthink.model.CabCategory;
import app.fourthink.model.CabStatus;
import app.fourthink.model.Distance;
import app.fourthink.model.Location;
import app.fourthink.persistence.CabRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class NearestCabFinder {

    private final CabRepository cabs;

    @Autowired
    public NearestCabFinder(CabRepository cabs) {
        this.cabs = cabs;
    }

    public Cab findClosest(Location pickup, CabCategory category) {
        return findClosestExcluding(pickup, category, Collections.<Long>emptySet());
    }

    public Cab findClosestExcluding(Location pickup, CabCategory category,
                                     java.util.Collection<Long> excludedCabIds) {
        Set<Long> exclusions = new HashSet<Long>(excludedCabIds);
        List<Cab> free = cabs.findByStatus(CabStatus.FREE);
        Cab best = null;
        double bestKm = Double.MAX_VALUE;
        for (Cab cab : free) {
            if (cab.getModel().getCategory() != category) continue;
            if (!cab.hasLocation()) continue;
            if (exclusions.contains(cab.getId())) continue;
            double km = Distance.kilometers(pickup, cab.getLocation());
            if (km < bestKm) {
                bestKm = km;
                best = cab;
            }
        }
        return best;
    }
}
