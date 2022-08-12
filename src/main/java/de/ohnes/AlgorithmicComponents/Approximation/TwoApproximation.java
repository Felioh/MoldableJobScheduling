package de.ohnes.AlgorithmicComponents.Approximation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.ohnes.util.Instance;
import de.ohnes.util.Job;

/**
 * implementation of the Algorithm from ludwig Tiwari
 */
public class TwoApproximation implements Approximation {
    
    public double approximate(Instance I) {

        //1
        double low = Double.MIN_VALUE;
        for(Job job : I.getJobs()) {
            if(low < job.getProcessingTime(I.getM())) {
                low = job.getProcessingTime(I.getM());
            }
        }
        //2
        int[] lower = new int[I.getN()];
        int[] upper = new int[I.getN()];
        for(int i = 0; i < I.getN(); i++) {
            lower[i] = 1;
            if(I.getJob(i).getProcessingTime(1) < low) {
                upper[i] = 0;
            } else {
                upper[i] = I.getJob(i).canonicalNumberMachines(low) - 1; //TODO check!!!
            }
        }
        //3
        double successful = Double.NEGATIVE_INFINITY;
        double unsuccessful =  Double.NEGATIVE_INFINITY;
        //4
        int[] mid = new int[I.getN()];
        int[] tallot = new int[I.getN()];
        int[] sallot = new int[I.getN()];
        int[] uallot = new int[I.getN()];
        //TODO track a list for all i's that have lower <= upper
        while(checkAllUppperGreaterThanLower(lower, upper)) {
            List<Integer> pTimes = new ArrayList<>();
            for(int i = 0; i < I.getN(); i++) {
                if(lower[i] <= upper[i]) {
                    mid[i] = (lower[i] + upper[i]) / 2; //step 4(a) integer division. So floored.
                    pTimes.add(I.getJob(i).getProcessingTime(mid[i])); //step 4(b)
                }
            }
            int target = MedianOfMedians.findMedian(pTimes);
            // double target = findTarget(pTimes.toArray(Integer[] :: new)); //step 4(b)
            for(int i = 0; i < I.getN(); i++) {
                tallot[i] = I.getJob(i).canonicalNumberMachines(target); //step 4(c)
            }
            //step 4(d)
            int work = 0;
            for(int i = 0; i < I.getN(); i++) {
                work += tallot[i] * I.getJob(i).getProcessingTime(tallot[i]);
            }
            //step 4(e)
            double achievedBound = Math.max(work / (double) I.getM(), target);
            //step 4(f)
            if(work / (double) I.getM() <= target) {
                successful = achievedBound;
                sallot = tallot.clone();
                for(int i = 0; i < I.getN(); i++) {
                    if(lower[i] <= upper[i]) {
                        lower[i] = I.getJob(i).canonicalNumberMachines(target);
                        if(I.getJob(i).getProcessingTime(lower[i]) == target) {
                            if(lower[i] == 1) {
                                upper[i] = 0;
                            } else {
                                lower[i] = lower[i] + 1;
                            }
                        }
                    }
                }
            } else {    //step 4(g)
                unsuccessful = achievedBound;
                uallot = tallot.clone();
                for(int i = 0; i < I.getN(); i++) {
                    if(lower[i] <= upper[i]) {
                        upper[i] = I.getJob(i).canonicalNumberMachines(target) - 1; //TODO check
                    }
                }
            }
        }
        //step 6
        return Math.max(unsuccessful, successful); //TODO check
    }

    /**
     * checks if lower[i] < upper[i] for all i. 
     * lengths must be equal
     * @param lower
     * @param upper
     * @return
     */
    private boolean checkAllUppperGreaterThanLower(int[] lower, int[] upper) {
        for(int i = 0; i < lower.length; i++) {
            if(lower[i] < upper[i]) {
                return true;
            }
        }
        return false;
    }

    private double findTarget(Integer[] pTimes) {
        Arrays.sort(pTimes);
        if(pTimes.length % 2 == 0) {
            return ((double)pTimes[pTimes.length/2] + (double)pTimes[pTimes.length/2 - 1])/2;
        } else {
            return (double) pTimes[pTimes.length/2];
        }
    }


    
}
