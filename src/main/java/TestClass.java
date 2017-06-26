import classes.Stream;
import interfaces.Function;
import interfaces.Predicate;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by simon.calabrese on 26/06/2017.
 */
public class TestClass {
    public static void main(String[] rgs) {
        List<Integer> l = Arrays.asList(1,2,3,4,5,6,7,8,9);
        Collection<Integer> objects = Stream.of(l).filter(new Predicate<Integer>() {
            @Override
            public Boolean test(Integer object) {
                return object % 2 == 0;
            }
        }).toList();
        Collection<String> strings = Stream.of(l).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer start) {
                return start.toString();
            }
        }).toList();
        System.out.println();
    }
}
