package com.utils.streamjava7.classes;

import com.utils.streamjava7.interfaces.BiFunction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

/**
 * Created by simon.calabrese on 15/11/2017.
 */
public class DoubleStream extends Stream<Double> {
    private DoubleStream(final Collection<Double> doubleStream) {
        super(doubleStream);
    }

    DoubleStream(final Stream<Double> stream) {
        super(stream.collect(Collectors.toList(new ArrayList<Double>())));
    }

    public Double average() {
        return new BigDecimal(reduce(0D, new BiFunction<Double, Double, Double>() {
            @Override
            public Double apply(Double elem1, Double elem2) {
                return elem1 + elem2;
            }
        }) / count().doubleValue()).doubleValue();
    }

    public Double sum() {
        return reduce(0D, new BiFunction<Double, Double, Double>() {
            @Override
            public Double apply(Double elem1, Double elem2) {
                return elem1 + Optional.of(elem2).orElse(0D);
            }
        });
    }

    public Double max() {
        return sorted(new Comparator<Double>() {
            @Override
            public int compare(Double o1, Double o2) {
                return o2.compareTo(o1);
            }
        }).findFirst().orElse(null);
    }

    public Double min() {
        return sorted(new Comparator<Double>() {
            @Override
            public int compare(Double o1, Double o2) {
                return o1.compareTo(o2);
            }
        }).findFirst().orElse(null);
    }

}
