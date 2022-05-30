package de.ohnes;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import javax.xml.crypto.dsig.spec.HMACParameterSpec;

import org.junit.Before;
import org.junit.Test;

import de.ohnes.AlgorithmicComponents.KnapsackSolver;
import de.ohnes.util.*;


public class KnapsackTests {

    private Instance instance;
    private Job[] solution;
    private int[] wt = {1, 1, 1, 1, 1};
    private int[] val = {10, 1, 1, 5, 9};
    private int n = 5;
    private int W = 2;

    @Before
    public void setUp() {
        Job[] jobs = new Job[5];
        int[] pTimes = {99, 40};
        jobs[0] = new Job(0, pTimes);
        jobs[1] = new Job(1, pTimes);
        jobs[2] = new Job(2, pTimes);
        jobs[3] = new Job(3, pTimes);
        jobs[4] = new Job(4, pTimes);
        this.solution = new Job[2];
        this.solution[0] = jobs[0];
        this.solution[1] = jobs[4];
        this.instance = new Instance(5, 2, jobs);
    }

    @Test
    public void KnapsackTest1() {
        Job[] selectedJobs = KnapsackSolver.knapsackConvolution(this.instance.getJobs(), wt, val, n, W);
        assertTrue("The number of selected Jobs should be <= to the capacity", selectedJobs.length <= W); //length should be leq than capacity
        // assertThat(selectedJobs, );
    }
}
