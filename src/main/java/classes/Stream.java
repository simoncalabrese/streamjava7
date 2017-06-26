package classes;

import interfaces.BiFunction;
import interfaces.Function;
import interfaces.Optional;
import interfaces.Predicate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by simon.calabrese on 26/06/2017.
 */
public class Stream<T> {
    private List<T> coll;

    private Stream(final List<T> coll) {
        this.coll = coll;
    }

    public static<T> Stream<T> of(List<T> collection) {
        return new Stream<>(collection);
    }

    public <A> Stream<A> map(final Function<T,A> function) {
        List<A> tmp = new ArrayList<>(coll.size());
        for(T elem:coll) {
            tmp.add(function.apply(elem));
        }
        return new Stream<>(tmp);
    }

    public Stream<T> filter(final Predicate<T> predicate) {
        List<T> tmp = new ArrayList<>(coll);
        for(T elem:coll) {
            if(!predicate.test(elem)) {
                tmp.remove(elem);
            }
        }
        return new Stream<>(tmp);
    }

    public T reduce() {
        T accumulator = null;
        for(T elem:coll) {
            accumulator = elem;
        }
        return accumulator;
    }

    public T reduce(BiFunction<T,T,T> biFunction) {
        Iterator<T> iterator = coll.iterator();
        T acc = iterator.next();
        while(iterator.hasNext()) {
            acc = biFunction.apply(acc,iterator.next());
        }
        return acc;
    }

    public <U> Optional<U> reduce(U identity, BiFunction<U,T,U> aggregator) {
        for(T elem:coll) {
            identity = aggregator.apply(identity,elem);
        }
        return Optional.of(identity);
    }

    public List<T> toList() {
        return coll;
    }
}
