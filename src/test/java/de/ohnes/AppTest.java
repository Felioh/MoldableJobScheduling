package de.ohnes;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.ohnes.AlgorithmicComponents.FPTAS.CompressionApproach;
import de.ohnes.AlgorithmicComponents.Shelves.FelixApproach;
import de.ohnes.util.Instance;
import de.ohnes.util.Job;
import de.ohnes.util.Machine;

/**
 * Unit test for simple App.
 */
public class AppTest {

    private Instance I;
    private double d;

    /**
     * generate a Random Instance an solve it.
     */
    @Before
    public void setup() {
        this.I = new Instance(0, 0, null);
        this.I.generateRandomInstance(10, 20, 3, 4);
        DualApproximationFramework dualApproxFramework = new DualApproximationFramework(new CompressionApproach(), new FelixApproach(), I);
        this.d = dualApproxFramework.start(0.2);
    }

    /**
     * Tests if the returned Schedule is valid
     */
    @Test
    public void scheduleIsValid() {
        for(Job job : I.getJobs()) {
            assertTrue("Every Job should be alloted to at least one Machine", job.getAllotedMachines() > 0);
            int allotedMachines = job.getAllotedMachines();
            for(Machine m : I.getMachines()) {
                if(m.getJobs().contains(job)) {
                    allotedMachines--;
                }
            }
            assertTrue("The number of machines referencing this job differs from the specified amount in the job", allotedMachines == 0);
        }
    }

    /**
     * Test if the Makespan is smaller than the promised one. (3/2 * d) TODO account for epsilon
     */
    @Test
    public void testMakespan() {
        // System.out.println(printSchedule.printThreeShelves(MyMath.findBigJobs(I, d), (int) d));
        assertTrue("The total makespan needs to be smaller than the promised one.", I.getMakespan() <= d * (3/2.0));
    }

    /**
     * there should not be more than m machines used at time 0.
     */
    @Test
    public void machineUsageTime0() {
        int usedMachines = 0;
        for(Job job : I.getJobs()) {
            if(job.getStartingTime() == 0) {
                usedMachines += job.getAllotedMachines();
            }
        }
        assertTrue("there should not be more than m machines used at time 0.", I.getM() >= usedMachines);
    }

    /**
     * there should not be more than m machines used at time d + 1.
     */
    @Test
    public void machineUsageTimed1() {
        int usedMachines = 0;
        for(Job job : I.getJobs()) {
            if(job.getStartingTime() < d + 1 && job.getStartingTime() + job.getProcessingTime(job.getAllotedMachines()) > d + 1) {
                usedMachines += job.getAllotedMachines();
            }
        }
        assertTrue("there should not be more than m machines used at time 0.", I.getM() >= usedMachines);
    }
}
