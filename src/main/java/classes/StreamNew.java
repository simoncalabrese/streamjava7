package classes;

import interfaces.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by simon.calabrese on 13/11/2017.
 */
public class StreamNew<T> {

    private Pipeline<T> pipeline;

    private Pipeline<T> getPipeline() {
        return pipeline;
    }

    protected StreamNew(final Collection<T> coll) {
        this.pipeline = new Pipeline<>(coll, coll.getClass());
    }

    public static <T> StreamNew<T> of(final Collection<T> collection) {
        return new StreamNew<>(collection);
    }

    public static <T> StreamNew<T> of(T... array) {
        return new StreamNew<>(new ArrayList<T>(Arrays.asList(array)));
    }

    public static <T> StreamNew<T> empty() {
        return new StreamNew<T>(new ArrayList<T>());
    }

    public static <T> StreamNew<T> concat(final StreamNew<T> streamA, final StreamNew<T> streamB) {
        final Collection<T> newColl = streamA.getPipeline().getColl();
        newColl.addAll(streamB.getPipeline().getColl());
        return new StreamNew<T>(newColl);
    }

    public <U> StreamNew<U> map(final Function<T, U> mapper) {
        final Collection<U> newInstance = getPipeline().getNewInstance();
        for (T elem : getPipeline().getColl()) {
            newInstance.add(mapper.apply(elem));
        }
        return new StreamNew<>(newInstance);
    }

    public Optional<T> reduce() {
        final BinaryOperator<T> identityOpearator = new BinaryOperator<T>() {
            public T apply(T elem1, T elem2) {
                return elem2;
            }
        };
        return reduce(identityOpearator);
    }

    public Optional<T> reduce(final BinaryOperator<T> operator) {
        final Optional<T> op = Optional.empty();
        for (final T elem : getPipeline().getColl()) {
            op.map(new UnaryOperator<T>() {
                public T apply(T start) {
                    return operator.apply(op.get(), elem);
                }
            });
        }
        return op;
    }

    public <U> U reduce(final U identity, final BiFunction<U, T, U> biMapper) {
        Optional<U> op = Optional.of(identity);
        for (final T elem : getPipeline().getColl()) {
           op =  op.map(new Function<U, U>() {
                @Override
                public U apply(U start) {
                    return biMapper.apply(start, elem);
                }
            });
        }
        return op.get();
    }

    public StreamNew<T> filter(final Predicate<T> pattern) {
        final Collection<T> newInstance = getPipeline().getNewInstance();
        for (final T elem : getPipeline().getColl()) {
            if (pattern.test(elem)) {
                newInstance.add(elem);
            }
        }
        return new StreamNew<>(newInstance);
    }

    public <U> StreamNew<U> flatMap(Function<T, StreamNew<U>> mapper) {
        return this.map(mapper).reduce(new BinaryOperator<StreamNew<U>>() {
            @Override
            public StreamNew<U> apply(StreamNew<U> elem1, StreamNew<U> elem2) {
                return concat(elem1, elem2);
            }
        }).orElse(StreamNew.<U>empty());
    }

    public StreamNew<T> peek(final UnaryOperator<T> consumer) {
        return StreamNew.of(getPipeline().getColl()).map(consumer);
    }

    public void forEach(final Consumer<T> consumer) {
        for (T t : getPipeline().getColl()) {
            consumer.consume(t);
        }
    }

    public<U,M> M collect(Collector<U,T,M> collector) {
        return collector.collect(this);
    }
}
