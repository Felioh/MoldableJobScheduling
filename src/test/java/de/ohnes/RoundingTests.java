package de.ohnes;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import de.ohnes.AlgorithmicComponents.GeometricalRounding;

@RunWith(Parameterized.class)
public class RoundingTests {

    private int a;
    private int lower;
    private int upper;
    private double x;
    private double solL;
    private double solU;
    // private MaxConvolution maxConvolution;

    public RoundingTests(int a, int lower, int upper, double x, double solU, double solL) {
        super();
        this.a = a;
        this.lower = lower;
        this.upper = upper;
        this.x = x;
        this.solL = solL;
        this.solU = solU;
    }

    @Parameterized.Parameters
    public static List<Object[]> input() {
        
        int a1 = 50;
        int lower1 = 20;
        int upper1 = 100;
        double x1 = 1.5;
        double solL1 = 45;
        double solU1 = 67.5;

        int a2 = 70;
        int lower2 = 20;
        int upper2 = 102;
        double x2 = 1.5;
        double solL2 = 67.5;
        double solU2 = 101.25;

        // int lower2 = random(1, 100);
        // int upper2 = random(lower2, 200);
        // int a2 = random(lower2, upper2);
        // double x2 = Math.random() + 1;
        // double solL2 = 0.0;
        // double solU2 = 0.0;

        return Arrays.asList(new Object[][] {{a1, lower1, upper1, x1, solU1, solL1}, {a2, lower2, upper2, x2, solU2, solL2}});
    }

    private static int random(int min, int max) {
        return (int) Math.round(min + Math.random() * (max - min));
    }

    @Test
    public void testGeometricFloor() {
        double sol = GeometricalRounding.gFloor(a, lower, upper, x);
        assertEquals(solL, sol, 0.0);
    }

    @Test
    public void testGeometricCeil() {
        double sol = GeometricalRounding.gCeil(a, lower, upper, x);
        assertEquals(solU, sol, 0.0);
    }
    
}
