package classes;

import mapOrReduce.interfaces.Function;

public abstract class AbstractFunction<A, B> implements Function<A, B> {

	public <C> Function<A,C> andThen(final Function<B,C> function) {
		return new Function<A, C>() {
			@Override
			public C apply(A start) {
				return this.apply(start);
			}
		};
	}
}
