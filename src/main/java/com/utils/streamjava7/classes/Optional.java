package com.utils.streamjava7.classes;

import com.utils.streamjava7.interfaces.Function;
import com.utils.streamjava7.interfaces.Predicate;
import com.utils.streamjava7.interfaces.Supplier;

/**
 * Created by simon.calabrese on 26/06/2017.
 */
public class Optional<T> {

    private T elem;

    protected Optional(T elem) {
        this.elem = elem;
    }

    public static <T> Optional<T> of(T elem) {
        return new Optional<>(elem);
    }

    public static <T> Optional<T> empty() {
        return Optional.of(null);
    }

    public Boolean isPresent() {
        return elem != null;
    }

    public T get() {
        return elem;
    }

    /**
     * @param function function for converting element in optional from T to U only if exists
     * @param <U>      type to return
     * @return Optional of U if value exists otherwise Optional.empty()
     */
    public <U> Optional<U> map(Function<T, U> function) {
        if (isPresent()) {
            return of(function.apply(elem));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Return value in Optional if exists otherwise default value
     *
     * @param defaultElem default same type value of optional
     * @return element in Optional or default value
     */
    public T orElse(T defaultElem) {
        if (isPresent()) {
            return get();
        } else {
            return defaultElem;
        }
    }

    /**
     * Return value in Optional if exists otherwise get of Supplier
     *
     * @param orElse Supplier of defaultValue
     * @return
     */
    public T orElseGet(Supplier<? extends T> orElse) {
        if (isPresent()) {
            return get();
        } else {
            return orElse.get();
        }
    }

    /**
     * Return element in optional otherwise element mapper by function
     *
     * @param function mapper for elements
     * @param <U>
     * @return
     */
    public <U> Object orElseMap(Function<T, U> function) {
        if (isPresent()) {
            return elem;
        } else {
            return function.apply(elem);
        }
    }

    /**
     * check if optional value match to pattern otherwise maps element to empty
     *
     * @param pattern
     * @return
     */
    public Optional<T> filter(final Predicate<T> pattern) {
        if (isPresent() && pattern.test(get())) {
            return this;
        } else {
            return Optional.empty();
        }
    }

    /**
     * Return value if exists otherwise throw exception in Supplier
     *
     * @param exceptionThrown exception to throw if optional is empty
     * @param <E>             exception
     * @return value in Optional
     * @throws E exception type
     */
    public <E extends Exception> T orElseThrow(final Supplier<E> exceptionThrown) throws E {
        if (isPresent()) {
            return get();
        } else {
            throw exceptionThrown.get();
        }
    }

    public <U> Optional<U> mapByPattarn(final Predicate<T> pattern, final Function<T, U> caseTrue, final Function<T, U> caseFalse) {
        if(isPresent()){
            return Optional.of(pattern.test(this.get()) ? caseTrue.apply(this.get()) : caseFalse.apply(this.get()));
        } else {
            return Optional.empty();
        }
    }
}
