package com.utils.streamjava7.classes;

import java.util.*;

/**
 * Created by simon.calabrese on 13/11/2017.
 */
class Pipeline<T> {
    private Collection<T> coll;
    private Class<? extends Collection> collectionType;

    @SuppressWarnings("unchecked")
    Pipeline(final Collection<T> coll, final Class<? extends Collection> collectionType) {
        this.coll = coll;
        this.collectionType = collectionType;
    }

    Collection<T> getColl() {
        return coll;
    }

    Class<? extends Collection> getCollectionType() {
        return collectionType;
    }

    @SuppressWarnings("unchecked")
    <C extends Collection> C getNewInstance() {
        try {
            if(AbstractSet.class.isAssignableFrom(collectionType)) {
                return (C) this.toSet();
            }
            return (C) collectionType.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
    }

    List<T> toList() {
        return new ArrayList<T>(this.coll);
    }

    Set<T> toSet() {
        return new HashSet<T>(this.coll);
    }


}
