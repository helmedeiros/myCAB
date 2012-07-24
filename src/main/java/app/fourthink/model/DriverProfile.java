package app.fourthink.model;

public final class DriverProfile {

    private final Long id;
    private final String fullName;
    private final String email;
    private final String phone;
    private final CabCategory preferredCategory;

    public DriverProfile(Long id, String fullName, String email,
                          String phone, CabCategory preferredCategory) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.preferredCategory = preferredCategory;
    }

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public CabCategory getPreferredCategory() { return preferredCategory; }

    public static DriverProfile of(Driver driver) {
        return new DriverProfile(driver.getId(), driver.getFullName(), driver.getEmail(),
                driver.getPhone().getValue(), driver.getPreferredCategory());
    }
}
