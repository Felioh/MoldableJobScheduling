package de.ohnes.AlgorithmicComponents.Approximation;

import java.util.List;

/**
 * a class that implements the medianOfMedians Algorithm (https://en.wikipedia.org/wiki/Median_of_medians)
 */
public class MedianOfMedians {
    
    /**
     * find the median of an input list.
     * this finds the upper median if len(list) % 2
     * @param list the input list
     * @return Upper median of the input list.
     */
    public static int findMedian(List<Integer> list) {
        Integer[] Arrlist = list.toArray(Integer[] :: new);
        int index = select(Arrlist, 0, list.size() - 1, list.size() / 2);
        return Arrlist[index];
    }

    private static int select(Integer[] list, int l, int r, int n) {
        while(l != r) {
            int pivot = pivot(list, l, r);
            pivot = partition(list, l, r, pivot, n);
            if(n == pivot) {
                return n;
            }else if(n < pivot) {
                r = pivot - 1;
            }else {
                l = pivot + 1;
            }
        }
        return l;
    }

    private static int pivot(Integer[] list, int l, int r) {
        if(r - l < 5) {
            return partition5(list, l, r);
        }
        for(int i = l; i < r; i = i + 5) {
            int subR = i + 4;
            if(subR > r) {
                subR = r;
            }
            int median5 = partition5(list, l, r);
            int el = list[median5];
            list[median5] = list[l + ((i - l) / 5)];
            list[l + ((i - l) / 5)] = el;
        }
        int mid = (r - l) / 10 + l + 1;
        return select(list, l, l + ((r - l) / 5), mid);
    }


    private static int partition(Integer[] list, int l, int r, int pivot, int n) {
        int pivotValue = list[pivot];
        //swap pivot and end
        list[pivot] = list[r];
        list[r] = pivotValue;

        int currIndex = l;
        for(int i = l; i < r - 1; i++) {
            if(list[i] < pivotValue) {
                int el = list[i];
                list[i] = list[currIndex];
                list[currIndex] = el;
                currIndex++;
            }
        }
        int currIndexEq = currIndex;
        for(int i = currIndex; i < r - 1; i++) {
            if(list[i] == pivotValue) {
                int el = list[i];
                list[i] = list[currIndexEq];
                list[currIndexEq] = el;
                currIndexEq++;
            }
        }
        //move pivot to final place
        list[r] = list[currIndexEq];
        list[currIndexEq] = pivotValue;
        return currIndexEq;
    }

    private static int partition5(Integer[] list, int l, int r) {
        int i = l + 1;
        while(i <= r) {
            int j = i;
            while(j > l && list[j - 1] > list[j]) {
                int el = list[j - 1];
                list[j - 1] = list[j];
                list[j] = el;
                j--;
            }
            i++;
        }
        return (l + r) / 2;
    }
}
