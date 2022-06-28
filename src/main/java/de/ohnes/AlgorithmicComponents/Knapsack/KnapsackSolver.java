package de.ohnes.AlgorithmicComponents.Knapsack;

import java.util.List;

import de.ohnes.util.Job;

public interface KnapsackSolver {
    public List<Job> solve(List<Job> jobs, int[] wt, int[] val, int n, int W);
}
