package com.utils.streamjava7.interfaces;

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

	public <U> Optional<U> map(Function<T, U> function) {
		if (isPresent()) {
			return of(function.apply(elem));
		} else {
			return Optional.empty();
		}
	}

	public T orElse(T defaultElem) {
		if (isPresent()) {
			return get();
		} else {
			return defaultElem;
		}
	}

	public T orElseGet(Supplier<? extends T> orElse) {
		if (isPresent()) {
			return get();
		} else {
			return orElse.get();
		}
	}

	public <U> Object orElseMap(Function<T, U> function) {
		if (isPresent()) {
			return elem;
		} else {
			return function.apply(elem);
		}
	}

	public Optional<T> filter(final Predicate<T> pattern) {
		if (isPresent() && pattern.test(get())) {
			return this;
		} else {
			return Optional.empty();
		}
	}
}
