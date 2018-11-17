package com.example.emilysumpena.myapplication.trilateration.src.main.java;
public class CoordinateConverter {

    public static final double EQUATORIAL_RADIUS_METE = 6378137.0;
    public static final double DEGREE_TO_RADIAN = Math.PI / 180.0;
    public static final double RADIAN_TO_DEGREE = 180.0 / Math.PI;

    /**
     * Convert the coordinate in map to the geographical coordinate
     * @param geoOrigin The coordinate of the origin of the map (or floor plan) under the geographic coordinate system (longitude and latitude)
     * @param alpha The rotation of the map (or floor plan) relative to the east (in degrees)
     * @param localCoord The targeting coordinate under the local coordinate system (x and y, in meters)
     * @return The new coordinate under the geographic coordinate system
     */
    public static double[] mapCoord2GeoCoord(double[] geoOrigin, double alpha, double[] localCoord) {
        double d = Math.sqrt(localCoord[0] * localCoord[0] + localCoord[1] * localCoord[1]);
        double beta = Math.atan(localCoord[1] / localCoord[0]);
        double gamma = alpha * DEGREE_TO_RADIAN + beta;
        double distSouth = d * Math.sin(gamma);
        double distEast = d * Math.cos(gamma);
        double newLong = geoOrigin[0] + radian2Dist(distEast, EQUATORIAL_RADIUS_METE * Math.cos(geoOrigin[1]));
        double newLat = geoOrigin[1] - radian2Dist(distSouth, EQUATORIAL_RADIUS_METE);
        return new double[] {newLong, newLat};
    }

    /**
     * Convert the geographical coordinate (in radius) to the local coordinate (in meter)
     * @param geoCoord
     * @param baseGeoCoord
     * @return
     */
    public static double[] geoCoord2LocalCoord(double[] geoCoord, double[] baseGeoCoord) {
        return new double[] {
                radian2Dist(geoCoord[0] - baseGeoCoord[0], EQUATORIAL_RADIUS_METE * Math.cos(baseGeoCoord[1])),
                radian2Dist(geoCoord[1] - baseGeoCoord[1], EQUATORIAL_RADIUS_METE)
        };
    }

    /**
     * Convert the local coordinate (in meter) to the geographical coordinate (in radius)
     * @param localCoord
     * @param baseGeoCoord
     * @return
     */
    public static double[] localCoord2GeoCoord(double[] localCoord, double[] baseGeoCoord) {
        return new double[] {
            baseGeoCoord[0] + dist2Radian(localCoord[0], EQUATORIAL_RADIUS_METE * Math.cos(baseGeoCoord[1])),
            baseGeoCoord[1] + dist2Radian(localCoord[1], EQUATORIAL_RADIUS_METE)
        };

    }

    public static double degree2Dist(double degree, double radius) {
        return degree * DEGREE_TO_RADIAN * radius;
    }

    public static double radian2Dist(double radian, double radius) {
        return radian * radius;
    }

    public static double dist2Degree(double dist, double radius) {
        return dist / radius * RADIAN_TO_DEGREE;
    }

    public static double dist2Radian(double dist, double radius) {
        return dist / radius;
    }

}
