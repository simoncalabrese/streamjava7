package com.utils.streamjava7.classes;

import com.utils.streamjava7.interfaces.*;
import com.utils.streamjava7.interfaces.Optional;
import com.utils.streamjava7.interfaces.innerFunction.ToIntegerFunction;

import java.util.*;

/**
 * Created by simon.calabrese on 13/11/2017.
 */
public class Stream<T> {

    private Pipeline<T> pipeline;

    private Pipeline<T> getPipeline() {
        return pipeline;
    }

    protected Stream() {

    }

    protected Stream(final Collection<T> coll) {
        this.pipeline = new Pipeline<>(coll, coll.getClass());
    }

    public static <T> Stream<T> of(final Collection<T> collection) {
        return new Stream<>(collection);
    }

    public static <T> Stream<T> of(T... array) {
        return new Stream<>(new ArrayList<T>(Arrays.asList(array)));
    }

    public static <T> Stream<T> empty() {
        return new Stream<>(new ArrayList<T>());
    }

    public static <T> Stream<T> concat(final Stream<T> streamA, final Stream<T> streamB) {
        final Collection<T> newColl = streamA.getPipeline().getColl();
        newColl.addAll(streamB.getPipeline().getColl());
        return new Stream<T>(newColl);
    }

    public <U> Stream<U> map(final Function<T, U> mapper) {
        final Collection<U> newInstance = getPipeline().getNewInstance();
        for (T elem : getPipeline().getColl()) {
            newInstance.add(mapper.apply(elem));
        }
        return new Stream<>(newInstance);
    }

    public IntStream mapToInt(final ToIntegerFunction<T> func) {
        return new IntStream(map(func));
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
            op = op.map(new Function<U, U>() {
                @Override
                public U apply(U start) {
                    return biMapper.apply(start, elem);
                }
            });
        }
        return op.get();
    }

    public Stream<T> filter(final Predicate<T> pattern) {
        final Collection<T> newInstance = getPipeline().getNewInstance();
        for (final T elem : getPipeline().getColl()) {
            if (pattern.test(elem)) {
                newInstance.add(elem);
            }
        }
        return new Stream<>(newInstance);
    }

    public <U> Stream<U> flatMap(Function<T, Stream<U>> mapper) {
        return this.map(mapper).reduce(new BinaryOperator<Stream<U>>() {
            @Override
            public Stream<U> apply(Stream<U> elem1, Stream<U> elem2) {
                return concat(elem1, elem2);
            }
        }).orElse(Stream.<U>empty());
    }

    public Stream<T> peek(final UnaryOperator<T> consumer) {
        return of(getPipeline().getColl()).map(consumer);
    }

    public void forEach(final Consumer<T> consumer) {
        for (T t : getPipeline().getColl()) {
            consumer.consume(t);
        }
    }

    public Optional<T> findFirst() {
        return Optional.of(collect(Collectors.toList(new ArrayList<T>())).get(0));
    }

    public Long count() {
        return (long) pipeline.getColl().size();
    }

    public Boolean anyMatch(final Predicate<T> pattern) {
        return Long.compare(filter(pattern).count(), 1L) >= 1;
    }

    public Boolean allMatch(final Predicate<T> pattern) {
        return Long.compare(of(pipeline.getColl()).count(), of(pipeline.getColl()).filter(pattern).count()) == 0;
    }

    public <U, M> M collect(final Collector<U, T, M> collector) {
        return collector.collect(this);
    }

    public Stream<T> sorted(Comparator<T> comparator) {
        Collections.sort(collect(Collectors.toList(new ArrayList<T>())), comparator);
        return this;
    }
}
