package de.ohnes.AlgorithmicComponents.Sorting;

import java.util.ArrayList;
import java.util.List;

/**
 * Radix sort for positive numbers.
 */
public class RadixSort {

    private final int base;

    public RadixSort(int base) {
        this.base = base;
    }

    private static class Bucket {
    
        private final List<Integer> elements = new ArrayList<>();
    
        private void add(int element) {
            elements.add(element);
        }
    
        private List<Integer> getElements() {
            return elements;
        }
    
    }

    public void sortDynamicList(int[] elements) {
        int max = getMaximum(elements);
        int nbDigits = getNumberOfDigits(max);

        for (int digitIndex = 0; digitIndex < nbDigits; digitIndex++) {
            sortByDigit(elements, digitIndex);
        }

    }

    private void sortByDigit(int[] elements, int digitIndex) {
        Bucket[] buckets = partition(elements, digitIndex);
        collect(buckets, elements);
    }

    private void collect(Bucket[] buckets, int[] elements) {
        int index = 0;
        for (Bucket bucket : buckets) {
            for (int element : bucket.getElements()) {
                elements[index] = element;
                index++;
            }
        }
    }

    private Bucket[] partition(int[] elements, int digitIndex) {
        Bucket[] buckets = createBuckets();
        distributeToBuckets(elements, digitIndex, buckets);
        return buckets;
    }

    private void distributeToBuckets(int[] elements, int digitIndex, Bucket[] buckets) {
        int div = calculateDivisor(digitIndex);

        for (int element : elements) {
            int digit = element / div % this.base;
            buckets[digit].add(element);
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
    
    private int getMaximum(int[] elements) {
        int max = 0;
        for (int element : elements) {
            if (element > max) max = element;
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

