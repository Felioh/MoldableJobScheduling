package util;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

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
