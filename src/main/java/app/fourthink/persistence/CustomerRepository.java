package app.fourthink.persistence;

import app.fourthink.model.Customer;

import java.util.List;

public interface CustomerRepository {

    Customer save(Customer customer);

    Customer findById(Long id);

    List<Customer> findAll();

    List<Customer> searchByName(String fragment);

    Customer findByPhone(String phone);

    void delete(Customer customer);

    long count();
}
