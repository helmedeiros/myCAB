package app.fourthink.model;

public final class Coordinates {

    private final double latitude;
    private final double longitude;

    public Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    public Location toLocation() {
        return new Location(latitude, longitude);
    }

    public static Coordinates of(Location location) {
        return new Coordinates(location.getLatitude(), location.getLongitude());
    }
}
