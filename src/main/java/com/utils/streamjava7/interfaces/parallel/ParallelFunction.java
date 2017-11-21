package com.utils.streamjava7.interfaces.parallel;


import com.utils.streamjava7.classes.Stream;
import com.utils.streamjava7.interfaces.Consumer;
import com.utils.streamjava7.interfaces.Function;

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
		startStream.forEach(new Consumer<A>() {
			@Override
			public void consume(A elem) {
				System.out.println(elem);
			}
		});
		return startStream.map(function);
	}

}
