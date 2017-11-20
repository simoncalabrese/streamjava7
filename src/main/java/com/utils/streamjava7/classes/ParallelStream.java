package com.utils.streamjava7.classes;

import com.utils.streamjava7.interfaces.BiFunction;
import com.utils.streamjava7.interfaces.Function;
import com.utils.streamjava7.interfaces.Predicate;
import com.utils.streamjava7.interfaces.parallel.ParallelFunction;
import org.apache.commons.collections4.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


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

    @Override
    public Stream<T> filter(final Predicate<T> pattern) {
        final List<Callable<Stream<T>>> callables = Stream.of(chunked).map(new Function<Stream<T>, Callable<Stream<T>>>() {
            @Override
            public Callable<Stream<T>> apply(final Stream<T> start) {
                return new Callable<Stream<T>>() {
                    @Override
                    public Stream<T> call() throws Exception {
                        return start.filter(pattern);
                    }
                };
            }
        }).collect(Collectors.toList(new ArrayList<Callable<Stream<T>>>()));
        return execute(callables);
    }

    @Override
    public Boolean anyMatch(final Predicate<T> pattern) {
        final List<Callable<Boolean>> callables = Stream.of(chunked).map(new Function<Stream<T>, Callable<Boolean>>() {
            @Override
            public Callable<Boolean> apply(final Stream<T> start) {
                return new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return start.anyMatch(pattern);
                    }
                };
            }
        }).collect(Collectors.toList(new ArrayList<Callable<Boolean>>()));
        return executeFinalBoolean(callables);
    }

    @Override
    public Boolean allMatch(final Predicate<T> pattern) {
        final List<Callable<Boolean>> callables = Stream.of(chunked).map(new Function<Stream<T>, Callable<Boolean>>() {
            @Override
            public Callable<Boolean> apply(final Stream<T> start) {
                return new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return start.allMatch(pattern);
                    }
                };
            }
        }).collect(Collectors.toList(new ArrayList<Callable<Boolean>>()));
        return executeFinalBoolean(callables);
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

    /**
     * @param inList   list chuncked to execute in parallel
     * @param function mapper function
     * @param <A>      type of result stream
     * @return Stream mapped
     */
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

    private Stream<T> execute(final List<Callable<Stream<T>>> callables) {
        try {
            return Stream.of(EXECUTOR.invokeAll(callables)).flatMap(new Function<Future<Stream<T>>, Stream<T>>() {
                @Override
                public Stream<T> apply(Future<Stream<T>> start) {
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

    private Boolean executeFinalBoolean(List<Callable<Boolean>> callables) {
        try {
            return Stream.of(EXECUTOR.invokeAll(callables)).reduce(Boolean.FALSE, new BiFunction<Boolean, Future<Boolean>, Boolean>() {
                @Override
                public Boolean apply(Boolean elem1, Future<Boolean> elem2) {
                    try {
                        return elem1 || elem2.get();
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
