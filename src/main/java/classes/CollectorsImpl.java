package classes;

import interfaces.BiFunction;
import interfaces.Collectors;
import interfaces.Consumer;
import pipeline.Pipeline;

import java.util.Collection;
import java.util.IdentityHashMap;

/**
 * Created by simon.calabrese on 27/06/2017.
 */
public class CollectorsImpl<U, C extends Collection<U>> implements Collectors<U, C> {

    private Pipeline<U> pipeline;
    private C identity;
    private BiFunction<C, U, C> aggregate;

    public CollectorsImpl(final Pipeline<U> pipeline,
                          final C identity,
                          final BiFunction<C, U, C> aggregate) {
        this.pipeline = pipeline;
        this.identity = identity;
        this.aggregate = aggregate;
    }

    @Override
    public C collect() {
        for(U elem:this.pipeline.getCollection()) {
            this.identity = this.aggregate.apply(identity,elem);
        }
        return identity;
    }
}
