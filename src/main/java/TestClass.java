import com.utils.streamjava7.classes.*;
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

        Integer b = Optional.of(ts.get(0)).mapByPattarn(new Predicate<TestPair>() {
            @Override
            public Boolean test(TestPair object) {
                return object.getName().equals("b") && object.getAge() % 2 == 0;
            }
        },new Function<TestPair, Integer>() {
            @Override
            public Integer apply(TestPair start) {
                return start.getAge() * 2;
            }
        },new Function<TestPair, Integer>() {
            @Override
            public Integer apply(TestPair start) {
                return start.getAge() * 3;
            }
        }).orElse(null);
        System.out.println(b);


        Integer a = PatternMatcherImp.getMatcher(ts.get(0), new Function<TestPair, Integer>() {
            @Override
            public Integer apply(TestPair start) {
                return start.getAge();
            }
        }).addMatcher(new Predicate<TestPair>() {
            @Override
            public Boolean test(TestPair object) {
                return object.getName().equals("a") && object.getAge() % 2 == 0;
            }
        }, new Function<TestPair, Integer>() {
            @Override
            public Integer apply(TestPair start) {
                return start.getAge() * 2;
            }
        }).addMatcher(new Predicate<TestPair>() {
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
