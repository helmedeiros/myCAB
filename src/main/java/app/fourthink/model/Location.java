package app.fourthink.model;

public final class Location {

    private final double latitude;
    private final double longitude;

    public Location(double latitude, double longitude) {
        if (latitude < -90.0 || latitude > 90.0) {
            throw new IllegalArgumentException("latitude must be between -90 and 90");
        }
        if (longitude < -180.0 || longitude > 180.0) {
            throw new IllegalArgumentException("longitude must be between -180 and 180");
        }
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Location)) return false;
        Location o = (Location) other;
        return Double.compare(latitude, o.latitude) == 0
                && Double.compare(longitude, o.longitude) == 0;
    }

    @Override
    public int hashCode() {
        long la = Double.doubleToLongBits(latitude);
        long lo = Double.doubleToLongBits(longitude);
        int result = (int) (la ^ (la >>> 32));
        result = 31 * result + (int) (lo ^ (lo >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Location[" + latitude + "," + longitude + "]";
    }
}
