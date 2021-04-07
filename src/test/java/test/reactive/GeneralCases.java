package test.reactive;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RunWith(JUnit4.class)
public class GeneralCases {

  @Test
  public void buffer() {
    Flux.range(1, 20)
        .delayElements(Duration.ofMillis(30))
        .buffer(5)
        .subscribe(System.out::println);
    Util.sleep(3);
  }

}
