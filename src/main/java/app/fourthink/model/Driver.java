package app.fourthink.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "driver")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(name = "license_number", nullable = false, unique = true)
    private String licenseNumber;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "preferred_category", nullable = false)
    @Enumerated(EnumType.STRING)
    private CabCategory preferredCategory;

    public Driver() {
    }

    public Driver(String fullName, String email, Phone phone, String licenseNumber,
                  String passwordHash, CabCategory preferredCategory) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone.getValue();
        this.licenseNumber = licenseNumber;
        this.passwordHash = passwordHash;
        this.preferredCategory = preferredCategory;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public Phone getPhone() {
        return new Phone(phone);
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public boolean hasEmail(String candidate) {
        return candidate != null && email.equalsIgnoreCase(candidate);
    }

    public CabCategory getPreferredCategory() {
        return preferredCategory;
    }
}
