package test.reactive;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(JUnit4.class)
public class MapsCases {

  @Test
  public void flatMap() {
    Flux.just(Arrays.asList(1, 2, 3, 4), Arrays.asList(5, 6, 7, 8), Arrays.asList(9, 10))
        .flatMap(x -> Flux.fromIterable(x))
        .subscribe(x -> System.out.println("Subscribed " + x));

  }

}
