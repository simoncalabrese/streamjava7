package com.utils.streamjava7.collection;

import java.util.Iterator;
import java.util.List;

public interface Pipeline<T> extends Iterable<T> {
    void add(T elem);

    void addAll(Pipeline<T> that);

    T head();

    Pipeline<T> tail();

    Pipeline<T> merge(final Pipeline<T> that);

    T[] pipeline();

    Boolean isNotEmpty();

    List<T> toList();

    @Override
    Iterator<T> iterator();
}
