import com.utils.streamjava7.classes.Collectors;
import com.utils.streamjava7.interfaces.Consumer;
import com.utils.streamjava7.interfaces.Function;
import com.utils.streamjava7.classes.streamableCollections.classes.StreamableSequence;
import com.utils.streamjava7.classes.streamableCollections.interfaces.StreamableList;
import com.utils.streamjava7.interfaces.innerFunction.ToDoubleFunction;

import java.util.Arrays;
import java.util.List;

/**
 * Created by simon.calabrese on 26/06/2017.
 */
public class TestClass {
    public static void main(String[] rgs) {
        StreamableList<Integer> ll = new StreamableSequence<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
        final Double collect = ll.map(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer start) {
                return start;
            }
        }).collect(Collectors.summingDouble(new ToDoubleFunction<Integer>() {
            @Override
            public Double apply(Integer start) {
                System.out.println(start + " + ");
                return new Double(start);
            }
        }));
        System.out.println(collect);

    }


}
