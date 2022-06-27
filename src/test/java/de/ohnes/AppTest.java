package de.ohnes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.ohnes.AlgorithmicComponents.FPTAS.CompressionApproach;
import de.ohnes.AlgorithmicComponents.Shelves.FelixApproach;
import de.ohnes.logger.printSchedule;
import de.ohnes.util.Instance;
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
        this.I.generateRandomInstance(40, 50, 40, 50);
        DualApproximationFramework dualApproxFramework = new DualApproximationFramework(new CompressionApproach(), new FelixApproach());
        this.d = dualApproxFramework.start(I, 0.2);
    }

    /**
     * Tests if the returned Schedule is valid
     */
    @Test
    public void ScheduleIsValid() {
        System.out.println(printSchedule.printThreeShelves(MyMath.findBigJobs(I, d), (int) d));
        assertEquals(d, I.getMakespanBigJobs(d), d * (1/2.0));;
    }
}
