package interfaces.parallel;


import classes.Stream;
import interfaces.Function;

import java.util.concurrent.Callable;

public class ParallelFunction<A, B> implements Callable<Stream<B>> {

	private Stream<A> startStream;
	private Function<A, B> function;

	public ParallelFunction(Stream<A> startStream, Function<A, B> function) {
		this.startStream = startStream;
		this.function = function;
	}

	@Override
	public Stream<B> call() throws Exception {
		return startStream.map(function);
	}

}
