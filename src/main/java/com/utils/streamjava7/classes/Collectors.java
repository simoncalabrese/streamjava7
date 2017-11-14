package com.utils.streamjava7.classes;

import com.utils.streamjava7.interfaces.*;
import com.utils.streamjava7.interfaces.innerFunction.ToDoubleFunction;
import com.utils.streamjava7.interfaces.innerFunction.ToIntegerFunction;

import java.util.*;

/**
 * Created by simon.calabrese on 14/11/2017.
 */
@SuppressWarnings("unchecked")
public class Collectors {
    public static <U extends List<T>, T> Collector<U, T, U> toList(final U identity) {
        return reducing((U) new ArrayList<T>(), new BiFunction<U, T, U>() {
            @Override
            public U apply(U elem1, T elem2) {
                elem1.add(elem2);
                return elem1;
            }
        });
    }

    public static <U extends Map<R, K>, T, K, R> Collector<U, T, U> toMap(final Function<T, R> keyMapper, final Function<T, K> valueMapper) {
        return toMap(keyMapper, valueMapper, new BinaryOperator<K>() {
            @Override
            public K apply(K elem1, K elem2) {
                return elem1;
            }
        });
    }

    public static <U extends Map<R, K>, T, K, R> Collector<U, T, U> toMap(final Function<T, R> keyMapper,
                                                                          final Function<T, K> valueMapper,
                                                                          final BinaryOperator<K> keyResolver) {

        return toMap(keyMapper, valueMapper, (U) new HashMap<R, K>(), keyResolver);
    }

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


    public static <T> Collector<String, T, String> joining() {
        return joining(",");
    }

    public static <T> Collector<String, T, String> joining(final String delimiter) {
        return joining(delimiter, new Function<T, String>() {
            @Override
            public String apply(T start) {
                return start.toString();
            }
        });
    }

    public static <T, R> Collector<String, T, String> joining(final String delimiter,
                                                              final Function<T, String> mapper) {

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

    public static <U extends Map<R, List<T>>, T, R> Collector<U, T, U> groupingBy(final Function<T, R> keyMapper) {
        return groupingBy(keyMapper, toList(new ArrayList<T>()), (U) new HashMap<R, List<T>>());
    }


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
