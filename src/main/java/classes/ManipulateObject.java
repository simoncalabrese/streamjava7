package classes;

import mapOrReduce.interfaces.Function;
import mapOrReduce.interfaces.Optional;
import mapOrReduce.interfaces.Predicate;

public class ManipulateObject<T> extends Optional<T> {

	private T object;

	private ManipulateObject(final T object) {
		super(object);
		this.object = object;
	}

	public static <T> ManipulateObject<T> of(final T object) {
		return new ManipulateObject<T>(object);
	}

	public <U> U mapByPattern(final Predicate<T> patternTrue, Function<T, U> mapTrue,
			Function<T, U> mapFalse) {
		if(isPresent()) {
			if(patternTrue.test(this.object)) {
				return map(mapTrue).orElse(null);
			} else {
				return map(mapFalse).orElse(null);
			}
		}
		return null;
	}

}
