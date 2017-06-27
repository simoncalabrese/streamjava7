import classes.Stream;
import interfaces.BiFunction;
import interfaces.Function;
import interfaces.Predicate;

import java.util.*;

/**
 * Created by simon.calabrese on 26/06/2017.
 */
public class TestClass {
    public static void main(String[] rgs) {
        List<Integer> l = new ArrayList<>(Arrays.asList(1,2));
        List<Integer> objects = Stream.of(l).filter(new Predicate<Integer>() {
            @Override
            public Boolean test(Integer object) {
                return object % 2 == 0;
            }
        }).toList();
        Set<Integer> strings = Stream.of(l).map(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer start) {
                return 4;
            }
        }).toSet();

        Integer reduce = Stream.of(l).reduce(new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer elem1, Integer elem2) {
                return elem1 + elem2;
            }
        });
        System.out.println();
    }
}
