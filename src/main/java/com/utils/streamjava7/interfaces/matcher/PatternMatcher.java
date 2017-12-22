package com.utils.streamjava7.interfaces.matcher;

import com.utils.streamjava7.interfaces.Function;
import com.utils.streamjava7.interfaces.Predicate;

/**
 * Created by simon.calabrese on 22/12/2017.
 */
public interface PatternMatcher<T,R> {

    /**
     *
     * @param defaultCondition in case all pattern fail
     */
    void setDefaultCondition(Function<T, R> defaultCondition);

    /**
     * Add pattern and result of function if pattern is true
     * @param condition
     * @param procedure
     * @return
     */
    PatternMatcher<T, R> addMatcher(Predicate<T> condition, Function<T, R> procedure);

    /**
     * Execute Pattern
     * @return result of match
     */
    R match();
}
