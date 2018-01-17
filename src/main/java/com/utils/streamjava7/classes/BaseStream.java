package com.utils.streamjava7.classes;

import com.utils.streamjava7.collection.Pipeline;
import com.utils.streamjava7.interfaces.*;
import com.utils.streamjava7.interfaces.innerFunction.ToIntegerFunction;
import org.apache.commons.lang3.tuple.Pair;


import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Created by simon.calabrese on 15/11/2017.
 */
public abstract class BaseStream<T> {
    protected Pipeline<T> pipeline;
    public abstract <U> Stream<U> map(final Function<T, U> mapper);
    public abstract IntStream mapToInt(final ToIntegerFunction<T> func);
    public abstract Optional<T> reduce();
    public abstract Optional<T> reduce(final BinaryOperator<T> operator);
    public abstract <U> U reduce(final U identity, final BiFunction<U, T, U> biMapper);
    public abstract Stream<T> filter(final Predicate<T> pattern);
    public abstract <U> Stream<U> flatMap(Function<T, Stream<U>> mapper);
    public abstract Stream<T> peek(final UnaryOperator<T> consumer);
    public abstract void forEach(final Consumer<T> consumer);
    public abstract Optional<T> findFirst();
    public abstract Long count();
    public abstract Boolean anyMatch(final Predicate<T> pattern);
    public abstract Boolean allMatch(final Predicate<T> pattern);
    public abstract <U, M> M collect(final Collector<U, T, M> collector);
    public abstract Stream<T> sorted(Comparator<T> comparator);
    public abstract <U> Stream<T> distinct(final Function<T, U> mapToAttribute);
    public abstract <U> PairStream<T, U> zip(final Collection<U> coll);
    public abstract <U> PairStream<T, U> zipLeft(final Collection<U> coll);
    public abstract <U> PairStream<T, U> zipRight(final Collection<U> coll);
    public abstract ParallelStream<T> parallel();
    public abstract Pair<List<T>,List<T>> partition(final Predicate<T> pattern);
    public abstract Stream<T> takeWhile(final Predicate<T> pattern);
    public abstract Stream<T> dropWhile(final Predicate<T> pattern);
    public abstract Pair<List<T>, List<T>> span(final Predicate<T> pattern);
    public abstract Stream<Stream<T>> pack();
}
