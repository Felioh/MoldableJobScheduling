package de.ohnes;

import de.ohnes.AlgorithmicComponents.Algorithm;
import de.ohnes.util.Instance;
import de.ohnes.util.Job;

public class DualApproximationFramework {
    
    //an fptas that is to be used for a large number of machines (>= 8*(n/epsilon))
    private Algorithm fptas;
    private Algorithm knapsack;
    private Instance I;

    public DualApproximationFramework(Algorithm fptas, Algorithm knapsack, Instance I) {
        this.fptas = fptas;
        this.knapsack = knapsack;
        this.I = I;
    }
    

    public double start(double epsilon) {
        
        Algorithm usedAlgo;
        if(I.getM() >= 8 * (I.getN() / epsilon)) {
            usedAlgo = this.fptas;
            usedAlgo.setInstance(I);
        } else {
            usedAlgo = this.knapsack;
            usedAlgo.setInstance(I);
        }
        //use the FPTAS
        double lowerBound = 0; //TODO either like that or 2 approx. * (1/2) 
        double upperBound = 0;
        for(Job job : I.getJobs()) {
            lowerBound += job.getProcessingTime(1);
            upperBound += (job.getProcessingTime(I.getM()) * I.getM()); //TODO Factor M can be ignored because it is divided by immediatly after.
        }
        lowerBound = lowerBound / I.getM();
        upperBound = upperBound / I.getM(); //TODO check if that is correct.

        return binarySearch(usedAlgo, epsilon, lowerBound, upperBound);
    }

    private double binarySearch(Algorithm algo, double epsilon, double l, double r) {

        double mid = l + (r - l) / 2;
        I.resetInstance(); //reset the instance because it was altered in previous attempt.
        if(algo.solve(mid, epsilon)) { //a schedule of length "mid" exists

            if(r - mid < epsilon) {     //TODO think about minimal steplength
                return mid;
            }

            return binarySearch(algo, epsilon, l, mid); //try to find a better schedule
        } else {    //no schedule for length "mid" exists
            return binarySearch(algo, epsilon, mid, r); //find a schedule for worse makespan
        }
    }
    
}
