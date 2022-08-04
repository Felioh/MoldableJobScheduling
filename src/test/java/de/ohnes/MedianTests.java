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


import de.ohnes.AlgorithmicComponents.Approximation.MedianOfMedians;


@RunWith(Parameterized.class)
public class MedianTests {

    private List<Integer> list;
    private int median;

    public MedianTests(List<Integer> list, int median) {
        super();
        this.list = list;
        this.median = median;
    }

    @Parameterized.Parameters
    public static List<Object[]> input() {
        List<Integer> list1 = IntStream.range(1, 11).boxed().collect(Collectors.toList());
        Collections.shuffle(list1);
        List<Integer> list2 = IntStream.range(1, 6).boxed().collect(Collectors.toList());
        Collections.shuffle(list2);
        list1.addAll(list2);

        List<Integer> list3 = IntStream.range(1, 101).boxed().collect(Collectors.toList());
        Collections.shuffle(list3);

        return Arrays.asList(new Object[][] {{list1, 4}, {list2, 3}, {list3, 51}});
    }

    @Test
    public void testMedian() {
        int median = MedianOfMedians.findMedian(this.list);
        assertEquals(this.median, median);
    }
    
}
