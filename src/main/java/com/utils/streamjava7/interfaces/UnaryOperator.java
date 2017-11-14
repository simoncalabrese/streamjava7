package com.utils.streamjava7.interfaces;

/**
 * Created by simon.calabrese on 13/11/2017.
 */
public interface UnaryOperator<T> extends Function<T,T> {
    T apply(T start);
}
