package de.ohnes.AlgorithmicComponents;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to implement the geometrical rounding used by Felix Land.
 */
public class GeometricalRounding {

    /**
     * A function to geometrically floor a value. (s. p.88 Thesis Felix)
     * @param a the value to round
     * @param lower the lower bound
     * @param upper the upper bound
     * @param x the "base"
     * @return the rounded value
     */
    public static double gFloor(int a, double lower, double upper, double x) {
        int i = 0;
        List<Double> geom = geom(lower, upper, x);
        while(geom.get(i) < a) {
            i++;
        }
        return geom.get(--i);
    }

    /**
     * A function to geometrically ceil a value. (s. p.88 Thesis Felix)
     * @param a the value to round
     * @param lower the lower bound
     * @param upper the upper bound
     * @param x the "base"
     * @return the rounded value
     */
    public static double gCeil(int a, double lower, double upper, double x) {
        int i = 0;
        List<Double> geom = geom(lower, upper, x);
        while(geom.get(i) < a) {
            i++;
        }
        return geom.get(i);
    }


    /**
     * s. Def. 5.12. (Thesis Felix)
     * @param lower
     * @param upper
     * @param x > 1 (normally: 1 < x < 2)
     * @return
     */
    private static List<Double> geom(double lower, double upper, double x) {
        List<Double> res = new ArrayList<>();
        int i = 0;
        while(lower * Math.pow(x, (double) i) < upper) {
            res.add(lower * Math.pow(x, (double) i));
            i++;
        }
        return res;
    }
    
}
