package com.utils.streamjava7.classes;

import com.utils.streamjava7.interfaces.Function;
import com.utils.streamjava7.interfaces.Predicate;
import com.utils.streamjava7.interfaces.Supplier;
import com.utils.streamjava7.interfaces.matcher.PatternMatcher;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simon.calabrese on 22/12/2017.
 */
public class PatternMatcherImp<T, R> implements PatternMatcher<T, R> {

    private T element;
    private List<Pair<Predicate<T>, Function<T, R>>> evaluators;
    private Function<T, R> defaultCondition;

    public PatternMatcherImp(final T element) {
        this.element = element;
        this.evaluators = new ArrayList<>();
    }

    public void setDefaultCondition(Function<T, R> defaultCondition) {
        this.defaultCondition = defaultCondition;
    }

    public static <T, R> PatternMatcher<T, R> getMatcher(final T element, final Function<T, R> defaultCondition) {
        final PatternMatcher<T, R> match = new PatternMatcherImp<T, R>(element);
        match.setDefaultCondition(defaultCondition);
        return match;
    }

    @Override
    public PatternMatcher<T, R> addMatcher(final Predicate<T> condition, final Function<T, R> procedure) {
        this.evaluators.add(Pair.of(condition, procedure));
        return this;
    }

    @Override
    public R match() {
        final ArrayList<Pair<Predicate<T>, Function<T, R>>> collect = Stream.of(evaluators).filter(new Predicate<Pair<Predicate<T>, Function<T, R>>>() {
            @Override
            public Boolean test(Pair<Predicate<T>, Function<T, R>> object) {
                return object.getKey().test(element);
            }
        }).collect(Collectors.toList(new ArrayList<Pair<Predicate<T>, Function<T, R>>>()));
        return Optional.of(collect).filter(new Predicate<ArrayList<Pair<Predicate<T>, Function<T, R>>>>() {
            @Override
            public Boolean test(ArrayList<Pair<Predicate<T>, Function<T, R>>> object) {
                return object.size() == 1 || object.size() == 0;
            }
        }).mapByPattarn(new Predicate<ArrayList<Pair<Predicate<T>, Function<T, R>>>>() {
            @Override
            public Boolean test(ArrayList<Pair<Predicate<T>, Function<T, R>>> object) {
                return object.size() == 0;
            }
        }, new Function<ArrayList<Pair<Predicate<T>, Function<T, R>>>, R>() {
            @Override
            public R apply(ArrayList<Pair<Predicate<T>, Function<T, R>>> start) {
                return defaultCondition.apply(element);
            }
        }, new Function<ArrayList<Pair<Predicate<T>, Function<T, R>>>, R>() {
            @Override
            public R apply(ArrayList<Pair<Predicate<T>, Function<T, R>>> start) {
                return Stream.of(start).reduce().map(new Function<Pair<Predicate<T>, Function<T, R>>, R>() {
                    @Override
                    public R apply(Pair<Predicate<T>, Function<T, R>> start) {
                        return start.getValue().apply(element);
                    }
                }).orElse(null);
            }
        }).orElseThrow(new Supplier<RuntimeException>() {
            @Override
            public RuntimeException get() {
                return new RuntimeException("More than one matcher return true");
            }
        });
    }


}
