package classes;

import interfaces.BiFunction;
import interfaces.Function;
import interfaces.Optional;
import interfaces.Predicate;
import pipeline.Pipeline;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * Created by simon.calabrese on 26/06/2017.
 */
public class Stream<T> {
    private Pipeline<T> pipeline;

    private Stream(final Collection<T> coll) {
        this.pipeline = new Pipeline<>(coll);
    }

    private Stream(final Pipeline<T> pipeline) {
        this.pipeline = pipeline;
    }

    public static<T> Stream<T> of(Collection<T> collection) {
        return new Stream<>(collection);
    }

    public <A> Stream<A> map(final Function<T,A> function) {
        Pipeline<A> tmp = pipeline.emptyPipeline();
        for(T elem:pipeline.getCollection()) {
            tmp.add(function.apply(elem));
        }
        return new Stream<>(tmp);
    }

    public Stream<T> filter(final Predicate<T> predicate) {
        final Pipeline<T> tmp = pipeline.clone();
        for(T elem:pipeline.getCollection()) {
            if(!predicate.test(elem)) {
                tmp.remove(elem);
            }
        }
        return new Stream<>(tmp);
    }

    public T reduce() {
        T accumulator = null;
        for(T elem:pipeline.getCollection()) {
            accumulator = elem;
        }
        return accumulator;
    }

    public T reduce(BiFunction<T,T,T> biFunction) {
        Iterator<T> iterator = pipeline.iterator();
        T acc = iterator.next();
        while(iterator.hasNext()) {
            acc = biFunction.apply(acc,iterator.next());
        }
        return acc;
    }

    public <U> Optional<U> reduce(U identity, BiFunction<U,T,U> aggregator) {
        for (Iterator<T> it = pipeline.iterator(); it.hasNext(); ) {
            final T elem = it.next();
            identity = aggregator.apply(identity,elem);
        }
        return Optional.of(identity);
    }

    public List<T> toList() {
        return pipeline.toList();
    }

    public Set<T> toSet() {
        return pipeline.toSet();
    }
}
