package com.utils.streamjava7.classes;

import com.utils.streamjava7.collection.Pipeline;
import com.utils.streamjava7.collection.PipelineImpl;
import com.utils.streamjava7.interfaces.*;
import com.utils.streamjava7.interfaces.innerFunction.ToDoubleFunction;
import com.utils.streamjava7.interfaces.innerFunction.ToIntegerFunction;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

/**
 * Created by simon.calabrese on 13/11/2017.
 */
public class Stream<T> extends BaseStream<T> {

    protected Stream() {
    }

    protected Stream(final Collection<T> coll) {
        super.pipeline = new PipelineImpl<>(Optional.of(coll).orElse(new ArrayList<T>()));
    }

    protected Stream(final Pipeline<T> pipeline) {
        super.pipeline = pipeline;
    }

    void replacePipeline(Collection<T> s) {
        this.pipeline = new PipelineImpl<>(s);
    }

    /**
     * Create a stream starting by a collection
     *
     * @param collection param to insert in pipeline
     * @param <T>        type of collection
     * @return Stream of <T>
     */
    public static <T> Stream<T> of(final Collection<T> collection) {
        return new Stream<>(collection);
    }

    /**
     * Create a stream starting by array
     *
     * @param array to transform in stream insert into pipeline
     * @param <T>   type of collection
     * @return Stream of <T>
     */
    public static <T> Stream<T> of(T... array) {
        return new Stream<>(new ArrayList<>(Arrays.asList(array)));
    }

    /**
     * Create empty stream of T
     *
     * @param <T>
     * @return
     */
    public static <T> Stream<T> empty() {
        return new Stream<>(new ArrayList<T>());
    }

    /**
     * Concat collections boot two streams and returns one
     *
     * @param streamA
     * @param streamB
     * @param <T>
     * @return stream results by merge of two streams
     */
    public static <T> Stream<T> concat(final Stream<T> streamA, final Stream<T> streamB) {
        return new Stream<>(streamA.getPipeline().merge(streamB.getPipeline()));

    }

    Pipeline<T> getPipeline() {
        return pipeline;
    }

    /**
     * execute mapper from Stream<T> to Stream<U>
     *
     * @param mapper function of mapping
     * @param <U>
     * @return
     */
    public <U> Stream<U> map(final Function<T, U> mapper) {
        final Pipeline<U> newSeq = new PipelineImpl<>(this.count());
        for (T t : this.getPipeline()) {
            newSeq.add(mapper.apply(t));
        }
        return new Stream<>(newSeq);
    }

    /**
     * @param func IntFunction for map stream to IntStream
     * @return @IntStream
     */
    public IntStream mapToInt(final ToIntegerFunction<T> func) {
        return new IntStream(map(func));
    }

    public DoubleStream mapToDouble(final ToDoubleFunction<T> func) {
        return new DoubleStream(map(func));
    }

    /**
     * perform reducing that returns the last element of a collection
     *
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
     *
     * @param operator reducer mapper
     * @return Optional of element in collection
     */
    public Optional<T> reduce(final BinaryOperator<T> operator) {
        if (!this.getPipeline().isNotEmpty()) {
            return Optional.empty();
        }
        Optional<T> op = Optional.of(this.getPipeline().head());
        for (final T t : this.getPipeline().tail()) {
            op = op.map(new Function<T, T>() {
                @Override
                public T apply(T start) {
                    return operator.apply(start, t);
                }
            });
        }
        return op;

    }

    /**
     * @param identity accumulator to return
     * @param biMapper combiner for reducing collection in accumulator
     * @param <U>
     * @return
     */
    public <U> U reduce(final U identity, final BiFunction<U, T, U> biMapper) {
        Optional<U> op = Optional.of(identity);
        for (final T elem : getPipeline()) {
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
     *
     * @param pattern
     * @return
     */
    public Stream<T> filter(final Predicate<T> pattern) {
        final Pipeline<T> pipelineImp = new PipelineImpl<>(this.count());
        for (T t : this.pipeline) {
            if (pattern.test(t)) {
                pipelineImp.add(t);
            }
        }
        this.pipeline = pipelineImp;
        return this;
    }

    /**
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
     * @param consumer UnaryOperator that decorate each element in Stream
     * @return
     */
    public Stream<T> peek(final UnaryOperator<T> consumer) {
        return new Stream<>(getPipeline()).map(consumer);
    }

    public void forEach(final Consumer<T> consumer) {
        for (T t : getPipeline()) {
            consumer.consume(t);
        }
    }

    public void forEachOrdered(final Consumer<T> consumer, final Comparator<T> comparator) {
        sorted(comparator).forEach(consumer);
    }

    public void forEachOrdered(final Consumer<T> consumer) {
        sorted(new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                return Integer.compare(o1.hashCode(), o2.hashCode());
            }
        }).forEach(consumer);
    }

    /**
     * @return the first element of stream as an optional or empty if Stream is empty
     */
    public Optional<T> findFirst() {
        return Optional.of(pipeline.head());
    }

    /**
     * @return the elements in list
     */
    public Long count() {
        return (long) pipeline.pipeline().length;
    }

    /**
     * Return by a pattern true if one element match with pattern otherwise false
     *
     * @param pattern Predicate of T
     * @return
     */
    public Boolean anyMatch(final Predicate<T> pattern) {
        return Long.compare(filter(pattern).count(), 1L) >= 1;
    }

    /**
     * Return true if all elements in stream match with pattern otherwise false
     *
     * @param pattern
     * @return
     */
    public Boolean allMatch(final Predicate<T> pattern) {
        for (T t : this.pipeline) {
            if (!pattern.test(t)) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    /**
     * Collect strem in Collection collected by selected collector
     * See Collectors class for take legacy collectors
     *
     * @param collector
     * @param <U>       Map<key, list of T>
     * @param <M>       Final Map collected by downstream
     * @return Map
     */
    public <U, M> M collect(final Collector<U, T, M> collector) {
        return collector.collect(this);
    }

    /**
     * Return a sorted Stream by Comparator
     *
     * @param comparator sorter criteria
     * @return
     */
    public Stream<T> sorted(Comparator<T> comparator) {
        final List<T> sorted = this.getPipeline().toList();
        Collections.sort(sorted, comparator);
        return new Stream<>(sorted);
    }

    public Stream<T> distinct() {
        final Function<T, T> identity = new Function<T, T>() {
            @Override
            public T apply(T start) {
                return start;
            }
        };
        return new Stream<>(collect(Collectors.toMap(identity, identity, new BinaryOperator<T>() {
            @Override
            public T apply(T elem1, T elem2) {
                return elem1;
            }
        })).keySet());
    }

    public <U> Stream<T> distinct(final Function<T, U> mapToAttribute) {
        return new Stream<>(collect(Collectors.toMap(mapToAttribute, new Function<T, T>() {
            @Override
            public T apply(T start) {
                return start;
            }
        }, new BinaryOperator<T>() {
            @Override
            public T apply(T elem1, T elem2) {
                return elem1;
            }
        })).values());
    }

    public Stream<T> skip(final int numToSkip) {
        for (int i = 0; i < numToSkip; i++) {
            pipeline = pipeline.tail();
        }
        return this;
    }

    /**
     * @param tails boot streams
     * @param acc   result accumulator
     * @param join  -1 left join, 0 inner join, 1 right join
     * @param <U>
     * @return
     */
    private <U> PairStream<T, U> recZip(final Pair<Pipeline<T>, Pipeline<U>> tails, PairStream<T, U> acc, final Integer join) {
        acc.pipeline.add(Pair.of(tails.getLeft().head(), tails.getRight().head()));
        final Pipeline<T> tail = tails.getKey().tail();
        final Pipeline<U> tail1 = tails.getValue().tail();
        final Boolean condition;
        switch (join) {
            case -1:
                condition = tail.isNotEmpty();
                break;
            case 0:
                condition = tail.isNotEmpty() && tail1.isNotEmpty();
                break;
            case 1:
                condition = tail1.isNotEmpty();
                break;
            default:
                condition = Boolean.FALSE;
                break;
        }
        if (condition) {
            recZip(Pair.of(tail, tail1), acc, join);
        }
        return acc;
    }

    public <U> PairStream<T, U> zip(final Collection<U> coll) {
        return recZip(Pair.of(this.pipeline, Stream.of(coll).pipeline),
                new PairStream<>(new PipelineImpl<Pair<T, U>>(this.count())), 0);
    }

    public <U> PairStream<T, U> zipLeft(final Collection<U> coll) {
        return recZip(Pair.of(this.pipeline, Stream.of(coll).pipeline),
                new PairStream<>(new PipelineImpl<Pair<T, U>>(this.count())), -1);
    }

    public <U> PairStream<T, U> zipRight(final Collection<U> coll) {
        return recZip(Pair.of(this.pipeline, Stream.of(coll).pipeline),
                new PairStream<>(new PipelineImpl<Pair<T, U>>(this.count())), 1);
    }

    public Stream<Stream<T>> split(final Integer chunkSize) {
        List<List<T>> partitions = new ArrayList<>();
        final int chunkList = pipeline.toList().size() % chunkSize;
        for (int i = 0; i < pipeline.toList().size(); i += chunkList) {
            partitions.add(new ArrayList<>(collect(Collectors.toList(new ArrayList<T>())).subList(i,
                    Math.min(i + chunkList, pipeline.toList().size()))));
        }
        return Stream.of(partitions).map(new Function<List<T>, Stream<T>>() {
            @Override
            public Stream<T> apply(List<T> start) {
                return Stream.of(start);
            }
        });
    }

    @Override
    public ParallelStream<T> parallel() {
        return new ParallelStream<>(pipeline);
    }

    @Override
    public Pair<List<T>, List<T>> partition(final Predicate<T> pattern) {
        return reduce(Pair.of((List<T>) new ArrayList<T>(), (List<T>) new ArrayList<T>()), new BiFunction<Pair<List<T>, List<T>>, T, Pair<List<T>, List<T>>>() {
            @Override
            public Pair<List<T>, List<T>> apply(Pair<List<T>, List<T>> elem1, T elem2) {
                if (pattern.test(elem2)) {
                    elem1.getKey().add(elem2);
                } else {
                    elem1.getValue().add(elem2);
                }
                return elem1;
            }
        });
    }

    @Override
    public Stream<T> takeWhile(final Predicate<T> pattern) {
        final Pipeline<T> newSeq = new PipelineImpl<>(this.count());
        //recursive function
        final BiFunction<Pipeline<T>, Pipeline<T>, Pipeline<T>> func = new BiFunction<Pipeline<T>, Pipeline<T>, Pipeline<T>>() {
            @Override
            public Pipeline<T> apply(Pipeline<T> actual, Pipeline<T> future) {
                if (pattern.test(actual.head())) {
                    return future;
                } else {
                    future.add(actual.head());
                    return apply(actual.tail(), future);
                }
            }
        };
        return new Stream<>(func.apply(this.pipeline, newSeq));

    }

    @Override
    public Stream<T> dropWhile(final Predicate<T> pattern) {
        final Pipeline<T> newSeq = new PipelineImpl<>(this.count());
        final BiFunction<Pipeline<T>, Pipeline<T>, Pipeline<T>> func = new BiFunction<Pipeline<T>, Pipeline<T>, Pipeline<T>>() {
            @Override
            public Pipeline<T> apply(Pipeline<T> actual, Pipeline<T> future) {
                if (pattern.test(actual.head())) {
                    future.addAll(actual);
                    return future;
                } else {
                    return apply(actual.tail(), future);
                }
            }
        };
        return new Stream<>(func.apply(this.pipeline, newSeq));
    }

    @Override
    public Pair<List<T>, List<T>> span(final Predicate<T> pattern) {
        final List<T> left = takeWhile(pattern).collect(Collectors.toList(new ArrayList<T>()));
        final List<T> right = dropWhile(pattern).collect(Collectors.toList(new ArrayList<T>()));
        return Pair.of(left, right);
    }


    private Pipeline<T> recursivePack(Pipeline<T> group, T lastElem) {
        final T head = this.pipeline.head();
        if (lastElem.equals(head)) {
            group.add(head);
            this.pipeline = pipeline.tail();
            return recursivePack(group, head);
        } else {
            return group;
        }
    }

    @Override
    public Stream<Stream<T>> pack() {
        final Pipeline<Pipeline<T>> pipeline = new PipelineImpl<>(count());
        while (this.pipeline.tail().isNotEmpty()) {
            pipeline.add(recursivePack(new PipelineImpl<T>(count()), this.pipeline.head()));
        }
        return new Stream<>(pipeline).map(new Function<Pipeline<T>, Stream<T>>() {
            @Override
            public Stream<T> apply(Pipeline<T> start) {
                return new Stream<>(start);
            }
        });
    }
}
