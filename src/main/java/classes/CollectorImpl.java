package classes;

import interfaces.*;

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
    public M collect(final StreamNew<T> streamNew) {
        return afterAll.apply(streamNew.reduce(supplier.get(), aggregator));
    }
}
