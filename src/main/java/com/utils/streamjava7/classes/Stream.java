package com.utils.streamjava7.classes;

import com.utils.streamjava7.interfaces.*;
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

    /**
     * Create a stream starting by a collection
     * @param collection param to insert in pipeline
     * @param <T> type of collection
     * @return Stream of <T>
     */
    public static <T> Stream<T> of(final Collection<T> collection) {
        return new Stream<>(collection);
    }

    /**
     * Create a stream starting by array
     * @param array to transform in stream insert into pipeline
     * @param <T> type of collection
     * @return Stream of <T>
     */
    public static <T> Stream<T> of(T... array) {
        return new Stream<>(new ArrayList<>(Arrays.asList(array)));
    }

    /**
     * Create empty stream of T
     * @param <T>
     * @return
     */
    public static <T> Stream<T> empty() {
        return new Stream<>(new ArrayList<T>());
    }

    /**
     * Concat collections boot two streams and returns one
     * @param streamA
     * @param streamB
     * @param <T>
     * @return stream results by merge of two streams
     */
    public static <T> Stream<T> concat(final Stream<T> streamA, final Stream<T> streamB) {
        final Collection<T> newColl = streamA.getPipeline().getColl();
        newColl.addAll(streamB.getPipeline().getColl());
        return new Stream<T>(newColl);
    }

    /**
     * execute mapper from Stream<T> to Stream<U>
     * @param mapper function of mapping
     * @param <U>
     * @return
     */
    public <U> Stream<U> map(final Function<T, U> mapper) {
        final Collection<U> newInstance = getPipeline().getNewInstance();
        for (T elem : getPipeline().getColl()) {
            newInstance.add(mapper.apply(elem));
        }
        return new Stream<>(newInstance);
    }

    /**
     *
     * @param func IntFunction for map stream to IntStream
     * @return @IntStream
     */
    public IntStream mapToInt(final ToIntegerFunction<T> func) {
        return new IntStream(map(func));
    }

    /**
     * perform reducing that returns the last element of a collection
     * @return
     */
    public Optional<T> reduce() {
        final BinaryOperator<T> identityOpearator = new BinaryOperator<T>() {
            public T apply(T elem1, T elem2) {
                return elem2;
            }
        };
        return reduce(identityOpearator);
    }

    /**
     * Perform a reduce with BinaryOperator and return result of all elements logic
     * @param operator reducer mapper
     * @return Optional of element in collection
     */
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

    /**
     *
     * @param identity accumulator to return
     * @param biMapper combiner for reducing collection in accumulator
     * @param <U>
     * @return
     */
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

    /**
     * Return filtered collection by pattern as e Predicate
     * @param pattern
     * @return
     */
    public Stream<T> filter(final Predicate<T> pattern) {
        final Collection<T> newInstance = getPipeline().getNewInstance();
        for (final T elem : getPipeline().getColl()) {
            if (pattern.test(elem)) {
                newInstance.add(elem);
            }
        }
        return new Stream<>(newInstance);
    }

    /**
     *
     * @param mapper function that converts T to stream of U and flat all streams in only
     *               one Stream
     * @param <U>
     * @return
     */
    public <U> Stream<U> flatMap(Function<T, Stream<U>> mapper) {
        return this.map(mapper).reduce(new BinaryOperator<Stream<U>>() {
            @Override
            public Stream<U> apply(Stream<U> elem1, Stream<U> elem2) {
                return concat(elem1, elem2);
            }
        }).orElse(Stream.<U>empty());
    }

    /**
     *
     * @param consumer UnaryOperator that decorate each element in Stream
     * @return
     */
    public Stream<T> peek(final UnaryOperator<T> consumer) {
        return of(getPipeline().getColl()).map(consumer);
    }

    public void forEach(final Consumer<T> consumer) {
        for (T t : getPipeline().getColl()) {
            consumer.consume(t);
        }
    }

    /**
     *
     * @return the first element of stream as an optional or empty if Stream is empty
     */
    public Optional<T> findFirst() {
        return Optional.of(collect(Collectors.toList(new ArrayList<T>())).get(0));
    }

    /**
     *
     * @return the elements in list
     */
    public Long count() {
        return (long) pipeline.getColl().size();
    }

    /**
     * Return by a pattern true if one element match with pattern otherwise false
     * @param pattern Predicate of T
     * @return
     */
    public Boolean anyMatch(final Predicate<T> pattern) {
        return Long.compare(filter(pattern).count(), 1L) >= 1;
    }

    /**
     * Return true if all elements in stream match with pattern otherwise false
     * @param pattern
     * @return
     */
    public Boolean allMatch(final Predicate<T> pattern) {
        return Long.compare(of(pipeline.getColl()).count(), of(pipeline.getColl()).filter(pattern).count()) == 0;
    }

    /**
     *  Collect strem in Collection collected by selected collector
     *  See Collectors class for take legacy collectors
     * @param collector
     * @param <U> Map<key, list of T>
     * @param <M> Final Map collected by downstream
     * @return Map
     */
    public <U, M> M collect(final Collector<U, T, M> collector) {
        return collector.collect(this);
    }

    /**
     * Return a sorted Stream by Comparator
     * @param comparator sorter criteria
     * @return
     */
    public Stream<T> sorted(Comparator<T> comparator) {
        Collections.sort(collect(Collectors.toList(new ArrayList<T>())), comparator);
        return this;
    }
}
