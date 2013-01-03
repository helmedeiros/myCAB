package app.fourthink.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DriverStatus status;

    @OneToOne(optional = true)
    @JoinColumn(name = "cab_id")
    private Cab cab;

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
        this.status = DriverStatus.PENDING;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Phone getPhone() {
        return new Phone(phone);
    }

    public void setPhone(Phone phone) {
        this.phone = phone.getValue();
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public CabCategory getPreferredCategory() {
        return preferredCategory;
    }

    public void setPreferredCategory(CabCategory preferredCategory) {
        this.preferredCategory = preferredCategory;
    }

    public DriverStatus getStatus() {
        return status;
    }

    public Cab getCab() {
        return cab;
    }

    public void setCab(Cab cab) {
        this.cab = cab;
    }

    public boolean hasEmail(String candidate) {
        return candidate != null && email.equalsIgnoreCase(candidate);
    }

    public void approve(Cab fleetCab) {
        if (status == DriverStatus.REJECTED) {
            throw new IllegalStateException("rejected driver cannot be approved");
        }
        this.cab = fleetCab;
        this.status = DriverStatus.ACTIVE;
    }

    public void reject() {
        this.status = DriverStatus.REJECTED;
        this.cab = null;
    }
}
