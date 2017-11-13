package classes;

import interfaces.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by simon.calabrese on 26/06/2017.
 */
public class Stream<T> {

    private List<T> coll;

    Stream(final List<T> coll) {
        this.coll = coll;
    }

    public static <T> Stream<T> of(Collection<T> collection) {
        return new Stream<>(new ArrayList<>(collection));
    }

    public static <T> Stream<T> of(List<T> collection) {
        return new Stream<>(Optional.of(collection).orElse(new ArrayList<T>()));
    }

    public static <T> Stream<T> of(T... array) {
        List<T> vettList = new ArrayList<>(Arrays.asList(array));
        return new Stream<>(vettList);
    }

    public static <T> Stream<T> concat(Stream<T> streamA, Stream<T> streamB) {
        final List<T> coll = new ArrayList<>(streamA.coll);
        coll.addAll(streamB.coll);
        return new Stream<>(coll);
    }

    public static <T> Stream<T> empty() {
        return new Stream<>(new ArrayList<T>());
    }

    public <A> Stream<A> map(final Function<T, A> function) {
        List<A> tmp = new ArrayList<>(coll.size());
        for (T elem : coll) {
            tmp.add(function.apply(elem));
        }
        return new Stream<>(tmp);
    }

    public Stream<T> filter(final Predicate<T> predicate) {
        final BiFunction<ArrayList<T>, T, ArrayList<T>> reducer = new BiFunction<ArrayList<T>, T, ArrayList<T>>() {
            public ArrayList<T> apply(ArrayList<T> elem1, T elem2) {
                if (predicate.test(elem2))
                    elem1.add(elem2);
                return elem1;
            }
        };
        return Stream.of(reduce(new ArrayList<T>(), reducer).orElse(null));
    }

    public T reduce() {
        T accumulator = null;
        if (coll != null) {
            for (T elem : coll) {
                accumulator = elem;
            }
        }
        return accumulator;
    }

    public T reduce(BiFunction<T, T, T> biFunction) {
        final Iterator<T> iterator = coll.iterator();
        T acc = iterator.next();
        while (iterator.hasNext()) {
            acc = biFunction.apply(acc, iterator.next());
        }
        return acc;
    }

    public <U> Optional<U> reduce(U identity, BiFunction<U, T, U> aggregator) {
        for (T elem : coll) {
            identity = aggregator.apply(identity, elem);
        }
        return Optional.of(identity);
    }

    public Optional<T> findFirst() {
        if (coll != null && coll.size() > 0) {
            return Optional.of(coll.get(0));
        } else {
            return Optional.empty();
        }
    }

    public List<T> toList() {
        return coll;
    }

    public <K> String joining(final Function<T, K> mapper, final String delimiter) {
        return map(mapper).reduce(null, new BiFunction<String, K, String>() {
            @Override
            public String apply(String elem1, K elem2) {
                if (elem1 != null && elem2 == null) {
                    return elem1;
                } else if (elem1 == null && elem2 != null) {
                    return elem2.toString();
                } else {
                    return elem1.concat(delimiter).concat(elem2.toString());
                }
            }
        }).orElse(null);
    }

    public String joining() {
        return joining(new Function<T, T>() {
            public T apply(T start) {
                return start;
            }
        }, "");
    }

    public String joining(final String delimiter) {
        return joining(new Function<T, T>() {
            public T apply(T start) {
                return start;
            }
        }, delimiter);
    }

    public <K> Map<K, List<T>> groupBy(final Function<T, K> key) {
        final Map<K, List<T>> groupped = new HashMap<>();
        for (final T elem : coll) {
            final List<T> subListed = Stream.of(coll).filter(new Predicate<T>() {
                public Boolean test(T e) {
                    return key.apply(elem).equals(key.apply(e));
                }
            }).toList();
            groupped.put(key.apply(elem), subListed);
        }
        return groupped;
    }

    public <K, U> Map<K, List<U>> groupBy(final Function<T, K> key, final Function<T, U> valueMapper) {
        final Map<K, List<U>> map = new HashMap<>();
        for (final T elem : coll) {
            final List<U> subList = Stream.of(coll).filter(new Predicate<T>() {
                public Boolean test(T object) {
                    return key.apply(elem).equals(key.apply(object));
                }
            }).map(valueMapper).toList();
            map.put(key.apply(elem), subList);
        }
        return map;
    }

    public <K, U> Map<K, U> groupBy(final Function<T, K> key, U identity, BiFunction<U, T, U> collector) {
        final Map<K, List<T>> groupped = groupBy(key);
        final Map<K, U> toreturn = new HashMap<K, U>();
        for (Map.Entry<K, List<T>> row : groupped.entrySet()) {
            toreturn.put(row.getKey(), Stream.of(row.getValue()).reduce(identity, collector).orElse(null));
        }
        return toreturn;
    }

    public <K, U> Map<K, U> toMap(final Function<T, K> keyMapper, final Function<T, U> valueMapper) {
        final Map<K, U> hashMap = new HashMap<>();
        for (T elem : coll) {
            hashMap.put(keyMapper.apply(elem), valueMapper.apply(elem));
        }
        return hashMap;
    }

    public <K, U> Map<K, U> toMap(final Function<T, K> keyMapper, final Function<T, U> valueMapper,
                                  final BiFunction<K, K, K> mergeConflict) {
        final Map<K, U> hashMap = new HashMap<>();
        for (T elem : coll) {
            final K key = keyMapper.apply(elem);
            hashMap.put(!hashMap.containsKey(key) ? key : mergeConflict.apply(key, (K) hashMap.get(key)),
                    valueMapper.apply(elem));
        }
        return hashMap;
    }

    public <U> Stream<U> flatMap(Function<T, Stream<U>> mapper) {
        return this.map(mapper).reduce(new BiFunction<Stream<U>, Stream<U>, Stream<U>>() {
            @Override
            public Stream<U> apply(Stream<U> elem1, Stream<U> elem2) {
                return concat(elem1, elem2);
            }
        });
    }

    public void forEach(final Consumer<T> consumer) {
        for (final Iterator<T> iter = this.coll.iterator(); iter.hasNext(); ) {
            consumer.consume(iter.next());
        }
    }

    public Double summingDouble(Function<T, Double> mapper) {
        return this.coll != null && this.coll.size() > 0
                ? this.map(mapper).reduce(new BiFunction<Double, Double, Double>() {
            @Override
            public Double apply(final Double elem1, final Double elem2) {
                return Optional.of(elem1)
                        .map(new Function<Double, Double>() {
                            public Double apply(final Double one) {
                                return Optional.of(elem2).map(new Function<Double, Double>() {
                                    @Override
                                    public Double apply(Double two) {
                                        return one + two;
                                    }
                                }).orElse(one);
                            }
                        }).orElse(0D);
            }
        }) : 0D;
    }

    public Long count() {
        return (long) coll.size();
    }

    public Boolean anyMatch(final Predicate<T> pattern) {
        return Long.compare(filter(pattern).count(), 1L) >= 1;
    }

    public Boolean allMatch(final Predicate<T> pattern) {
        return Long.compare(Stream.of(coll).count(), Stream.of(coll).filter(pattern).count()) == 0;
    }

    public Stream<T> sorted(Comparator<T> comparator) {
        Collections.sort(coll, comparator);
        return this;
    }
}
