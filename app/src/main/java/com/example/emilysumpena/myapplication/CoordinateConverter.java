package com.example.emilysumpena.myapplication;
public class CoordinateConverter {

    public static final double EQUATORIAL_RADIUS_METER = 63781370.0;
    public static final double DEGREE_TO_RADIAN = Math.PI / 180.0;
    public static final double RADIAN_TO_DEGREE = 180.0 / Math.PI;

    public static double calcLatChanges(double dist) {
        return (dist / EQUATORIAL_RADIUS_METER) * RADIAN_TO_DEGREE;
    }

    public static double calcLongChanges(double latitude, double dist) {
        double r = EQUATORIAL_RADIUS_METER * Math.cos(latitude * DEGREE_TO_RADIAN);
        return (dist / r) * RADIAN_TO_DEGREE;
    }

    /**
     *
     * @param geoOrigin The coordinate of the origin of the map (or floor plan) under the geographic coordinate system (longitude and latitude)
     * @param alpha The rotation of the map (or floor plan) relative to the east (in degrees)
     * @param localCoord The targeting coordinate under the local coordinate system (x and y, in meters)
     * @return The new coordinate under the geographic coordinate system
     */
    public static double[] localCoord2GeoCoord(double[] geoOrigin, double alpha, double[] localCoord) {
        double d = Math.sqrt(localCoord[0] * localCoord[0] + localCoord[1] * localCoord[1]);
        double beta = Math.atan(localCoord[1] / localCoord[0]);
        double gamma = alpha * DEGREE_TO_RADIAN + beta;
        double distSouth = d * Math.sin(gamma);
        double distEast = d * Math.cos(gamma);
        double newLong = geoOrigin[0] + calcLongChanges(geoOrigin[1], distEast);
        double newLat = geoOrigin[1] - calcLatChanges(distSouth);
        return new double[] {newLong, newLat};
    }


}

