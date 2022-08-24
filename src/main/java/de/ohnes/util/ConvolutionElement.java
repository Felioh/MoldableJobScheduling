package de.ohnes.util;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * A helper object to represent a single convolution element with its accumulated profit and all elements it consists of.
 */
@Getter
@Setter
public class ConvolutionElement {

    private int profit = 0;
    private List<Job> jobs;

    public ConvolutionElement(int profit, List<Job> jobs) {
        this.profit = profit;
        this.jobs = jobs;
    }
    
}
