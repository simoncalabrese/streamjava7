package com.utils.streamjava7.classes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IntStream extends Stream<Integer> {

    private IntStream(final Collection<Integer> intStream) {
        super(intStream);
    }

    IntStream(final Stream<Integer> stream) {
        super(stream.collect(Collectors.toList(new ArrayList<Integer>())));
    }

    /**
     * Create an IntStream that goes from start inclusive value to end esclusive value
     * @param startInclusive start value
     * @param endEsclusive end value
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
     * @param startInclusive start value
     * @param endInclusive end value
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
}
