package de.ohnes.AlgorithmicComponents.Knapsack;

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
    public boolean solve(List<Job> items, int capacity,
            List<Job> c1, List<Job> c2, List<Job> c3, double d) {

        int n = items.size();
        // 1st dimension: number of items
        // 2nd dimension: capacity (needs to be scaled by two, to keep integer values)
        Integer[][][] dp = new Integer[n + 1][2 * capacity + 1][3];

        // initialize the dp array
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= 2 * capacity; j++) {
                for (int k = 0; k < 3; k++) {
                    if (i == 0) {
                        dp[i][j][k] = 0; // base case
                    } else {
                        dp[i][j][k] = Integer.MAX_VALUE;
                    }
                }
            }
        }

        double d3div7 = 3 * d / 7;
        double d4div7 = 4 * d / 7;

        Integer minCost = Integer.MAX_VALUE - 1;
        int maxJ = 0;
        int maxK = 0;

        int[][] precomputedWeights = new int[n][3];
        int[][] precomputedCosts = new int[n][3];

        for (int i = 0; i < n; i++) {
            Job item = items.get(i);
            precomputedWeights[i][0] = item.canonicalNumberMachines(d) * 2;
            precomputedWeights[i][1] = item.canonicalNumberMachines(d4div7);
            precomputedWeights[i][2] = 0;

            precomputedCosts[i][0] = item.getProcessingTime(item.canonicalNumberMachines(d))
                    * item.canonicalNumberMachines(d);
            precomputedCosts[i][1] = item.canonicalNumberMachines(d4div7) == -1 ? 0
                    : item.getProcessingTime(precomputedWeights[i][1]) * precomputedWeights[i][1]; // 0 will not be
                                                                                                   // used.
            precomputedCosts[i][2] = item.canonicalNumberMachines(d3div7) == -1 ? 0
                    : item.getProcessingTime(item.canonicalNumberMachines(d3div7))
                            * item.canonicalNumberMachines(d3div7); // 0 will not be used.
        }

        // actual knapsack algorithm
        for (int i = 1; i <= n; i++) {
            if (minCost == Integer.MAX_VALUE) {
                // the previous job was not placeable
                return false; // The intsance is not solvable.
            }
            minCost = Integer.MAX_VALUE;
            for (int j = 0; j <= 2 * capacity; j++) {

                Job item = items.get(i - 1);

                // for each choice of the item
                for (int k = 0; k < 3; k++) {
                    // calculate the cost and weight of the job
                    int cost = precomputedCosts[i - 1][k];
                    int weight = precomputedWeights[i - 1][k];
                    switch (k) {
                        case 0:
                            if (item.canonicalNumberMachines(d) == -1) {
                                return false; // The intsance is not solvable.
                            }
                            break;
                        case 1:
                            if (item.canonicalNumberMachines(d4div7) == -1) {
                                continue; // this choice is not vaild.
                            }
                            break;
                        case 2:
                            if (item.canonicalNumberMachines(d3div7) == -1) {
                                continue; // this choice is not vaild.
                            }
                            break;
                    }

                    // if the weight is less than the capacity
                    if (j >= weight) {
                        int bestPrevious = Math.min(
                                dp[i - 1][j - weight][0],
                                Math.min(dp[i - 1][j - weight][1],
                                        dp[i - 1][j - weight][2]));
                        // update the dp array
                        if (bestPrevious != Integer.MAX_VALUE) {
                            dp[i][j][k] = bestPrevious + cost;
                            if (dp[i][j][k] < minCost) {
                                minCost = dp[i][j][k];
                                maxJ = j;
                                maxK = k;
                            }
                        }
                    }
                }
            }
        }

        // backtracking to find the selected items
        for (int i = n; i > 0; i--) {
            Job item = items.get(i - 1);
            int cost = precomputedCosts[i - 1][maxK];
            int weight = precomputedWeights[i - 1][maxK];
            switch (maxK) {
                case 0:
                    item.setAllotedMachines(item.canonicalNumberMachines(d));
                    c1.add(item);
                    break;
                case 1:
                    item.setAllotedMachines(item.canonicalNumberMachines(d4div7));
                    c2.add(item);
                    break;
                case 2:
                    item.setAllotedMachines(item.canonicalNumberMachines(d3div7));
                    c3.add(item);
                    break;
            }

            if (i == 1) {
                break; // no more items to process
            }
            // find the best previous choice
            if (minCost - cost == dp[i - 1][maxJ - weight][0]) {
                maxK = 0;
            } else if (minCost - cost == dp[i - 1][maxJ - weight][1]) {
                maxK = 1;
            } else if (minCost - cost == dp[i - 1][maxJ - weight][2]) {
                maxK = 2;
            } else {
                return false; // this should not happen.
                // throw new IllegalArgumentException("Invalid choice in Backtracking");
            }
            // update the maxProfit
            maxJ = maxJ - weight;
            minCost = dp[i - 1][maxJ][maxK];

        }
        return true;
    }
}
