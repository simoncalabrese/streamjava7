package com.utils.streamjava7.classes;

import com.utils.streamjava7.interfaces.BiFunction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

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

}
