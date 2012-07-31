package app.fourthink.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "default_address")
    private String defaultAddress;

    public Customer() {
    }

    public Customer(String name, Phone phone, String defaultAddress) {
        this.name = name;
        this.phone = phone.getValue();
        this.defaultAddress = defaultAddress;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Phone getPhone() {
        return new Phone(phone);
    }

    public void setPhone(Phone phone) {
        this.phone = phone.getValue();
    }

    public boolean hasDefaultAddress() {
        return defaultAddress != null && !defaultAddress.trim().isEmpty();
    }

    public String getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(String defaultAddress) {
        this.defaultAddress = defaultAddress;
    }
}
