package app.fourthink.persistence;

import app.fourthink.model.CabCategory;
import app.fourthink.model.Driver;
import app.fourthink.model.DriverStatus;

import java.util.List;

public interface DriverRepository {

    Driver save(Driver driver);

    Driver findById(Long id);

    Driver findByEmail(String email);

    Driver findByLicenseNumber(String licenseNumber);

    List<Driver> findAll();

    long count();

    List<Driver> findByCategory(CabCategory category);

    List<Driver> findByStatus(DriverStatus status);
}
