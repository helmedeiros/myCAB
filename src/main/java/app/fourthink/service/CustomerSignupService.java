package app.fourthink.service;

import app.fourthink.model.Customer;
import app.fourthink.model.Phone;
import app.fourthink.persistence.CustomerRepository;
import app.fourthink.security.PasswordHasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
@Transactional
public class CustomerSignupService {

    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private CustomerRepository customers;
    private PasswordHasher hasher;

    public CustomerSignupService() {
    }

    @Autowired
    public CustomerSignupService(CustomerRepository customers, PasswordHasher hasher) {
        this.customers = customers;
        this.hasher = hasher;
    }

    public Customer signup(String name, String email, String phone,
                            String password, String defaultAddress) {
        validate(name, email, phone, password);
        String normalized = email.toLowerCase();
        if (customers.findByEmail(normalized) != null) {
            throw new IllegalStateException("email already registered");
        }
        Phone validatedPhone = new Phone(phone);
        if (customers.findByPhone(validatedPhone.getValue()) != null) {
            throw new IllegalStateException("phone already registered");
        }
        return customers.save(new Customer(name, validatedPhone, defaultAddress,
                normalized, hasher.hash(password)));
    }

    private void validate(String name, String email, String phone, String password) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("name is required");
        }
        if (email == null || !EMAIL.matcher(email).matches()) {
            throw new IllegalArgumentException("invalid email");
        }
        if (phone == null) {
            throw new IllegalArgumentException("phone is required");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("password too short");
        }
    }
}
