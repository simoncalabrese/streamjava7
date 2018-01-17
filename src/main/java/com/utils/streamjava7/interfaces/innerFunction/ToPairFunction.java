package com.utils.streamjava7.interfaces.innerFunction;

import com.utils.streamjava7.interfaces.Function;
import org.apache.commons.lang3.tuple.Pair;

public interface ToPairFunction<T,K,V> extends Function<T, Pair<K, V>> {
}
