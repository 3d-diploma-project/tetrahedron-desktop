package org.cmps.tetrahedron.utils;

/**
 * TODO: add description.
 *
 * @author Mariia Borodin (HappyMary16)
 * @since 1.0
 */
public class StressUtils {

    public static double misesStress(double qx, double qy, double qz, double txy, double tyz, double tzx) {
        return 1 / Math.sqrt(2)
                * Math.sqrt(Math.pow(qx - qy, 2)
                                    + Math.pow(qy - qz, 2)
                                    + Math.pow(qz - qx, 2)
                                    + 6 * (Math.pow(txy, 2) + Math.pow(tyz, 2) + Math.pow(tzx, 2)));
    }
}
