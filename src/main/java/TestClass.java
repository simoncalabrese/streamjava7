import com.utils.streamjava7.classes.Collectors;
import com.utils.streamjava7.classes.ParallelStream;
import com.utils.streamjava7.classes.Stream;
import com.utils.streamjava7.interfaces.*;
import com.utils.streamjava7.interfaces.innerFunction.ToDoubleFunction;
import com.utils.streamjava7.interfaces.innerFunction.ToStringFunction;

import java.util.*;

/**
 * Created by simon.calabrese on 26/06/2017.
 */
public class TestClass {
    public static void main(String[] rgs) {
        List<TestPair> ts = Stream.of(new ArrayList<>(Arrays.asList(
                new TestPair("b", 1),
                new TestPair("b", 2),
                new TestPair("a", 3),
                new TestPair("a", 4),
                new TestPair("c", 5),
                new TestPair("c", 6),
                new TestPair("c", 7)))).collect(Collectors.toCollection(new ArrayList<TestPair>()));

        final Double sum = Stream.of(ts).mapToDouble(new ToDoubleFunction<TestPair>() {
            @Override
            public Double apply(TestPair start) {
                return new Double(start.getAge());
            }
        }).sum();

        System.out.println(sum);
        /*Stream.of(ts).forEachOrdered(new Consumer<TestPair>() {
            @Override
            public void consume(TestPair elem) {
                System.out.println(elem.getName());
            }
        }, new Comparator<TestPair>() {
            @Override
            public int compare(TestPair o1, TestPair o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });*/
    }


}
