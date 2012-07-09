package app.fourthink.service;

import app.fourthink.model.Customer;
import app.fourthink.model.Phone;
import app.fourthink.persistence.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomerService {

    private CustomerRepository customers;

    public CustomerService() {
    }

    @Autowired
    public CustomerService(CustomerRepository customers) {
        this.customers = customers;
    }

    public Customer register(String name, String phone, String defaultAddress) {
        Phone p = new Phone(phone);
        Customer existing = customers.findByPhone(p.getValue());
        if (existing != null) {
            throw new IllegalStateException("phone already belongs to a customer");
        }
        return customers.save(new Customer(name, p, defaultAddress));
    }

    public Customer get(Long id) {
        Customer c = customers.findById(id);
        if (c == null) {
            throw new IllegalArgumentException("unknown customer: " + id);
        }
        return c;
    }

    public List<Customer> list() {
        return customers.findAll();
    }

    public List<Customer> recent(int limit) {
        return customers.findRecent(limit);
    }

    public List<Customer> search(String fragment) {
        if (fragment == null || fragment.trim().isEmpty()) {
            return customers.findAll();
        }
        return customers.searchByName(fragment.trim());
    }

    public Customer update(Long id, String name, String phone, String defaultAddress) {
        Customer c = get(id);
        c.setName(name);
        c.setPhone(new Phone(phone));
        c.setDefaultAddress(defaultAddress);
        return customers.save(c);
    }

    public void deregister(Long id) {
        customers.delete(get(id));
    }
}
