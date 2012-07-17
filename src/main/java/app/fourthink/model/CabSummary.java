package app.fourthink.model;

public final class CabSummary {

    private final Long id;
    private final String plate;
    private final String modelDisplay;
    private final CabCategory category;
    private final CabStatus status;

    public CabSummary(Long id, String plate, String modelDisplay,
                       CabCategory category, CabStatus status) {
        this.id = id;
        this.plate = plate;
        this.modelDisplay = modelDisplay;
        this.category = category;
        this.status = status;
    }

    public Long getId() { return id; }
    public String getPlate() { return plate; }
    public String getModelDisplay() { return modelDisplay; }
    public CabCategory getCategory() { return category; }
    public CabStatus getStatus() { return status; }

    public static CabSummary of(Cab cab) {
        return new CabSummary(cab.getId(), cab.getPlate().getValue(),
                cab.getModel().displayName(), cab.getModel().getCategory(),
                cab.getStatus());
    }
}
