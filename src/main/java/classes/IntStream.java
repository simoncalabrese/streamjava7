package classes;

import java.util.ArrayList;
import java.util.List;

public class IntStream extends Stream<Integer> {

	private IntStream(final List<Integer> intStream) {
		super(intStream);
	}

	/**
	 * 
	 * @param startInclusive
	 * @param endEsclusive
	 * @return
	 */
	public static Stream<Integer> range(final Integer startInclusive, final Integer endEsclusive) {
		if (startInclusive.compareTo(endEsclusive) > 0) {
			return Stream.empty();
		} else {
			return buildIntStream(startInclusive, endEsclusive, Boolean.FALSE);
		}
	}
	
	public static Stream<Integer> rangeClosed(final Integer startInclusive, final Integer endInclusive) {
		if (startInclusive.compareTo(endInclusive) > 0) {
			return Stream.empty();
		} else {
			return buildIntStream(startInclusive, endInclusive, Boolean.TRUE);
		}
	}

	private static Stream<Integer> buildIntStream(final Integer startInclusive, final Integer endEsclusive,
			final Boolean isEndInclusive) {
		final Integer size = isEndInclusive ? endEsclusive + 1 : endEsclusive;
		final List<Integer> integerList = new ArrayList<>(size);
		for (int i = startInclusive; i < size; i++) {
			integerList.add(i);
		}
		return of(integerList);
	}
}
