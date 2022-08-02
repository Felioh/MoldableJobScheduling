package de.ohnes;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.ohnes.util.Instance;

@RunWith(Parameterized.class)
public class ApproximationTests {

    private Instance I;
    private double opt;

    public ApproximationTests(Instance I, double opt) {
        super();
        this.I = I;
        this.opt = opt;
    }

    @Parameterized.Parameters
    public static List<Object[]> input() {
        Instance I = null;
        try {
            I = new ObjectMapper().readValue(Paths.get("TestInstance copy 3.json").toFile(), Instance.class);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return Arrays.asList(new Object[][] {{I, 80.0}});
    }

    @Test
    public void testApproximation() {
        TwoApproximation approx = new TwoApproximation();
        double res = approx.approximate(I);
        assertTrue("The approximation should be at most 2opt", this.opt * 2 >= res);
    }
    
}
