package util;

import java.util.Arrays;
import java.util.stream.Stream;

public class MyMath {

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static double getRandomNumber(double min, double max) {
        return ((Math.random() * (max - min)) + min);
    }

    /**
     * 
     * @param I the instance object including all jobs
     * @param d the deadline
     * @return all Jobs that have a sequential processing time > d/2 
     */
    public static Job[] findBigJobs(Instance I, double d) {
        Stream<Job> jobs = Arrays.stream(I.getJobs());
        return jobs.filter(j -> j.getProcessingTimes()[0] > d / 2).toArray(Job[] :: new);
    }


    /**
     * dynamic programming approach to solve a knapsack problem
     * @param w the item weights
     * @param p the item profits
     * @param n number of items
     * @param C maximal capacity
     * @return an array of all selected jobs.
     */
    public static int[] knapsackSolver(int[] w, int[] p, int n, int C) {
        if (n <= 0 || C <= 0) {
            return null;
        }
    
        int[][] m = new int[C + 1][n + 1];
        for (int j = 0; j <= C; j++) { //profit 0 for all caps with 0 jobs.
            m[j][0] = 0;
        }
    
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= C; j++) {
                if (w[i - 1] > j) { //if weight of job geater than cap
                    m[j][i] = m[j][i - 1]; //keep the profit
                } else {
                    m[j][i] = Math.max(
                      m[j][i - 1], 
                      m[j - w[i - 1]][i - 1] + p[i - 1]);
                }

            }
        }
        return m[C];
    }
    
}
