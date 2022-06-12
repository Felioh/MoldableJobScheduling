package de.ohnes.AlgorithmicComponents.Knapsack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.ohnes.AlgorithmicComponents.MaxConvolution;
import de.ohnes.util.ConvolutionElement;
import de.ohnes.util.Job;

public class ConvolutionKnapsack implements KnapsackSolver {

    /**
     *  Laufzeit: O(DT), T = Capacity, D = Distinct weights (<=M)
     * 
     * @param jobs Array of jobs
     * @param wt   Weight of jobs
     * @param val  Profit of Jobs
     * @param n    number of Jobs
     * @param W    maximum Capacity
     * @param I    The full Instance
     * @param d    the deadline
     * @return     A Subset of the Jobs to be selected
     */
    @Override
    public Job[] solve(Job[] jobs, int[] wt, int[] val, int n, int W) {
        ConvolutionElement[] sol = new ConvolutionElement[W];
        for(int j = 0; j < W; j++) {        //to avoid NullPointer
            sol[j] = new ConvolutionElement(0, new ArrayList<>());
        }

        for(int i = 1; i <= W; i++) {        //for every possible value of weights (D)
            List<Job> currJobs = new ArrayList<>();
            List<Integer> profit = new ArrayList<>();
            for(int j = 0; j < jobs.length; j++) {
                if(wt[j] == i) {
                    profit.add(val[j]);     //TODO: Dont use ArrayLists!!
                    currJobs.add(jobs[j]);
                }
            }

            ConvolutionElement[] currSol = new ConvolutionElement[W];
            for(int j = 1; j <= W; j++) {       //increasing capacity
                List<Job> selected_Jobs = new ArrayList<>();
                List<Integer> selected_Profit = new ArrayList<>();
                while(selected_Jobs.size() * i <= (W - i) && !profit.isEmpty()) {        // <= W - i because there needs to be space for at least one item of weight i
                    int maxProfit = Collections.max(profit);
                    selected_Profit.add(maxProfit);
                    selected_Jobs.add(currJobs.get(profit.indexOf(maxProfit)));
                    //delete
                    currJobs.remove(currJobs.get(profit.indexOf(maxProfit)));
                    profit.remove((Integer) maxProfit);
                }
                currSol[j - 1] = new ConvolutionElement(selected_Profit.stream().reduce(0, Integer::sum), selected_Jobs);
                currJobs.addAll(selected_Jobs);
                profit.addAll(selected_Profit);

            }

            // sol = MaxConvolution.nativeApproach(currSol, sol, W);
            sol = MaxConvolution.linearApproach(currSol, sol);
        }
        // sol[W - 1].getJobs().forEach(j -> j.setAllotedMachines(I.canonicalNumberMachines(j.getId(), d)));
        return sol[W - 1].getJobs().toArray(Job[] :: new);
    }
    
}
