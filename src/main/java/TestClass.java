import classes.Collectors;
import classes.Stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by simon.calabrese on 26/06/2017.
 */
public class TestClass {
    public static void main(String[] rgs) {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 1, 5, 5, 3, 3, 7));
        final Stream<Integer> sorted = Stream.of(list).sorted(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        });
        sorted.collect(Collectors.<Integer>joining(","));

        System.out.println();
    }
}
