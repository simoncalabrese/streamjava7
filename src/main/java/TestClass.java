import com.utils.streamjava7.classes.Collectors;
import com.utils.streamjava7.classes.ParallelStream;
import com.utils.streamjava7.classes.Stream;
import com.utils.streamjava7.interfaces.Consumer;
import com.utils.streamjava7.interfaces.Function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by simon.calabrese on 26/06/2017.
 */
public class TestClass {
    public static void main(String[] rgs) {
        List<TestPair> ts = new ArrayList<>(Arrays.asList(
                new TestPair(1),
                new TestPair(2),
                new TestPair(3),
                new TestPair(4),
                new TestPair(5),
                new TestPair(6),
                new TestPair(7)));
        long start = System.currentTimeMillis();
        ParallelStream.of(ts).map(new Function<TestPair, Integer>() {
            @Override
            public Integer apply(TestPair start) {
                return start.getAge();
            }
        }).collect(Collectors.toList(new ArrayList<Integer>()));
        System.out.println(System.currentTimeMillis() - start);
        start = System.currentTimeMillis();
        Stream.of(ts).map(new Function<TestPair, Integer>() {
            @Override
            public Integer apply(TestPair start) {
                return start.getAge();
            }
        }).collect(Collectors.toList(new ArrayList<Integer>()));
        System.out.println(System.currentTimeMillis() - start);
    }


}
