package de.ohnes;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import de.ohnes.util.Job;

@RunWith(Parameterized.class)
public class InstanceAndJobTests {

    private Job job;
    private double h;
    private double canonicalNumberMachines;
    // private MaxConvolution maxConvolution;

    public InstanceAndJobTests(Job job, double h, double canonicalNumberMachines) {
        super();
        this.job = job;
        this.h = h;
        this.canonicalNumberMachines = canonicalNumberMachines;
    }

    @Parameterized.Parameters
    public static List<Object[]> input() {
        int[] pTimes = {100, 80, 50, 40 ,30};
        Job job = new Job(0, pTimes);

        return Arrays.asList(new Object[][] {{job, 60, 3}, {job, 50, 3}, {job, 20, -1}, {job, 100, 1}, {job, 110, 1}});
    }

    @Test
    public void testCannonicalNubmer() {
        assertEquals(this.canonicalNumberMachines, job.canonicalNumberMachines(h), 0.0);
    }

    
}
