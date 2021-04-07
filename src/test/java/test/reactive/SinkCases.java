package test.reactive;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@RunWith(JUnit4.class)
public class SinkCases {

  @Test
  public void emitMono() {
    Sinks.One<Object> sink = Sinks.one();
    Mono<Object> mono = sink.asMono();

    mono.subscribe(Util::printThreadOnSubscribe);
    mono.subscribe(Util::printThreadOnSubscribe);
    sink.tryEmitValue("hi");
    // It wont be printed in subscribe because the sink is already closed
    sink.tryEmitValue("hello");
  }

  @Test
  public void emitFlux() {
    Sinks.Many<Object> sink = Sinks.many().multicast().onBackpressureBuffer();
    Flux<Object> flux = sink.asFlux();
    flux.subscribe(Util::printThreadOnSubscribe);
    flux.subscribe(Util::printThreadOnSubscribe);
    sink.tryEmitNext("hi");
    sink.tryEmitNext("hello");
  }

}
