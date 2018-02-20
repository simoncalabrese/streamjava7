package com.utils.streamjava7.classes.streamableCollections.interfaces;


import com.utils.streamjava7.classes.Collectors;
import com.utils.streamjava7.classes.Optional;
import com.utils.streamjava7.classes.ParallelStream;
import com.utils.streamjava7.classes.Stream;
import com.utils.streamjava7.interfaces.*;
import com.utils.streamjava7.interfaces.innerFunction.ToDoubleFunction;
import com.utils.streamjava7.interfaces.innerFunction.ToIntegerFunction;
import com.utils.streamjava7.classes.streamableCollections.classes.StreamableSequence;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public abstract class StreamableList<T> extends StreamableCollection<T> implements List<T> {
    public StreamableList(Collection<T> coll) {
        super(coll);
    }

    protected void replacePipeline(final Collection<T> coll) {
        stream = Stream.of(coll);
    }

    private <U> StreamableList<U> toStreamableList(final Stream<U> stream) {
        return new StreamableSequence<>(stream.collect(Collectors.toList(new ArrayList<U>())));
    }


    public <U> StreamableList<U> map(Function<T, U> mapper) {
        return toStreamableList(super.stream.map(mapper));
    }

    public StreamableList<Integer> mapToInt(ToIntegerFunction<T> func) {
        return toStreamableList(super.stream.mapToInt(func));
    }

    public StreamableList<Double> mapToDouble(ToDoubleFunction<T> func) {
        return toStreamableList(super.stream.mapToDouble(func));
    }

    public StreamableList<T> filter(Predicate<T> pattern) {
        return toStreamableList(super.stream.filter(pattern));
    }

    public <U> StreamableList<U> flatMap(Function<T, Stream<U>> mapper) {
        return toStreamableList(super.stream.flatMap(mapper));
    }

    public StreamableList<T> peek(UnaryOperator<T> consumer) {
        return toStreamableList(super.stream.peek(consumer));
    }

    public StreamableList<T> sorted(Comparator<T> comparator) {
        return toStreamableList(super.stream.sorted(comparator));
    }

    public StreamableList<T> distinct() {
        return toStreamableList(super.stream.distinct());
    }

    public <U> StreamableList<T> distinct(Function<T, U> mapToAttribute) {
        return toStreamableList(super.stream.distinct(mapToAttribute));
    }

    public StreamableList<T> skip(int numToSkip) {
        return toStreamableList(super.stream.skip(numToSkip));
    }

    public <U> StreamableList<Pair<T, U>> zip(Collection<U> coll) {
        return toStreamableList(super.stream.zip(coll));
    }

    public <U> StreamableList<Pair<T, U>> zipLeft(Collection<U> coll) {
        return toStreamableList(super.stream.zipLeft(coll));
    }

    public <U> StreamableList<Pair<T, U>> zipRight(Collection<U> coll) {
        return toStreamableList(super.stream.zipRight(coll));
    }

    public StreamableList<StreamableList<T>> split(Integer chunkSize) {
        return toStreamableList(super.stream.split(chunkSize)).map(new Function<Stream<T>, StreamableList<T>>() {
            @Override
            public StreamableList<T> apply(Stream<T> start) {
                return toStreamableList(start);
            }
        });
    }

    public StreamableList<T> takeWhile(Predicate<T> pattern) {
        return toStreamableList(super.stream.takeWhile(pattern));
    }

    public StreamableList<T> dropWhile(Predicate<T> pattern) {
        return toStreamableList(super.stream.dropWhile(pattern));
    }

    public StreamableList<StreamableList<T>> pack() {
        return toStreamableList(super.stream.pack()).map(new Function<Stream<T>, StreamableList<T>>() {
            @Override
            public StreamableList<T> apply(Stream<T> start) {
                return toStreamableList(start);
            }
        });
    }

    public Optional<T> reduce() {
        return super.stream.reduce();
    }

    public Optional<T> reduce(BinaryOperator<T> operator) {
        return super.stream.reduce(operator);
    }

    public <U> U reduce(U identity, BiFunction<U, T, U> biMapper) {
        return super.stream.reduce(identity, biMapper);
    }

    public void forEach(Consumer<T> consumer) {
        super.stream.forEach(consumer);
    }

    public void forEachOrdered(Consumer<T> consumer, Comparator<T> comparator) {
        super.stream.forEachOrdered(consumer, comparator);
    }

    public void forEachOrdered(Consumer<T> consumer) {
        super.stream.forEachOrdered(consumer);
    }

    public Optional<T> findFirst() {
        return super.stream.findFirst();
    }

    public Long count() {
        return super.stream.count();
    }

    public Boolean anyMatch(Predicate<T> pattern) {
        return super.stream.anyMatch(pattern);
    }

    public Boolean allMatch(Predicate<T> pattern) {
        return super.stream.allMatch(pattern);
    }

    public <U, M> M collect(Collector<U, T, M> collector) {
        return super.stream.collect(collector);
    }

    public ParallelStream<T> parallel() {
        return super.stream.parallel();
    }

    public Pair<List<T>, List<T>> partition(Predicate<T> pattern) {
        return super.stream.partition(pattern);
    }

    public Pair<List<T>, List<T>> span(Predicate<T> pattern) {
        return super.stream.span(pattern);
    }
}
