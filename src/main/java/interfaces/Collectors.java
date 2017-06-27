package interfaces;

import pipeline.Pipeline;

import java.util.Collection;

/**
 * Created by simon.calabrese on 27/06/2017.
 */
public interface Collectors<U, C extends Collection<U>> {

    C collect();
}
