package com.utils.streamjava7.collection;

import java.util.*;

public class PipelineImpl<T> implements Pipeline<T> {
    private T[] pipeline;
    private Integer lastIndexFill;

    public PipelineImpl(final Collection<T> coll) {
        if (coll != null) {
            this.pipeline = (T[]) coll.toArray();
            //means that is full
            this.lastIndexFill = this.pipeline.length;
        } else {
            this.pipeline = (T[]) new Object[0];
            //means that is full
            this.lastIndexFill = 0;
        }
    }

    public PipelineImpl(final Long size) {
        this.pipeline = (T[]) new Object[size.intValue()];
        this.lastIndexFill = 0;
    }

    private PipelineImpl(T[] pipeline) {
        this.pipeline = pipeline;
        this.lastIndexFill = pipeline.length;
    }

    public Boolean isNotEmpty() {
        return pipeline != null && pipeline.length > 0;
    }

    private Boolean isFull() {
        return lastIndexFill == pipeline.length;
    }

    @Override
    public void add(final T elem) {
        if (isFull()) {
            final PipelineImpl<T> ts = new PipelineImpl<>(Collections.singletonList(elem));
            this.pipeline = merge(ts).pipeline();
        } else {
            this.pipeline[lastIndexFill] = elem;
            this.lastIndexFill++;
        }
    }

    @Override
    public T head() {
        if (pipeline != null && pipeline.length > 0)
            return pipeline[0];
        else return null;
    }

    @Override
    public Pipeline<T> tail() {
        if (isNotEmpty())
            return new PipelineImpl<>(Arrays.copyOfRange(pipeline, 1, pipeline.length));
        else return new PipelineImpl<>(new ArrayList<T>());
    }

    @Override
    public Pipeline<T> merge(final Pipeline<T> that) {
        final List<T> newL = new ArrayList<>();
        for (T t : this) {
            newL.add(t);
        }
        for (T t : that) {
            newL.add(t);
        }
        return new PipelineImpl<>(newL);
    }

    @Override
    public void addAll(final Pipeline<T> that) {
        this.pipeline = merge(that).pipeline();
    }

    @Override
    public T[] pipeline() {
        return this.pipeline;
    }

    @Override
    public List<T> toList() {
        return Arrays.asList(pipeline);
    }

    @Override
    public void replace(Pipeline<T> pipeline) {
        this.pipeline = pipeline.pipeline();
    }

    @Override
    public Iterator<T> iterator() {
        return new SequenceIterator(this);
    }

    private class SequenceIterator implements Iterator<T> {

        private Pipeline<T> pipeline;

        public SequenceIterator(Pipeline<T> pipeline) {
            this.pipeline = pipeline;
        }

        @Override
        public boolean hasNext() {
            return pipeline.head() != null;
        }

        @Override
        public T next() {
            final T toRet = pipeline.head();
            pipeline = pipeline.tail();
            return toRet;
        }

        @Override
        public void remove() {

        }
    }
}
