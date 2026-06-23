package net.henrycmoss.bb.phys;

public class BbMath {

    public static double gaussian(double x, double mean, double standardDev) {
        return (1 / (standardDev * Math.sqrt(2d * Math.PI))) * Math.pow(Math.E,
                -(1d / 2d) * Math.pow(((x - mean) / standardDev), 2));
    }
}
