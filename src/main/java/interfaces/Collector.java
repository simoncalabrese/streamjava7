package interfaces;

import classes.Stream;

/**
 * Created by simon.calabrese on 14/11/2017.
 */
public interface Collector<U, T, M> {
    M collect(final Stream<T> stream);
}
