package org.cmps.tetrahedron.utils;

public class StressUtils {

    public static float misesStress(double qx, double txy, double tzx, double qy, double tyz, double qz) {
        return (float) (1 / Math.sqrt(2)
                * Math.sqrt(Math.pow(qx - qy, 2)
                                    + Math.pow(qy - qz, 2)
                                    + Math.pow(qz - qx, 2)
                                    + 6 * (Math.pow(txy, 2) + Math.pow(tyz, 2) + Math.pow(tzx, 2))));
    }
}
