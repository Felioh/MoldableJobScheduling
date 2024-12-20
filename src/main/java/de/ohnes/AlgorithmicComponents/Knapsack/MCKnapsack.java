package de.ohnes.AlgorithmicComponents.Knapsack;

import java.util.HashMap;
import java.util.List;

import de.ohnes.util.Job;

/**
 * The MDKnapsack class represents a multi-dimensional knapsack problem solver.
 * It provides a method to solve the problem and allocate jobs to different
 * shelves based on their weights and costs.
 */
public class MCKnapsack {
    /**
     * solves a multiple choice knapsack problem.
     * 
     */
    public void solve(List<Job> items, int capacity,
            List<Job> c1, List<Job> c2, List<Job> c3, double d) {

        int n = items.size();
        // 1st dimension: number of items
        // 2nd dimension: capacity (needs to be scaled by two, to keep integer values)
        Double[][] dp = new Double[n + 1][2 * capacity + 1];

        // initialize the dp array
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= 2 * capacity; j++) {
                dp[i][j] = 0.0;
            }
        }

        // actual knapsack algorithm
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= capacity; j++) {

                Job item = items.get(i - 1);
                int maxWork = item.canonicalNumberMachines(3 * d / 7)
                        * item.getProcessingTime(item.canonicalNumberMachines(3 * d / 7));

                // for each choice of the item
                for (int k = 0; k < 3; k++) {
                    // calculate the cost and weight of the job
                    double profit = 0;
                    int weight = 0;
                    switch (k) {
                        case 0:
                            weight = item.canonicalNumberMachines(d);
                            profit = maxWork - item.getProcessingTime(weight) * weight;
                            weight = 2 * weight;
                            break;
                        case 1:
                            weight = item.canonicalNumberMachines(4 * d / 7);
                            profit = maxWork - item.getProcessingTime(weight) * weight;
                            break;
                        case 2:
                            weight = 0;
                            profit = 0;
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid choice");
                    }

                    // if the weight is less than the capacity
                    if (j >= weight) {
                        // update the dp array
                        dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - weight] + profit);
                    }
                }
            }
        }

        // backtracking to find the selected items
        int j = 2 * capacity;
        for (int i = n; i > 0; i--) {
            Job item = items.get(i - 1);
            int maxWork = item.canonicalNumberMachines(3 * d / 7)
                    * item.getProcessingTime(item.canonicalNumberMachines(3 * d / 7));
            int weight = item.canonicalNumberMachines(d);
            // TODO: check outOfBounds
            if (j - 2 * weight >= 0
                    && dp[i][j] == dp[i - 1][j - 2 * weight] + maxWork - item.getProcessingTime(weight) * weight) {
                item.setAllotedMachines(item.canonicalNumberMachines(d));
                c1.add(item);
                j -= 2 * weight;
            } else if (j - weight >= 0 &&
                    dp[i][j] == dp[i - 1][j - weight] + maxWork - item.getProcessingTime(weight) * weight) {
                item.setAllotedMachines(item.canonicalNumberMachines(4 * d / 7));
                c2.add(item);
                j -= weight;
            } else {
                item.setAllotedMachines(item.canonicalNumberMachines(3 * d / 7));
                c3.add(item);
            }
        }
    }
}
