package app.fourthink.model;

public final class Distance {

    private static final double EARTH_RADIUS_KM = 6371.0088;

    private Distance() {}

    public static double kilometers(Location from, Location to) {
        double lat1 = Math.toRadians(from.getLatitude());
        double lat2 = Math.toRadians(to.getLatitude());
        double dLat = Math.toRadians(to.getLatitude() - from.getLatitude());
        double dLon = Math.toRadians(to.getLongitude() - from.getLongitude());

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                 + Math.cos(lat1) * Math.cos(lat2)
                 * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }
}
