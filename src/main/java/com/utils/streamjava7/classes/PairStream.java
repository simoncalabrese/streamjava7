package com.utils.streamjava7.classes;

import com.utils.streamjava7.collection.Pipeline;
import com.utils.streamjava7.interfaces.BiFunction;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PairStream<K, V> extends Stream<Pair<K, V>> {

    public PairStream(Collection<Pair<K, V>> coll) {
        super(coll);
    }

    public PairStream(Pipeline<Pair<K, V>> pipeline) {
        super(pipeline);
    }


    public Pair<List<K>, List<V>> unzip() {
        return reduce(Pair.of((List<K>) new ArrayList<K>(), (List<V>) new ArrayList<V>()), new BiFunction<Pair<List<K>, List<V>>, Pair<K, V>, Pair<List<K>, List<V>>>() {
            @Override
            public Pair<List<K>, List<V>> apply(Pair<List<K>, List<V>> elem1, Pair<K, V> elem2) {
                elem1.getKey().add(elem2.getKey());
                elem1.getValue().add(elem2.getValue());
                return elem1;
            }
        });
    }
}
