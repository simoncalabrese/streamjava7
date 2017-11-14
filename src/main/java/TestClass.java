import classes.CollectorImpl;
import classes.Collectors;
import classes.Stream;
import classes.StreamNew;
import interfaces.*;

import java.util.*;

/**
 * Created by simon.calabrese on 26/06/2017.
 */
public class TestClass {
    public static void main(String[] rgs) {
        /*List<Integer> list = new ArrayList<>(Arrays.asList(1, 1, 5, 5, 3, 3, 7));

        Map<String, Integer> mm = StreamNew.of(list).collect(Collectors.toMap(new Function<Integer, String>() {
            @Override
            public String apply(Integer start) {
                return start.toString();
            }
        }, new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer start) {
                return start;
            }
        }, new BinaryOperator<Integer>() {
            @Override
            public Integer apply(Integer elem1, Integer elem2) {
                return elem1 + elem2;
            }
        }));
       final List<String> collect = StreamNew.of(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6))).collect(Collectors.mapping(new Function<Integer, String>() {
            @Override
            public String apply(Integer start) {
                return start.toString();
            }
        }));*/
        /*final Integer collect1 = StreamNew.of(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6))).collect(Collectors.reducing(0, new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer elem1, Integer elem2) {
                return elem1 + elem2;
            }
        }));*/

        final HashMap<String, Integer> collect = StreamNew.of(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6))).collect(Collectors.groupingBy(new Function<Integer, String>() {
            @Override
            public String apply(Integer start) {
                return start.toString();
            }
        }, Collectors.reducing(0, new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer elem1, Integer elem2) {
                return elem1 + elem2;
            }
        }), new HashMap<String, Integer>()));

        System.out.println();
    }
}
