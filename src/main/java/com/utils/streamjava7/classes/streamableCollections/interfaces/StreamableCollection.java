package com.utils.streamjava7.classes.streamableCollections.interfaces;

import com.utils.streamjava7.classes.Stream;

import java.util.Collection;

public abstract class StreamableCollection<T> {
    protected Stream<T> stream;
    public StreamableCollection(Collection<T> coll) {
        this.stream = Stream.of(coll);
    }


}
