package de.ohnes;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import de.ohnes.AlgorithmicComponents.Knapsack.DynamicKnapsack;
import de.ohnes.AlgorithmicComponents.Knapsack.KnapsackSolver;
import de.ohnes.util.Job;

@RunWith(Parameterized.class)
public class KnapsackTests {

    private Job[] allJobs;
    private Job[] solution;
    private int[] wt;
    private int[] val;
    private int n;
    private int W;

    public KnapsackTests(Job[] allJobs, Job[] solution, int[] wt, int[] val, int n, int W) {
        super();
        this.allJobs = allJobs;
        this.solution = solution;
        this.wt = wt;
        this.val = val;
        this.n = n;
        this.W = W;
    }

    // @Before
    // public void setUp() {
    //     Job[] jobs = new Job[5];
    //     int[] pTimes = {99, 40};
    //     jobs[0] = new Job(0, pTimes);
    //     jobs[1] = new Job(1, pTimes);
    //     jobs[2] = new Job(2, pTimes);
    //     jobs[3] = new Job(3, pTimes);
    //     jobs[4] = new Job(4, pTimes);
    //     this.solution = new Job[2];
    //     this.solution[0] = jobs[0];
    //     this.solution[1] = jobs[4];
    //     this.instance = new Instance(5, 2, jobs);
    // }

    @Parameterized.Parameters
    public static List<Object[]> input() {

        //1st test env
        Job[] allJobs1 = new Job[8];
        int[] wt1 = {1, 1, 1, 1, 2, 2, 2, 2};
        int[] val1 = {2, 4, 6, 8, 3, 6, 9, 12};
        int n1 = 8;
        int W1 = 3;

        return Arrays.asList(new Object[][] {{allJobs1, null, wt1, val1, n1, W1}});
    }

    // private Job generateJob(int id, int wt, int val, int m) {
    //     int[] pTimes = new int[m];
    //     int pTime = 100;
    //     for(int i = wt; i >= 0; i--) {
    //         pTimes[i] = pTime;
            
    //     }
    //     Job job = new Job(id, pTimes);
    // }

    // @Test
    // public void KnapsackTestConvolutionOutputSize() {
    //     KnapsackSolver kS = new ConvolutionKnapsack();
    //     Job[] selectedJobs = kS.solve(this.instance.getJobs(), wt, val, n, W);
    //     assertTrue("The number of selected Jobs should be <= to the capacity", selectedJobs.length <= W); //length should be leq than capacity
    //     // assertThat(selectedJobs, );
    // }

    @Test
    public void KnapsackTestDynamicOutputSize() {
        KnapsackSolver kS = new DynamicKnapsack();
        Job[] selectedJobs = kS.solve(allJobs, wt, val, n, W);
        assertTrue("The number of selected Jobs should be <= to the capacity", selectedJobs.length <= W); //length should be leq than capacity
        // assertThat(selectedJobs, );
    }
}
