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

    @Column(name = "destination_address")
    private String destinationAddress;

    @Column(name = "customer_initiated", nullable = false)
    private boolean customerInitiated;

    @Column(name = "requested_category", nullable = false)
    @Enumerated(EnumType.STRING)
    private CabCategory requestedCategory;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DispatchStatus status;

    @ManyToOne
    @JoinColumn(name = "assigned_cab_id")
    private Cab assignedCab;

    @Column(name = "proposed_cab_id")
    private Long proposedCabId;

    @Column(name = "declined_cab_ids", length = 500)
    private String declinedCabIds;

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
        this.customerInitiated = false;
    }

    public Dispatch(Customer customer, Location pickup, String pickupAddress,
                     String destinationAddress, CabCategory requestedCategory,
                     boolean customerInitiated) {
        this(customer, pickup, pickupAddress, requestedCategory);
        this.destinationAddress = destinationAddress;
        this.customerInitiated = customerInitiated;
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

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public boolean isCustomerInitiated() {
        return customerInitiated;
    }

    public CabCategory getRequestedCategory() {
        return requestedCategory;
    }

    public DispatchStatus getStatus() {
        return status;
    }

    public boolean isAssigned() {
        return status == DispatchStatus.ASSIGNED;
    }

    public boolean isOpen() {
        return status == DispatchStatus.REQUESTED || status == DispatchStatus.ASSIGNED;
    }

    public Cab getAssignedCab() {
        return assignedCab;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void assignTo(Cab cab) {
        if (status != DispatchStatus.REQUESTED && status != DispatchStatus.PROPOSED) {
            throw new IllegalStateException("cannot assign cab outside REQUESTED/PROPOSED state");
        }
        this.assignedCab = cab;
        this.proposedCabId = null;
        this.status = DispatchStatus.ASSIGNED;
    }

    public void proposeTo(Cab cab) {
        if (status != DispatchStatus.REQUESTED) {
            throw new IllegalStateException("can only propose from REQUESTED state");
        }
        this.proposedCabId = cab.getId();
        this.status = DispatchStatus.PROPOSED;
    }

    public Long getProposedCabId() {
        return proposedCabId;
    }

    public void declineProposed() {
        if (status != DispatchStatus.PROPOSED) {
            throw new IllegalStateException("only PROPOSED dispatches can be declined");
        }
        appendDeclined(proposedCabId);
        this.proposedCabId = null;
        this.status = DispatchStatus.REQUESTED;
    }

    private void appendDeclined(Long cabId) {
        if (cabId == null) return;
        if (declinedCabIds == null || declinedCabIds.isEmpty()) {
            declinedCabIds = cabId.toString();
        } else {
            declinedCabIds = declinedCabIds + "," + cabId;
        }
    }

    public java.util.List<Long> getDeclinedCabIds() {
        java.util.List<Long> out = new java.util.ArrayList<Long>();
        if (declinedCabIds == null || declinedCabIds.isEmpty()) return out;
        for (String part : declinedCabIds.split(",")) {
            out.add(Long.parseLong(part));
        }
        return out;
    }

    public int getDeclineCount() {
        return getDeclinedCabIds().size();
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
