package de.ohnes;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import de.ohnes.util.Instance;
import de.ohnes.util.Job;

@RunWith(Parameterized.class)
public class InstanceAndJobTests {

    private Instance I;
    private double h;
    private int m;
    // private MaxConvolution maxConvolution;

    public InstanceAndJobTests(Instance I, double h, int m) {
        super();
        this.I = I;
        this.h = h;
        this.m = m;
    }

    // @Before
    // public void initialize() {
    //     maxConvolution = new MaxConvolution();
    // }

    @Parameterized.Parameters
    public static List<Object[]> input() {
        int[] pTimes = {100, 80, 50, 40 ,30};
        Job[] jobs = {new Job(0, pTimes)};
        Instance I1 = new Instance(0, 5, jobs);

        return Arrays.asList(new Object[][] {{I1, 60, 3}, {I1, 50, 3}, {I1, 20, -1}});
    }

    @Test
    public void testCannonicalNubmer() {
        assertEquals(m, I.canonicalNumberMachines(0, h));
    }

    
}
