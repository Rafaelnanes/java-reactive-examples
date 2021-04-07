package test.reactive;

import com.github.javafaker.Faker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(JUnit4.class)
public class ProgrammaticallyCreatingSequenceCases {

  /**
   * O create faz com que toda a criação esteja dentro do método dele, com isso da suporte a async
   */
  @Test
  public void create() {
    AtomicInteger atomicInteger = new AtomicInteger(10);
    Flux.create(sink -> {
      do {
        int value = atomicInteger.decrementAndGet();
        sink.next(value);
      } while (atomicInteger.intValue() > 0);
      sink.complete();
    }).subscribe(System.out::println);
  }

  /**
   * O create faz com que toda a criação esteja dentro do método dele, com isso da suporte a async
   */
  @Test
  public void createAsync() {
    AtomicInteger atomicInteger = new AtomicInteger(10);
    Flux.create(sink -> {
      do {
        new Thread(() -> {
          System.out.println("Thread : " + Thread.currentThread().getName());
          int value = atomicInteger.decrementAndGet();
          sink.next(value);
        }).start();
      } while (atomicInteger.intValue() > 0);
      sink.complete();
    }).subscribe(System.out::println);
  }


  /**
   * O Generate cria um fluxo e executa ele um por um até que o sink.complete e sempre será síncrono
   * synchronousSink é porque a execução tem que ser feita cada vez na chamada dentro do generate, por isso não posso chamar 2x o next dentro do generate
   */
  @Test
  public void generate() {
    AtomicInteger atomicInteger = new AtomicInteger(10);
    Flux.generate(synchronousSink -> {
      int value = atomicInteger.decrementAndGet();
      synchronousSink.next(value);
      if (atomicInteger.intValue() == 0) {
        synchronousSink.complete();
      }
    }).subscribe(System.out::println);
  }

  @Test
  public void generateUsingTake() {
    AtomicBoolean atomicBoolean = new AtomicBoolean(Boolean.TRUE);
    Flux<Object> flux = Flux.generate(synchronousSink -> synchronousSink.next(Faker.instance().name().fullName()))
                            .take(3)
                            .doOnComplete(() -> atomicBoolean.set(Boolean.FALSE));
    flux
        .subscribe(Util::printThreadOnSubscribe);
    flux
        .subscribe(Util::printThreadOnSubscribe);

    do {

    } while (atomicBoolean.get());
  }

}
