package app.fourthink.service;

import app.fourthink.model.Customer;
import app.fourthink.persistence.CustomerRepository;
import app.fourthink.security.PasswordHasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomerLoginService {

    private CustomerRepository customers;
    private PasswordHasher hasher;

    public CustomerLoginService() {
    }

    @Autowired
    public CustomerLoginService(CustomerRepository customers, PasswordHasher hasher) {
        this.customers = customers;
        this.hasher = hasher;
    }

    public Customer authenticate(String email, String password) {
        if (email == null || password == null) {
            return null;
        }
        Customer customer = customers.findByEmail(email.toLowerCase());
        if (customer == null || !customer.canLogin()) {
            return null;
        }
        if (!hasher.matches(password, customer.getPasswordHash())) {
            return null;
        }
        return customer;
    }
}
