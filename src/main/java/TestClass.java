import com.utils.streamjava7.classes.Collectors;
import com.utils.streamjava7.classes.Stream;
import com.utils.streamjava7.interfaces.Function;
import com.utils.streamjava7.interfaces.innerFunction.ToStringFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by simon.calabrese on 26/06/2017.
 */
public class TestClass {
    public static void main(String[] rgs) {
        List<TestPair> ts = new ArrayList<>(Arrays.asList(
                new TestPair("b", 1),
                new TestPair("b", 2),
                new TestPair("a", 3),
                new TestPair("a", 4),
                new TestPair("a", 5),
                new TestPair("a", 6),
                new TestPair("a", 7)));
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
    }


}
