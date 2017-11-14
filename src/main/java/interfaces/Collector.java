package interfaces;

import classes.StreamNew;

/**
 * Created by simon.calabrese on 14/11/2017.
 */
public interface Collector<U, T, M> {
    M collect(final StreamNew<T> stream);
}
