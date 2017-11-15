import com.utils.streamjava7.classes.Collectors;
import com.utils.streamjava7.classes.Stream;
import com.utils.streamjava7.interfaces.Function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by simon.calabrese on 26/06/2017.
 */
public class TestClass {
    public static void main(String[] rgs) {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 1, 5, 5, 3, 3, 7));
        List<TestPair> ts = new ArrayList<>(Arrays.asList(new TestPair(1), new TestPair(1), new TestPair(5), new TestPair(5), new TestPair(3), new TestPair(3), new TestPair(7)));
        final ArrayList<TestPair> collect = Stream.of(ts).distinct(new Function<TestPair, Integer>() {
            @Override
            public Integer apply(TestPair start) {
                return start.getAge();
            }
        }).collect(Collectors.toList(new ArrayList<TestPair>()));

        System.out.println();
    }
}
