package classes;

import interfaces.Function;
import interfaces.Predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by simon.calabrese on 26/06/2017.
 */
public class Stream<T> {
    private Collection<T> coll;

    private Stream(final Collection<T> coll) {
        this.coll = coll;
    }

    public static<T> Stream<T> of(Collection<T> collection) {
        return new Stream<>(collection);
    }

    public <A> Stream<A> map(final Function<T,A> function) {
        Collection<A> tmp = new ArrayList<>(coll.size());
        for(T elem:coll) {
            tmp.add(function.apply(elem));
        }
        return new Stream<>(tmp);
    }

    public Stream<T> filter(final Predicate<T> predicate) {
        Collection<T> tmp = new ArrayList<>(coll);
        for(T elem:coll) {
            if(!predicate.test(elem)) {
                tmp.remove(elem);
            }
        }
        return new Stream<>(tmp);
    }

    public Collection<T> toList() {
        return coll;
    }
}
