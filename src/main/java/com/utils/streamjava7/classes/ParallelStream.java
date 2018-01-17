package com.utils.streamjava7.classes;

import com.utils.streamjava7.collection.Pipeline;
import com.utils.streamjava7.interfaces.Function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.*;


public class ParallelStream<T> extends Stream<T> {

    private static final Integer chunkSize = 4;
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(chunkSize);
    private Stream<Stream<T>> chunked;

    private ParallelStream(Collection<T> coll) {
        super(coll);
        chunked = super.split(chunkSize);
    }

    public ParallelStream(Pipeline<T> pipeline) {
        super(pipeline);
        this.chunked = super.split(chunkSize);
    }

    public static <T> ParallelStream<T> of(Collection<T> coll) {
        return new ParallelStream<>(coll);
    }

    @Override
    public <U> Stream<U> map(final Function<T, U> mapper) {
        try {
            final ArrayList<Callable<Stream<U>>> collect = chunked.map(new Function<Stream<T>, Callable<Stream<U>>>() {
                @Override
                public Callable<Stream<U>> apply(final Stream<T> start) {
                    return new Callable<Stream<U>>() {
                        @Override
                        public Stream<U> call() throws Exception {
                            return start.map(mapper);
                        }
                    };
                }
            }).collect(Collectors.toList(new ArrayList<Callable<Stream<U>>>()));

            return Stream.of(EXECUTOR.invokeAll(collect)).flatMap(new Function<Future<Stream<U>>, Stream<U>>() {
                @Override
                public Stream<U> apply(Future<Stream<U>> start) {
                    try {
                        return start.get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
