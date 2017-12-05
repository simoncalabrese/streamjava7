package com.utils.streamjava7.classes;

import com.utils.streamjava7.interfaces.BiFunction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings("unchecked")
public class IntStream extends Stream<Integer> {

    private IntStream(final Collection<Integer> intStream) {
        super(intStream);
    }

    IntStream(final Stream<Integer> stream) {
        super(stream.collect(Collectors.toList(new ArrayList<Integer>())));
    }

    /**
     * Create an IntStream that goes from start inclusive value to end esclusive value
     *
     * @param startInclusive start value
     * @param endEsclusive   end value
     * @return Stream of integers
     */
    public static Stream<Integer> range(final Integer startInclusive, final Integer endEsclusive) {
        if (startInclusive.compareTo(endEsclusive) > 0) {
            return empty();
        } else {
            return buildIntStream(startInclusive, endEsclusive, Boolean.FALSE);
        }
    }

    /**
     * Create an IntStream that goes from start inclusive value to end inclusive value
     *
     * @param startInclusive start value
     * @param endInclusive   end value
     * @return Stream of Integers
     */
    public static Stream<Integer> rangeClosed(final Integer startInclusive, final Integer endInclusive) {
        if (startInclusive.compareTo(endInclusive) > 0) {
            return empty();
        } else {
            return buildIntStream(startInclusive, endInclusive, Boolean.TRUE);
        }
    }

    private static Stream<Integer> buildIntStream(final Integer startInclusive, final Integer endEsclusive,
                                                  final Boolean isEndInclusive) {
        final Integer size = isEndInclusive ? endEsclusive + 1 : endEsclusive;
        final List<Integer> integerList = new ArrayList<>(size);
        for (int i = startInclusive; i < size; i++) {
            integerList.add(i);
        }
        return of(integerList);
    }

    public Double average() {
        return new BigDecimal(reduce(0, new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer elem1, Integer elem2) {
                return elem1 + elem2;
            }
        }) / count().doubleValue()).doubleValue();
    }


    public Integer sum() {
        return reduce(0, new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer elem1, Integer elem2) {
                return elem1 + Optional.of(elem2).orElse(0);
            }
        });
    }

    public Integer max() {
        return sorted(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        }).findFirst().orElse(null);
    }

    public Integer min() {
        return sorted(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        }).findFirst().orElse(null);
    }
}
