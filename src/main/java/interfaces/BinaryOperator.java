package interfaces;

/**
 * Created by simon.calabrese on 13/11/2017.
 */
public interface BinaryOperator<T> extends BiFunction<T,T,T> {
    T apply(T elem1, T elem2);
}
