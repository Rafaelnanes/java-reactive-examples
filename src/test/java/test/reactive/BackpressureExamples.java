package test.reactive;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@RunWith(JUnit4.class)
public class BackpressureExamples {

  /**
   * It emits all the numbers (until 100) and keeps in buffer than send to the subscriber
   */
  @Test
  public void createUsingBuffer() {
    AtomicBoolean atomicBoolean = new AtomicBoolean(Boolean.TRUE);
    Flux.create(getFluxSinkConsumer(), FluxSink.OverflowStrategy.BUFFER)
        .delayElements(Duration.ofMillis(20))
        .doOnComplete(() -> atomicBoolean.set(false))
        .subscribe(Util::printThreadOnSubscribe);

    do {

    } while (atomicBoolean.get());

  }

  /**
   * It stops to emit itens and drop all of the rest
   */
  @Test
  public void createUsingDrop() {
    AtomicBoolean atomicBoolean = new AtomicBoolean(Boolean.TRUE);
    Flux.create(getFluxSinkConsumer(), FluxSink.OverflowStrategy.DROP)
        .delayElements(Duration.ofMillis(20))
        .doOnDiscard(Integer.class, x -> System.out.println("On discard " + x))
        .doOnComplete(() -> atomicBoolean.set(false))
        .subscribe(x -> {
          Util.printThreadOnSubscribe(x);
        });

    do {

    } while (atomicBoolean.get());

  }

  /**
   * It throws error when overflow
   */
  @Test
  public void createUsingError() {
    AtomicInteger atomicInteger = new AtomicInteger(100);
    Flux.create(getFluxSinkConsumer(), FluxSink.OverflowStrategy.ERROR)
        .delayElements(Duration.ofMillis(20))
        .doOnError(Util::printThreadOnError)
        .subscribe(x -> {
          Util.printThreadOnSubscribe(x);
          atomicInteger.decrementAndGet();
        });

  }


  private Consumer<FluxSink<Object>> getFluxSinkConsumer() {
    return fluxSink -> {
      for (int i = 0; i < 100; i++) {
        fluxSink.next(i);
      }
      fluxSink.complete();
    };
  }

}
