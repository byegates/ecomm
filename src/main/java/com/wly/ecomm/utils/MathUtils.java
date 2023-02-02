package com.wly.ecomm.utils;

/**
 * Math related utility static methods
 */

public class MathUtils {
    /**
     * Rounding with scale, we only need 2 so far in this project, but we are allowing -6 to 6 now;
     */
    public static double round(double x, int scale) {
        scale = Math.min(scale, 6);
        scale = Math.max(scale, -6);
        double powerOf10 = Math.pow(10, scale);
        return Math.round(x * powerOf10) / powerOf10;
    }

    public static double getPrice() {
        return round(Math.random() * 1000 + 200, 2);
    }

    public static double getOffPercentage() {
        return round(Math.random()*100, 2);
    }
}
