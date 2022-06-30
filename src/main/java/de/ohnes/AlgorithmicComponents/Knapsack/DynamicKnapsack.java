package de.ohnes.AlgorithmicComponents.Knapsack;

import java.util.ArrayList;
import java.util.List;

import de.ohnes.util.Job;

public class DynamicKnapsack implements KnapsackSolver {

    /**
     * dynamic programming approach to solve a knapsack problem.
     * @implNote this function already assigns the selected job to respect the d threshold.
     * @param wt the item weights
     * @param val the item profits
     * @param n number of items
     * @param W maximal capacity
     * @return an array of all selected jobs.
     */
    @Override
    public List<Job> solve(List<Job> jobs, int[] wt, int[] val, int n, int W) {
        List<Job> selectedJobs = new ArrayList<>();
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
                selectedJobs.add(jobs.get(i - 1));
                // jobs[i - 1].setAllotedMachines(I.canonicalNumberMachines(jobs[i - 1].getId(), d));    //allot job to machines respecting d as a threshold
 
                // Since this weight is included its
                // value is deducted
                res = res - val[i - 1];
                w = w - wt[i - 1];
            }
        }
        return selectedJobs;
    }
    
}
