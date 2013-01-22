package app.fourthink.service;

import app.fourthink.model.Cab;
import app.fourthink.model.CabCategory;
import app.fourthink.model.CabModel;
import app.fourthink.model.Driver;
import app.fourthink.model.DriverStatus;
import app.fourthink.model.Phone;
import app.fourthink.model.Plate;
import app.fourthink.persistence.CabModelRepository;
import app.fourthink.persistence.CabRepository;
import app.fourthink.persistence.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DriverReviewService {

    private DriverRepository drivers;
    private CabRepository cabs;
    private CabModelRepository models;
    private FleetIdGenerator fleetIds;

    public DriverReviewService() {
    }

    @Autowired
    public DriverReviewService(DriverRepository drivers, CabRepository cabs,
                                CabModelRepository models, FleetIdGenerator fleetIds) {
        this.drivers = drivers;
        this.cabs = cabs;
        this.models = models;
        this.fleetIds = fleetIds;
    }

    public List<Driver> pending() {
        return drivers.findByStatus(DriverStatus.PENDING);
    }

    public Driver get(Long id) {
        Driver d = drivers.findById(id);
        if (d == null) {
            throw new IllegalArgumentException("unknown driver: " + id);
        }
        return d;
    }

    public Driver editPersonal(Long id, String fullName, String email,
                                String phone, String licenseNumber,
                                CabCategory preferredCategory) {
        Driver d = get(id);
        d.setFullName(fullName);
        d.setEmail(email == null ? d.getEmail() : email.toLowerCase());
        d.setPhone(new Phone(phone));
        d.setLicenseNumber(licenseNumber);
        d.setPreferredCategory(preferredCategory);
        return drivers.save(d);
    }

    public Driver editVehicle(Long id, String plate, Long modelId, String color) {
        Driver d = get(id);
        Cab cab = d.getCab();
        if (cab == null) {
            throw new IllegalStateException("driver has no vehicle");
        }
        Plate validated = new Plate(plate);
        Cab existing = cabs.findByPlate(validated.getValue());
        if (existing != null && !existing.getId().equals(cab.getId())) {
            throw new IllegalStateException("plate already used by another cab");
        }
        cab.rePlate(validated);
        CabModel model = models.findById(modelId);
        if (model == null) {
            throw new IllegalArgumentException("unknown cab model: " + modelId);
        }
        cab.changeModel(model);
        cab.setColor(color);
        cabs.save(cab);
        return d;
    }

    public Driver approve(Long id) {
        Driver d = get(id);
        if (d.getCab() == null) {
            throw new IllegalStateException("driver has no vehicle to approve");
        }
        if (!d.getCab().hasFleetId()) {
            d.getCab().setFleetId(fleetIds.next());
        }
        cabs.save(d.getCab());
        d.approve(d.getCab());
        return drivers.save(d);
    }

    public Driver reject(Long id) {
        Driver d = get(id);
        d.reject();
        return drivers.save(d);
    }
}
