package com.utils.streamjava7.classes;

import com.utils.streamjava7.interfaces.*;
import com.utils.streamjava7.interfaces.innerFunction.ToDoubleFunction;
import com.utils.streamjava7.interfaces.innerFunction.ToIntegerFunction;
import com.utils.streamjava7.interfaces.innerFunction.ToStringFunction;

import java.util.*;

/**
 * Created by simon.calabrese on 14/11/2017.
 */
@SuppressWarnings("unchecked")
public class Collectors {
    /**
     *
     * @param identity generic class that extends List
     * @param <U> List collected
     * @param <T> type of Stream
     * @return
     */
    public static <U extends List<T>, T> Collector<U, T, U> toList(final U identity) {
        return reducing(identity, new BiFunction<U, T, U>() {
            @Override
            public U apply(U elem1, T elem2) {
                elem1.add(elem2);
                return elem1;
            }
        });
    }

    /**
     *
     * @param keyMapper function used for mapping element T in key of map
     * @param valueMapper function used for mapping element T in value of map
     * @param <U> Map returned
     * @param <T> type of Stream
     * @param <K> Type of value
     * @param <R> Type of key
     * @return Map<R-->K>
     */
    public static <U extends Map<R, K>, T, K, R> Collector<U, T, U> toMap(final Function<T, R> keyMapper, final Function<T, K> valueMapper) {
        return toMap(keyMapper, valueMapper, new BinaryOperator<K>() {
            @Override
            public K apply(K elem1, K elem2) {
                return elem1;
            }
        });
    }

    /**
     *
     * @param keyMapper function used for mapping element T in key of map
     * @param valueMapper function used for mapping element T in value of map
     * @param keyResolver Binary Operator for resolving Key conflicts
     * @param <U> Map returned
     * @param <T>  type of Stream
     * @param <K>  Type of value
     * @param <R>  Type of key
     * @return
     */
    public static <U extends Map<R, K>, T, K, R> Collector<U, T, U> toMap(final Function<T, R> keyMapper,
                                                                          final Function<T, K> valueMapper,
                                                                          final BinaryOperator<K> keyResolver) {

        return toMap(keyMapper, valueMapper, (U) new HashMap<R, K>(), keyResolver);
    }

    /**
     *
     * @param keyMapper function used for mapping element T in key of map
     * @param valueMapper function used for mapping element T in value of map
     * @param keyResolver Binary Operator for resolving Key conflicts
     * @param identityMap Instance of U extends Map
     * @param <U> Map returned
     * @param <T>  type of Stream
     * @param <K>  Type of value
     * @param <R>  Type of key
     * @return
     */
    public static <U extends Map<R, K>, T, K, R> Collector<U, T, U> toMap(final Function<T, R> keyMapper,
                                                                          final Function<T, K> valueMapper,
                                                                          final U identityMap,
                                                                          final BinaryOperator<K> keyResolver) {

        return reducing(identityMap, new BiFunction<U, T, U>() {
            @Override
            public U apply(U elem1, T elem2) {
                if (elem1.containsKey(keyMapper.apply(elem2))) {
                    final K remove = elem1.remove(keyMapper.apply(elem2));
                    elem1.put(keyMapper.apply(elem2), keyResolver.apply(remove, valueMapper.apply(elem2)));
                } else {
                    elem1.put(keyMapper.apply(elem2), valueMapper.apply(elem2));
                }
                return elem1;
            }
        });
    }


    /**
     * Join Stream in a String value delimited by comma
     * @param <T>
     * @return
     */
    public static <T> Collector<String, T, String> joining() {
        return joining(",");
    }

    /**
     *
     * @param delimiter custom delimiter for joining value
     * @param <T>
     * @return Collectors in String
     */
    public static <T> Collector<String, T, String> joining(final String delimiter) {
        return joining(delimiter, new ToStringFunction<T>() {
            @Override
            public String apply(T start) {
                return start.toString();
            }
        });
    }

    /**
     *
     * @param delimiter custom delimiter for joining value
     * @param mapper ToStringFunction for converting value of Stream in String
     * @param <T>
     * @param <R>
     * @return Collector of Strings
     */
    public static <T, R> Collector<String, T, String> joining(final String delimiter,
                                                              final ToStringFunction<T> mapper) {

        return reducing("", new BiFunction<String, T, String>() {
            @Override
            public String apply(String elem1, T elem2) {
                if (elem1 != null && elem2 == null) {
                    return elem1;
                } else if (elem1 == null && elem2 != null) {
                    return mapper.apply(elem2);
                } else {
                    return elem1.concat(delimiter).concat(mapper.apply(elem2));
                }
            }
        });
    }

    /**
     *
     * @param mapper function for collecting stream of t in List of R
     * @param <U> List collected
     * @param <T> type of Stream
     * @param <R> type of list
     * @return
     */
    public static <U extends List<R>, T, R> Collector<U, T, U> mapping(final Function<T, R> mapper) {
        return new CollectorImpl<>(new Supplier<U>() {
            @Override
            public U get() {
                return (U) new ArrayList<R>();
            }
        }, new BiFunction<U, T, U>() {
            @Override
            public U apply(U elem1, T elem2) {
                elem1.add(mapper.apply(elem2));
                return elem1;
            }
        }, new UnaryOperator<U>() {
            @Override
            public U apply(U start) {
                return start;
            }
        });
    }

    /**
     *
     * @param identity instance of reducing result (accumulator)
     * @param operator bifunction for reducing (combiner)
     * @param <U> element returned
     * @param <T> type of Stream
     * @return Element in Accumulator value
     */
    public static <U, T> Collector<U, T, U> reducing(final U identity, final BiFunction<U, T, U> operator) {
        return new CollectorImpl<>(new Supplier<U>() {
            @Override
            public U get() {
                return identity;
            }
        }, operator, new UnaryOperator<U>() {
            @Override
            public U apply(U start) {
                return start;
            }
        });
    }

    /**
     *
     * @param keyMapper function for converting value in Stream in key of Map
     * @param <U> Map returned
     * @param <T> Type of Stream
     * @param <R> Key Type
     * @return Map<R->List<T>>
     */
    public static <U extends Map<R, List<T>>, T, R> Collector<U, T, U> groupingBy(final Function<T, R> keyMapper) {
        return groupingBy(keyMapper, toList(new ArrayList<T>()), (U) new HashMap<R, List<T>>());
    }


    /**
     *
     * @param keyMapper function for converting value in Stream in key of Map
     * @param downStream Collector used for collecting value in map
     * @param identity instance for accumulating result
     * @param <U> map key,list of t
     * @param <R> key type
     * @param <T> stream value type
     * @param <M> collected map
     * @param <KK> type of value in result
     * @return Map collected by downstream
     */
    public static <U extends Map<R, List<T>>, R, T, M extends Map<? super R, ? super KK>, KK> Collector<U, T, M> groupingBy(final Function<T, R> keyMapper,
                                                                                                                            final Collector<KK, T, KK> downStream,
                                                                                                                            final M identity) {
        return new CollectorImpl<>(new Supplier<U>() {
            @Override
            public U get() {
                return (U) new HashMap<R, List<T>>();
            }
        }, new BiFunction<U, T, U>() {
            @Override
            public U apply(U elem1, T elem2) {
                final R apply = keyMapper.apply(elem2);
                if (elem1.containsKey(apply)) {
                    final List<T> values = elem1.get(apply);
                    values.add(elem2);
                    elem1.remove(apply);
                    elem1.put(apply, values);
                } else {
                    final List<T> values = Stream.of(elem2).collect(toList(new ArrayList<T>()));
                    elem1.put(keyMapper.apply(elem2), values);
                }
                return elem1;
            }
        }, new Function<U, M>() {
            @Override
            public M apply(U start) {
                return (M) Stream.of(start.entrySet()).map(new Function<Map.Entry<R, List<T>>, Map.Entry<R, KK>>() {
                    @Override
                    public Map.Entry<R, KK> apply(Map.Entry<R, List<T>> start) {
                        return new AbstractMap.SimpleEntry<>(start.getKey(), Stream.of(start.getValue()).collect(downStream));
                    }
                }).collect(toMap(new Function<Map.Entry<R, KK>, R>() {
                    @Override
                    public R apply(Map.Entry<R, KK> start) {
                        return start.getKey();
                    }
                }, new Function<Map.Entry<R, KK>, KK>() {
                    @Override
                    public KK apply(Map.Entry<R, KK> start) {
                        return start.getValue();
                    }
                }));
            }
        });
    }

    /**
     * Collect or elements in stream in sum of each elements as Double Value
     * @param function function T to Double
     * @param <U> Double returned
     * @param <T> Type of Stream
     * @return Double object
     */
    public static <U extends Double, T> Collector<U, T, U> summingDouble(final ToDoubleFunction<T> function) {
        return new CollectorImpl<>(new Supplier<U>() {
            @Override
            public U get() {
                return (U) new Double(0D);
            }
        }, new BiFunction<U, T, U>() {
            @Override
            public U apply(U elem1, T elem2) {
                return (U) new Double(new Double(elem1) + function.apply(elem2));
            }
        }, new UnaryOperator<U>() {
            @Override
            public U apply(U start) {
                return start;
            }
        });
    }
    /**
     * Collect or elements in stream in sum of each elements as Integer Value
     * @param function function T to Integer
     * @param <U> Integer returned
     * @param <T> Type of Stream
     * @return Integer object
     */
    public static <U extends Integer, T> Collector<U, T, U> summingInt(final ToIntegerFunction<T> function) {
        return new CollectorImpl<>(new Supplier<U>() {
            @Override
            public U get() {
                return (U) new Integer(0);
            }
        }, new BiFunction<U, T, U>() {
            @Override
            public U apply(U elem1, T elem2) {
                return (U) new Integer(new Integer(elem1) + function.apply(elem2));
            }
        }, new UnaryOperator<U>() {
            @Override
            public U apply(U start) {
                return start;
            }
        });
    }



}
