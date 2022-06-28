package de.ohnes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.ohnes.AlgorithmicComponents.FPTAS.CompressionApproach;
import de.ohnes.AlgorithmicComponents.Shelves.FelixApproach;
import de.ohnes.logger.printSchedule;
import de.ohnes.util.Instance;
import de.ohnes.util.Job;
import de.ohnes.util.MyMath;

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
        DualApproximationFramework dualApproxFramework = new DualApproximationFramework(new CompressionApproach(), new FelixApproach());
        this.d = dualApproxFramework.start(I, 0.2);
    }

    /**
     * Tests if the returned Schedule is valid
     */
    @Test
    public void scheduleIsValid() {
        //TODO
    }

    /**
     * Test if the Makespan is smaller than the promised one. (3/2 * d)
     */
    @Test
    public void testMakespan() {
        System.out.println(printSchedule.printThreeShelves(MyMath.findBigJobs(I, d), (int) d));
        assertTrue("The total makespan needs to be smaller than the promised one.", I.getMakespan() <= d * (3/2.0));
    }

    /**
     * there should not be more than m machines used at time 0.
     */
    @Test
    public void machineUsageTime0() {
        int usedMachines = 0;
        for(Job job : I.getJobs()) {
            if(job.getStartingTime() == 0 && job.getAllotedMachines() != 0) {           //TODO delete allotedMachines != 0
                usedMachines += job.getAllotedMachines();
            }
        }
        assertTrue("there should not be more than m machines used at time 0.", I.getM() >= usedMachines);
    }
}
