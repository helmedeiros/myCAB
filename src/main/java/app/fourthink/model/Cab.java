package app.fourthink.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cab")
public class Cab {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plate", nullable = false, unique = true)
    private String plate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "model_id")
    private CabModel model;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private CabStatus status;

    @Column(name = "lat")
    private Double latitude;

    @Column(name = "lon")
    private Double longitude;

    @Column(name = "fleet_id", unique = true)
    private String fleetId;

    public Cab() {
    }

    public Cab(Plate plate, CabModel model) {
        this.plate = plate.getValue();
        this.model = model;
        this.status = CabStatus.OFFLINE;
    }

    public Long getId() {
        return id;
    }

    public Plate getPlate() {
        return new Plate(plate);
    }

    public CabModel getModel() {
        return model;
    }

    public CabStatus getStatus() {
        return status;
    }

    public void setStatus(CabStatus status) {
        this.status = status;
    }

    public Location getLocation() {
        if (latitude == null || longitude == null) {
            return null;
        }
        return new Location(latitude, longitude);
    }

    public boolean hasLocation() {
        return latitude != null && longitude != null;
    }

    public void updateLocation(Location location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }

    public String getFleetId() {
        return fleetId;
    }

    public void setFleetId(String fleetId) {
        this.fleetId = fleetId;
    }

    public boolean hasFleetId() {
        return fleetId != null && !fleetId.trim().isEmpty();
    }

    public void rePlate(Plate plate) {
        this.plate = plate.getValue();
    }

    public void changeModel(CabModel model) {
        this.model = model;
    }
}
