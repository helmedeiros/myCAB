package app.fourthink.service;

import app.fourthink.model.Cab;
import app.fourthink.model.CabCategory;
import app.fourthink.model.CabModel;
import app.fourthink.model.Driver;
import app.fourthink.model.Phone;
import app.fourthink.model.Plate;
import app.fourthink.persistence.CabModelRepository;
import app.fourthink.persistence.CabRepository;
import app.fourthink.persistence.DriverRepository;
import app.fourthink.security.PasswordHasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
@Transactional
public class DriverSignupService {

    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private DriverRepository drivers;
    private CabRepository cabs;
    private CabModelRepository models;
    private PasswordHasher hasher;

    public DriverSignupService() {
    }

    @Autowired
    public DriverSignupService(DriverRepository drivers, CabRepository cabs,
                                CabModelRepository models, PasswordHasher hasher) {
        this.drivers = drivers;
        this.cabs = cabs;
        this.models = models;
        this.hasher = hasher;
    }

    public Driver signup(String fullName, String email, String phone,
                          String licenseNumber, String password,
                          CabCategory preferredCategory,
                          String plate, Long modelId) {
        validate(fullName, email, phone, licenseNumber, password, preferredCategory, plate, modelId);
        String normalized = email.toLowerCase();
        if (drivers.findByEmail(normalized) != null) {
            throw new IllegalStateException("email already registered");
        }
        if (drivers.findByLicenseNumber(licenseNumber) != null) {
            throw new IllegalStateException("license already registered");
        }
        Plate validatedPlate = new Plate(plate);
        if (cabs.findByPlate(validatedPlate.getValue()) != null) {
            throw new IllegalStateException("plate already registered");
        }
        CabModel model = models.findById(modelId);
        if (model == null) {
            throw new IllegalArgumentException("unknown cab model: " + modelId);
        }
        Cab cab = cabs.save(new Cab(validatedPlate, model));
        Driver driver = new Driver(fullName, normalized, new Phone(phone),
                licenseNumber, hasher.hash(password), preferredCategory);
        driver.setCab(cab);
        return drivers.save(driver);
    }

    private void validate(String fullName, String email, String phone,
                           String licenseNumber, String password,
                           CabCategory preferredCategory,
                           String plate, Long modelId) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("name is required");
        }
        if (email == null || !EMAIL.matcher(email).matches()) {
            throw new IllegalArgumentException("invalid email");
        }
        if (phone == null) {
            throw new IllegalArgumentException("phone is required");
        }
        if (licenseNumber == null || licenseNumber.trim().length() < 6) {
            throw new IllegalArgumentException("license number too short");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("password too short");
        }
        if (preferredCategory == null) {
            throw new IllegalArgumentException("category is required");
        }
        if (plate == null || plate.trim().isEmpty()) {
            throw new IllegalArgumentException("plate is required");
        }
        if (modelId == null) {
            throw new IllegalArgumentException("vehicle model is required");
        }
    }
}
