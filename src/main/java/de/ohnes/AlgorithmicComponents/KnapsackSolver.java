package de.ohnes.AlgorithmicComponents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.ohnes.util.*;

public class KnapsackSolver {

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
    public static Job[] knapsackConvolution(Job[] jobs, int[] wt, int[] val, int n, int W) {

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

    
    /**
     * dynamic programming approach to solve a knapsack problem.
     * @implNote this function already assigns the selected job to respect the d threshold.
     * @param wt the item weights
     * @param val the item profits
     * @param n number of items
     * @param W maximal capacity
     * @return an array of all selected jobs.
     */
    public static Job[] dynamicKnapsack(Job[] jobs, int[] wt, int[] val, int n, int W, Instance I, double d) {
        int i, w;
        int K[][] = new int[n + 1][W + 1];
 
        // Build table K[][] in bottom up manner
        for (i = 0; i <= n; i++) {
            for (w = 0; w <= W; w++) {
                if (i == 0 || w == 0)
                    K[i][w] = 0;
                else if (wt[i - 1] <= w)
                    K[i][w] = Math.max(val[i - 1] +
                              K[i - 1][w - wt[i - 1]], K[i - 1][w]);
                else
                    K[i][w] = K[i - 1][w];
            }
        }
 
        // stores the result of Knapsack
        int res = K[n][W];
 
        w = W;
        for (i = n; i > 0 && res > 0; i--) {
 
            // either the result comes from the top
            // (K[i-1][w]) or from (val[i-1] + K[i-1]
            // [w-wt[i-1]]) as in Knapsack table. If
            // it comes from the latter one/ it means
            // the item is included.
            if (res == K[i - 1][w])
                continue;
            else {
 
                // This item is included.
                jobs[i - 1].setAllotedMachines(I.canonicalNumberMachines(jobs[i - 1].getId(), d));    //allot job to machines respecting d as a threshold
 
                // Since this weight is included its
                // value is deducted
                res = res - val[i - 1];
                w = w - wt[i - 1];
            }
        }
        return jobs;
    }


}
