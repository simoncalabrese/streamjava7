import com.utils.streamjava7.classes.Collectors;
import com.utils.streamjava7.interfaces.Consumer;
import com.utils.streamjava7.interfaces.Function;
import com.utils.streamjava7.classes.streamableCollections.classes.StreamableSequence;
import com.utils.streamjava7.classes.streamableCollections.interfaces.StreamableList;
import com.utils.streamjava7.interfaces.Predicate;
import com.utils.streamjava7.interfaces.innerFunction.ToDoubleFunction;

import java.util.Arrays;
import java.util.List;

/**
 * Created by simon.calabrese on 26/06/2017.
 */
public class TestClass {
    public static void main(String[] rgs) {
        StreamableList<Integer> ll = new StreamableSequence<>(Arrays.asList(0,2,5,6,8));
        final Boolean aBoolean = ll.allMatch(new Predicate<Integer>() {
            public Boolean test(Integer object) {
                return object % 2 == 0;
            }
        });

        System.out.println(aBoolean);

    }


}
