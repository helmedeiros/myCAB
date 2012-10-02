package app.fourthink.model;

public final class DispatchSummary {

    private final Long id;
    private final String customerName;
    private final String pickupAddress;
    private final CabCategory requestedCategory;
    private final DispatchStatus status;
    private final String assignedPlate;

    public DispatchSummary(Long id, String customerName, String pickupAddress,
                            CabCategory requestedCategory, DispatchStatus status,
                            String assignedPlate) {
        this.id = id;
        this.customerName = customerName;
        this.pickupAddress = pickupAddress;
        this.requestedCategory = requestedCategory;
        this.status = status;
        this.assignedPlate = assignedPlate;
    }

    public Long getId() { return id; }
    public String getCustomerName() { return customerName; }
    public String getPickupAddress() { return pickupAddress; }
    public CabCategory getRequestedCategory() { return requestedCategory; }
    public DispatchStatus getStatus() { return status; }
    public String getAssignedPlate() { return assignedPlate; }

    public static DispatchSummary of(Dispatch dispatch) {
        return new DispatchSummary(
                dispatch.getId(),
                dispatch.getCustomer().getName(),
                dispatch.getPickupAddress(),
                dispatch.getRequestedCategory(),
                dispatch.getStatus(),
                dispatch.getAssignedCab() == null ? null : dispatch.getAssignedCab().getPlate().getValue());
    }
}
