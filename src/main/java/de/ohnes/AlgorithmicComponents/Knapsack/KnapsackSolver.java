package de.ohnes.AlgorithmicComponents.Knapsack;

import de.ohnes.util.Job;

public interface KnapsackSolver {
    public Job[] solve(Job[] jobs, int[] wt, int[] val, int n, int W);
}
