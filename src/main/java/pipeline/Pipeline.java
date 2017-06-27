package pipeline;

import classes.CollectorsImpl;
import classes.Stream;
import interfaces.BiFunction;
import interfaces.Consumer;
import interfaces.Function;
import jdk.nashorn.internal.ir.FunctionCall;

import java.util.*;

/**
 * Created by simon.calabrese on 27/06/2017.
 */
public class Pipeline<T> extends BasePipeline<T> implements Iterable<T> {
    private Collection<T> collection;

    private final BiFunction<List<T>,T,List<T>> TO_LIST_GENERIC = new BiFunction<List<T>, T, List<T>>() {
        @Override
        public List<T> apply(List<T> accumulator, T combiner) {
            accumulator.add(combiner);
            return accumulator;
        }
    };

    private final BiFunction<Set<T>,T,Set<T>> TO_SET_GENERIC = new BiFunction<Set<T>, T, Set<T>>() {
        @Override
        public Set<T> apply(Set<T> accumulator, T combiner) {
            accumulator.add(combiner);
            return accumulator;
        }
    };

    private final BiFunction<Collection<T>,T,Collection<T>> CLONE = new BiFunction<Collection<T>, T, Collection<T>>() {
        @Override
        public Collection<T> apply(Collection<T> accumulator, T combiner) {
            accumulator.add(combiner);
            return accumulator;
        }
    };

    public Pipeline(final Collection<T> collection) {
        this.collection = collection;
    }

    public <A> Pipeline<A> emptyPipeline() {
        final Collection<A> tmp = emptyPipeline(collection);
        return new Pipeline<>(tmp);
    }

    public void add(final T elem) {
        collection.add(elem);
    }

    public void remove(final T elem) {
        collection.remove(elem);
    }

    public List<T> toList() {
       return new CollectorsImpl<>(this,new ArrayList<T>(),TO_LIST_GENERIC).collect();
    }

    public Set<T> toSet() {
        return new CollectorsImpl<>(this,new HashSet<T>(),TO_SET_GENERIC).collect();
    }

    public Collection<T> getCollection() {
        return collection;
    }

    public Pipeline<T> clone() {
        final Collection<T> objects = emptyPipeline(collection);
        return new Pipeline<>(new CollectorsImpl<>(this,objects,CLONE).collect());
    }


    @Override
    public Iterator<T> iterator() {
        return new PipelineIterator<>(collection);
    }

    private class PipelineIterator<T> implements Iterator<T>{

        private Iterator<T> toIterate;

        public PipelineIterator(Collection<T> toIterate) {
            this.toIterate = toIterate.iterator();
        }

        @Override
        public boolean hasNext() {
            return toIterate.hasNext();
        }

        @Override
        public T next() {
            return toIterate.next();
        }

        @Override
        public void remove() {

        }
    }
}
