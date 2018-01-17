import com.utils.streamjava7.classes.Collectors;
import com.utils.streamjava7.classes.Stream;
import com.utils.streamjava7.interfaces.Consumer;
import com.utils.streamjava7.interfaces.Predicate;
import com.utils.streamjava7.interfaces.innerFunction.ToPairFunction;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by simon.calabrese on 26/06/2017.
 */
public class TestClass {
    public static void main(String[] rgs) {
        List<Integer> li = Arrays.asList(1, 2,2,3,3,3,1,1);
        final Pair<List<Integer>, List<Integer>> partition = Stream.of(li).partition(new Predicate<Integer>() {
            @Override
            public Boolean test(Integer object) {
                return object % 2 == 0;
            }
        });
        final ArrayList<Integer> collect = Stream.of(li).dropWhile(new Predicate<Integer>() {
            @Override
            public Boolean test(Integer object) {
                return object % 2 == 1;
            }
        }).collect(Collectors.toList(new ArrayList<Integer>()));

        final Pair<List<Integer>, List<Integer>> span = Stream.of(li).span(new Predicate<Integer>() {
            @Override
            public Boolean test(Integer object) {
                return object % 2 == 0;
            }
        });
        Stream.of(li).pack().forEach(new Consumer<Stream<Integer>>() {
            @Override
            public void consume(Stream<Integer> elem) {
                System.out.println("\n\n");
                elem.forEach(new Consumer<Integer>() {
                    @Override
                    public void consume(Integer elem) {
                        System.out.println(elem);
                    }
                });

            }
        });
        System.out.println(partition);

        Stream.of(li).map(new ToPairFunction<Integer,Integer,Integer>() {
            @Override
            public Pair<Integer, Integer> apply(Integer start) {
                return Pair.of(start,start*2);
            }
        }).collect(Collectors.toList(new ArrayList<Pair<Integer,Integer>>()));
    }


}
