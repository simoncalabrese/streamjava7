package classes;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by simon.calabrese on 27/06/2017.
 */
public class StramUtils<T> {

    @SuppressWarnings("unchecked")
    protected  <U> Collection<U> emptyCollection(final Collection<T> collection) {
        try{
            if(List.class.isAssignableFrom(collection.getClass())) {
                return (List<U>)collection.getClass().newInstance();
            } else if(Set.class.isAssignableFrom(collection.getClass())) {
                return (Set<U>)collection.getClass().newInstance();
            } else {
                return null;
            }
        } catch (IllegalAccessException | InstantiationException e) {
            return null;
        }
    }

    protected List<T> toList() {

    }
}
