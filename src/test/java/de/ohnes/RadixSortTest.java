package de.ohnes;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;


import de.ohnes.AlgorithmicComponents.Sorting.RadixSort;


@RunWith(Parameterized.class)
public class RadixSortTest {

    private int[] list;
    private int[] sorted;
    private int base;

    public RadixSortTest(int[] list, int[] sorted, int base) {
        super();
        this.list = list;
        this.sorted = sorted;
        this.base = base;
    }

    @Parameterized.Parameters
    public static List<Object[]> input() {
        List<Integer> list1 = IntStream.range(1, 11).boxed().collect(Collectors.toList());
        int[] l1S = list1.stream().mapToInt(i -> i).toArray();
        Collections.shuffle(list1);
        int[] l1 = list1.stream().mapToInt(i -> i).toArray();
        int b1 = 10 + (int) Math.random() * 100;

        List<Integer> list2 = IntStream.range(1, 6).boxed().collect(Collectors.toList());
        int[] l2S = list2.stream().mapToInt(i -> i).toArray();
        Collections.shuffle(list2);
        int[] l2 = list2.stream().mapToInt(i -> i).toArray();
        int b2 = 10 + (int) Math.random() * 100;

        List<Integer> list3 = IntStream.range(1, 101).boxed().collect(Collectors.toList());
        int[] l3S = list3.stream().mapToInt(i -> i).toArray();
        Collections.shuffle(list3);
        int[] l3 = list3.stream().mapToInt(i -> i).toArray();
        int b3 = 10 + (int) Math.random() * 100;


        return Arrays.asList(new Object[][] {{l1, l1S, b1}, {l2, l2S, b2}, {l3, l3S, b3}});
    }

    @Test
    public void testRadixSort() {
        RadixSort radixSort = new RadixSort(this.base);
        radixSort.sortDynamicList(this.list);
        for (int i = 0; i < this.list.length; i++) {
            assertEquals(this.list[i], this.sorted[i]);
        }
    }
    
}