package com.utils.streamjava7.classes.streamableCollections.classes;

import com.utils.streamjava7.classes.Collectors;
import com.utils.streamjava7.classes.Stream;
import com.utils.streamjava7.interfaces.Predicate;
import com.utils.streamjava7.classes.streamableCollections.interfaces.StreamableList;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class StreamableSequence<T> extends StreamableList<T> {
    public StreamableSequence(Collection<T> coll) {
        super(coll);
    }

    public int size() {
        return stream.count().intValue();
    }

    public boolean isEmpty() {
        return super.collect(Collectors.toList(new ArrayList<T>())).isEmpty();
    }

    public boolean contains(final Object o) {
        return stream.anyMatch(new Predicate<T>() {
            public Boolean test(T object) {
                return object.equals(o);
            }
        });
    }

    public Iterator<T> iterator() {
        return super.collect(Collectors.toList(new ArrayList<T>())).iterator();
    }

    public Object[] toArray() {
        return super.collect(Collectors.toList(new ArrayList<T>())).toArray();
    }

    public <T1> T1[] toArray(T1[] a) {
        return super.collect(Collectors.toList(new ArrayList<T>())).toArray(a);
    }

    public boolean add(T t) {
        final ArrayList<T> collect = super.collect(Collectors.toList(new ArrayList<T>()));
        final boolean add = collect.add(t);
        replacePipeline(collect);
        return add;
    }

    public boolean remove(final Object o) {
        final Pair<List<T>, List<T>> span = stream.span(new Predicate<T>() {
            public Boolean test(T object) {
                return object.equals(o);
            }
        });
        final boolean remove = span.getValue().remove(o);
        span.getKey().addAll(span.getValue());
        replacePipeline(span.getKey());
        return remove;
    }

    public boolean containsAll(Collection<?> c) {
        return Stream.of((Collection<T>) c).allMatch(new Predicate<T>() {
            @Override
            public Boolean test(final T that) {
                return stream.anyMatch(new Predicate<T>() {
                    @Override
                    public Boolean test(T object) {
                        return object.equals(that);
                    }
                });
            }
        });
    }

    public boolean addAll(Collection<? extends T> c) {
        final ArrayList<T> collect = stream.collect(Collectors.toList(new ArrayList<T>()));
        final boolean b = collect.addAll(c);
        replacePipeline(collect);
        return b;
    }

    public boolean addAll(int index, Collection<? extends T> c) {
        final ArrayList<T> collect =stream.collect(Collectors.toList(new ArrayList<T>()));
        final boolean b = collect.addAll(index, c);
        replacePipeline(collect);
        return b;
    }

    public boolean removeAll(Collection<?> c) {
        return stream.collect(Collectors.toList(new ArrayList<T>())).removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return stream.collect(Collectors.toList(new ArrayList<T>())).retainAll(c);
    }

    public void clear() {
        replacePipeline(new ArrayList<T>());
    }

    public T get(int index) {
        return stream.collect(Collectors.toList(new ArrayList<T>())).get(index);
    }

    public T set(int index, T element) {
        final ArrayList<T> collect =stream.collect(Collectors.toList(new ArrayList<T>()));
        final T set = collect.set(index, element);
        replacePipeline(collect);
        return set;
    }

    public void add(int index, T element) {
        final ArrayList<T> collect =stream.collect(Collectors.toList(new ArrayList<T>()));
        collect.add(index,element);
        replacePipeline(collect);
    }

    public T remove(int index) {
        final ArrayList<T> collect =stream.collect(Collectors.toList(new ArrayList<T>()));
        final T remove = collect.remove(index);
        replacePipeline(collect);
        return remove;
    }

    public int indexOf(Object o) {
        return stream.collect(Collectors.toList(new ArrayList<T>())).indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return stream.collect(Collectors.toList(new ArrayList<T>())).lastIndexOf(o);
    }

    public ListIterator<T> listIterator() {
        return stream.collect(Collectors.toList(new ArrayList<T>())).listIterator();
    }

    public ListIterator<T> listIterator(int index) {
        return stream.collect(Collectors.toList(new ArrayList<T>())).listIterator(index);
    }

    public List<T> subList(int fromIndex, int toIndex) {
        return stream.collect(Collectors.toList(new ArrayList<T>())).subList(fromIndex,toIndex);
    }
}
