package classes;

import mapOrReduce.interfaces.Optional;
import mapOrReduce.interfaces.Predicate;

public class ConversionUtils {

	public static <T> T optionalOfFiltered(final T value, final Predicate<T> pattern, final T orElse) {
		return Optional.of(value).filter(pattern).orElse(orElse);
	}
}
