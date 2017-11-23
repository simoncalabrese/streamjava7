import com.utils.streamjava7.classes.Collectors;
import com.utils.streamjava7.classes.Stream;
import com.utils.streamjava7.interfaces.*;
import com.utils.streamjava7.interfaces.innerFunction.ToStringFunction;

import java.util.*;

/**
 * Created by simon.calabrese on 26/06/2017.
 */
public class TestClass {
    public static void main(String[] rgs) {
        Collection<TestPair> ts = Stream.of(new ArrayList<>(Arrays.asList(
                new TestPair("b", 1),
                new TestPair("b", 2),
                new TestPair("a", 3),
                new TestPair("a", 4),
                new TestPair("a", 5),
                new TestPair("a", 6),
                new TestPair("a", 7)))).collect(Collectors.toCollection(new ArrayList<TestPair>()));
        final HashMap<String,String> collect = Stream.of(ts).collect(Collectors.groupingBy(new Function<TestPair, String>() {
            @Override
            public String apply(TestPair start) {
                return start.getName();
            }
        }, Collectors.joining("", new ToStringFunction<TestPair>() {
            @Override
            public String apply(TestPair start) {
                return start.getAge().toString();
            }
        }), new HashMap<String, String>()));
        System.out.println();

        String s = null;
        final String collect1 = Stream.of(s).collect(Collectors.<String>joining());
        System.out.println();


        Stream.of(ts).skip(1).forEach(new Consumer<TestPair>() {
            public void consume(TestPair elem) {
                System.out.println(elem.getAge());
            }
        });
    }


}
