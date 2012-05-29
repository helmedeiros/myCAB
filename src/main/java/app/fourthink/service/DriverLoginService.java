package app.fourthink.service;

import app.fourthink.model.Driver;
import app.fourthink.persistence.DriverRepository;
import app.fourthink.security.PasswordHasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DriverLoginService {

    private DriverRepository drivers;
    private PasswordHasher hasher;

    public DriverLoginService() {
    }

    @Autowired
    public DriverLoginService(DriverRepository drivers, PasswordHasher hasher) {
        this.drivers = drivers;
        this.hasher = hasher;
    }

    public Driver authenticate(String email, String password) {
        if (email == null || password == null) {
            return null;
        }
        Driver driver = drivers.findByEmail(email.toLowerCase());
        if (driver == null) {
            return null;
        }
        if (!hasher.matches(password, driver.getPasswordHash())) {
            return null;
        }
        return driver;
    }
}
