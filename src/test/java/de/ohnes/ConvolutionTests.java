package de.ohnes;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import de.ohnes.AlgorithmicComponents.MaxConvolution;
import de.ohnes.util.ConvolutionElement;

@RunWith(Parameterized.class)
public class ConvolutionTests {

    private ConvolutionElement[] seqA;
    private ConvolutionElement[] seqB;
    private ConvolutionElement[] seqC;
    // private MaxConvolution maxConvolution;

    public ConvolutionTests(ConvolutionElement[] seqA, ConvolutionElement[] seqB, ConvolutionElement[] seqC) {
        super();
        this.seqA = seqA;
        this.seqB = seqB;
        this.seqC = seqC;
    }

    // @Before
    // public void initialize() {
    //     maxConvolution = new MaxConvolution();
    // }

    @Parameterized.Parameters
    public static Collection input() {
        ConvolutionElement[] seqA1 = new ConvolutionElement[] {new ConvolutionElement(1, new ArrayList<>()), 
                                                               new ConvolutionElement(2, new ArrayList<>())};
        ConvolutionElement[] seqB1 = new ConvolutionElement[] {new ConvolutionElement(3, new ArrayList<>()), 
                                                               new ConvolutionElement(0, new ArrayList<>())};
        ConvolutionElement[] seqC1 = new ConvolutionElement[] {new ConvolutionElement(3, new ArrayList<>()), 
                                                               new ConvolutionElement(4, new ArrayList<>())};


        ConvolutionElement[] seqA2 = new ConvolutionElement[] {new ConvolutionElement(1, new ArrayList<>()), 
                                                               new ConvolutionElement(2, new ArrayList<>()),
                                                               new ConvolutionElement(3, new ArrayList<>()),
                                                               new ConvolutionElement(4, new ArrayList<>())};
        ConvolutionElement[] seqB2 = new ConvolutionElement[] {new ConvolutionElement(1, new ArrayList<>()), 
                                                               new ConvolutionElement(2, new ArrayList<>()),
                                                               new ConvolutionElement(3, new ArrayList<>()),
                                                               new ConvolutionElement(4, new ArrayList<>())};
        ConvolutionElement[] seqC2 = new ConvolutionElement[] {new ConvolutionElement(1, new ArrayList<>()), 
                                                               new ConvolutionElement(2, new ArrayList<>()),
                                                               new ConvolutionElement(3, new ArrayList<>()),
                                                               new ConvolutionElement(4, new ArrayList<>())};

        return Arrays.asList(new Object[][] {{seqA2, seqB2, seqC2}, {seqA1, seqB1, seqC1}});
    }

    @Test
    public void testConvolutions() {
        ConvolutionElement[] linearResult = MaxConvolution.linearApproach(seqA, seqB);
        // ConvolutionElement[] linearResult = MaxConvolution.nativeApproach(seqA, seqB, seqA.length);
        for(int i = 0; i < linearResult.length; i++) {
            assertEquals(linearResult[i].getProfit(), seqC[i].getProfit());
        }
        // assertArrayEquals(MaxConvolution.linearApproach(seqA, seqB), MaxConvolution.linearApproach(seqA, seqB));
    }
    
}
