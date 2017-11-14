package com.utils.streamjava7.classes;

import com.utils.streamjava7.interfaces.Function;
import com.utils.streamjava7.interfaces.parallel.ParallelFunction;
import org.apache.commons.collections4.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class ParallelStream<T> extends Stream<T> {

    private static final Integer chunkSize = 4;
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(chunkSize);
    private List<Stream<T>> chunked;

    private ParallelStream(List<T> coll) {
        super(coll);
        this.chunked = chunkList();
    }

    public static <T> ParallelStream<T> of(List<T> coll) {
        return new ParallelStream<>(coll);
    }

    @Override
    public <A> Stream<A> map(Function<T, A> function) {
        return executeMap(chunked, function);
    }


    private List<Stream<T>> chunkList() {
        final Stream<Stream<T>> map = Stream.of(ListUtils.partition(super.collect(Collectors.toList(new ArrayList<T>())), chunkSize))
                .map(new Function<List<T>, Stream<T>>() {
                    @Override
                    public Stream<T> apply(List<T> start) {
                        return Stream.of(start);
                    }
                });
        return map.collect(Collectors.toList(new ArrayList<Stream<T>>()));
    }

    private <A> Stream<A> executeMap(final List<Stream<T>> inList, final Function<T, A> function) {
        final List<ParallelFunction<T, A>> list = Stream.of(inList).map(new Function<Stream<T>, ParallelFunction<T, A>>() {
            @Override
            public ParallelFunction<T, A> apply(Stream<T> start) {
                return new ParallelFunction<>(start, function);
            }
        }).collect(Collectors.toList(new ArrayList<ParallelFunction<T, A>>()));
        try {
            return Stream.of(EXECUTOR.invokeAll(list)).flatMap(new Function<Future<Stream<A>>, Stream<A>>() {
                public Stream<A> apply(Future<Stream<A>> start) {
                    try {
                        return start.get();
                    } catch (InterruptedException | ExecutionException e) {
                        return Stream.empty();
                    }
                }
            });
        } catch (InterruptedException e) {
            return Stream.empty();
        }

    }

}
