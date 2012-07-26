package app.fourthink.service;

import app.fourthink.model.CabCategory;
import app.fourthink.model.Driver;
import app.fourthink.model.DriverProfile;
import app.fourthink.persistence.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DriverDirectory {

    private DriverRepository drivers;

    public DriverDirectory() {
    }

    @Autowired
    public DriverDirectory(DriverRepository drivers) {
        this.drivers = drivers;
    }

    public List<DriverProfile> all() {
        return project(drivers.findAll());
    }

    public List<DriverProfile> byCategory(CabCategory category) {
        return project(drivers.findByCategory(category));
    }

    public long count() {
        return drivers.count();
    }

    private List<DriverProfile> project(List<Driver> input) {
        List<DriverProfile> out = new ArrayList<DriverProfile>();
        for (Driver d : input) {
            out.add(DriverProfile.of(d));
        }
        return out;
    }
}
