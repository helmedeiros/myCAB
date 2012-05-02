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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "dispatch")
public class Dispatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "pickup_lat", nullable = false)
    private double pickupLatitude;

    @Column(name = "pickup_lon", nullable = false)
    private double pickupLongitude;

    @Column(name = "pickup_address")
    private String pickupAddress;

    @Column(name = "requested_category", nullable = false)
    @Enumerated(EnumType.STRING)
    private CabCategory requestedCategory;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DispatchStatus status;

    @ManyToOne
    @JoinColumn(name = "assigned_cab_id")
    private Cab assignedCab;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    public Dispatch() {
    }

    public Dispatch(Customer customer, Location pickup, String pickupAddress,
                     CabCategory requestedCategory) {
        this.customer = customer;
        this.pickupLatitude = pickup.getLatitude();
        this.pickupLongitude = pickup.getLongitude();
        this.pickupAddress = pickupAddress;
        this.requestedCategory = requestedCategory;
        this.status = DispatchStatus.REQUESTED;
        this.createdAt = new Date();
    }

    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Location getPickup() {
        return new Location(pickupLatitude, pickupLongitude);
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public CabCategory getRequestedCategory() {
        return requestedCategory;
    }

    public DispatchStatus getStatus() {
        return status;
    }

    public Cab getAssignedCab() {
        return assignedCab;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void assignTo(Cab cab) {
        if (status != DispatchStatus.REQUESTED) {
            throw new IllegalStateException("cannot assign cab outside REQUESTED state");
        }
        this.assignedCab = cab;
        this.status = DispatchStatus.ASSIGNED;
    }

    public void complete() {
        if (status != DispatchStatus.ASSIGNED) {
            throw new IllegalStateException("only assigned dispatches can be completed");
        }
        this.status = DispatchStatus.COMPLETED;
    }

    public void cancel() {
        if (status == DispatchStatus.COMPLETED) {
            throw new IllegalStateException("completed dispatches cannot be cancelled");
        }
        this.status = DispatchStatus.CANCELLED;
    }
}
