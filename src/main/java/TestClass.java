import com.utils.streamjava7.classes.Collectors;
import com.utils.streamjava7.classes.ParallelStream;
import com.utils.streamjava7.classes.PatternMatcherImp;
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

        Integer a = PatternMatcherImp.getMatcher(ts.get(0), new Function<TestPair, Integer>() {
            @Override
            public Integer apply(TestPair start) {
                return start.getAge();
            }
        }).addMather(new Predicate<TestPair>() {
            @Override
            public Boolean test(TestPair object) {
                return object.getName().equals("a") && object.getAge() % 2 == 0;
            }
        }, new Function<TestPair, Integer>() {
            @Override
            public Integer apply(TestPair start) {
                return start.getAge() * 2;
            }
        }).addMather(new Predicate<TestPair>() {
            @Override
            public Boolean test(TestPair object) {
                return object.getName().equals("a") && object.getAge() % 2 == 1;
            }
        }, new Function<TestPair, Integer>() {
            @Override
            public Integer apply(TestPair start) {
                return start.getAge() * 3;
            }
        }).match();
        System.out.println(a);
    }


}
