package de.ohnes.AlgorithmicComponents.Sorting;

import java.util.ArrayList;
import java.util.List;

import de.ohnes.util.Job;
import lombok.Getter;

/**
 * Radix sort for positive numbers.
 * 
 * This class is implemented highly specific to the context it is used in.
 */
public class RadixSort {

    private final int base;

    public RadixSort(int base) {
        this.base = base;
    }

    @Getter
    private static class Bucket {
    
        private final List<Integer> profits = new ArrayList<>();
        private final List<Job> jobs = new ArrayList<>();
        private final List<Integer> weights = new ArrayList<>();
    
        private void add(int profit, Job job, int weight) {
            profits.add(profit);
            jobs.add(job);
            weights.add(weight);
        }
    
    }

    /**
     * This method gets three lists and sorts them in a way that the 1st list will be sorted.
     * The other two lists will be sorted in the same way
     * 
     * for example:
     * [3, 2, 1], [a, b, c] [c, b, a] -> [1, 2, 3], [c, b, a], [a, b, c]
     */
    public void sortDynamicList(int[] profits, Job[] jobs, int[] weights) {
        int max = getMaximum(profits);
        int nbDigits = getNumberOfDigits(max);

        for (int digitIndex = 0; digitIndex < nbDigits; digitIndex++) {
            sortByDigit(profits, digitIndex, jobs, weights);
        }

    }

    private void sortByDigit(int[] profits, int digitIndex, Job[] jobs, int[] weights) {
        Bucket[] buckets = partition(profits, digitIndex, jobs, weights);
        collect(buckets, profits, jobs, weights);
    }

    private void collect(Bucket[] buckets, int[] profits, Job[] jobs, int[] weights) {
        int globIndex = 0;
        int tmpIndex = 0;
        for (Bucket bucket : buckets) {
            tmpIndex = globIndex;
            for (int profit : bucket.getProfits()) {
                profits[tmpIndex] = profit;
                tmpIndex++;
            }
            tmpIndex = globIndex;
            for (int weight : bucket.getWeights()) {
                weights[tmpIndex] = weight;
                tmpIndex++;
            }
            tmpIndex = globIndex;
            for (Job job : bucket.getJobs()) {
                jobs[tmpIndex] = job;
                tmpIndex++;
            }
            globIndex = tmpIndex;
        }
    }

    private Bucket[] partition(int[] profits, int digitIndex, Job[] jobs, int[] weights) {
        Bucket[] buckets = createBuckets();
        distributeToBuckets(profits, digitIndex, buckets, jobs, weights);
        return buckets;
    }

    private void distributeToBuckets(int[] profits, int digitIndex, Bucket[] buckets, Job[] jobs, int[] weights) {
        int div = calculateDivisor(digitIndex);

        for (int i = 0; i < profits.length; i++) {
            int digit = profits[i] / div % this.base;
            buckets[digit].add(profits[i], jobs[i], weights[i]);
        }
    }

    private int calculateDivisor(int digitIndex) {
        int div = 1;
        for (int i = 0; i < digitIndex; i++) {
            div *= this.base;
        }
        return div;
    }

    private Bucket[] createBuckets() {
        Bucket[] buckets = new Bucket[this.base];
        for (int i = 0; i < this.base; i++) {
            buckets[i] = new Bucket();
        }
        return buckets;
    }
    
    private int getMaximum(int[] profits) {
        int max = 0;
        for (int profit : profits) {
            if (profit > max) max = profit;
        }
        return max;
    }

    private int getNumberOfDigits(int number) {
        int nbDigits = 1;
        while (number >= this.base) {
            number /= this.base;
            nbDigits++;
        }
        return nbDigits;
    }

}

