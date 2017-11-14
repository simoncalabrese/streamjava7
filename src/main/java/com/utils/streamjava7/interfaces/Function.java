package com.utils.streamjava7.interfaces;

/**
 * Created by simon.calabrese on 26/06/2017.
 */
public interface Function<A,B> {
    B apply(A start);
}
