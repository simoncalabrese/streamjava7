package com.utils.streamjava7.classes;

import com.utils.streamjava7.interfaces.BiFunction;
import com.utils.streamjava7.interfaces.Collector;
import com.utils.streamjava7.interfaces.Function;
import com.utils.streamjava7.interfaces.Supplier;

/**
 * Created by simon.calabrese on 14/11/2017.
 */
public class CollectorImpl<U, T, M> implements Collector<U ,T, M> {

    private Supplier<U> supplier;
    private BiFunction<U, T, U> aggregator;
    private Function<U, M> afterAll;

    public CollectorImpl(final Supplier<U> supplier, final BiFunction<U, T, U> aggregator, final Function<U, M> afterAll) {
        this.supplier = supplier;
        this.aggregator = aggregator;
        this.afterAll = afterAll;
    }



    @Override
    public M collect(final Stream<T> stream) {
        return afterAll.apply(stream.reduce(supplier.get(), aggregator));
    }
}
