package test.reactive;

import com.github.javafaker.Faker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.atomic.AtomicInteger;

@RunWith(JUnit4.class)
public class ThreadsExamplesCases {

  @Test
  public void createUsingThreadsManually() {
    AtomicInteger atomicInteger = new AtomicInteger(0);
    Flux<Object> objectFlux = Flux.create(fluxSink -> fluxSink.next(atomicInteger.incrementAndGet()));

    Runnable runnable = () -> objectFlux.subscribe(x -> Util.printThread(x));

    for (int i = 0; i < 5; i++) {
      new Thread(runnable).start();
    }
    Util.sleep(2);
  }

  @Test
  public void createSimple() {
    Flux<Object> objectFlux = Flux.create(fluxSink -> {
      String fullName = Faker.instance().name().fullName();
      fluxSink.next(fullName);
      Util.printThreadOnNext(fullName);
    });
    //                                  .subscribeOn(Schedulers.boundedElastic());
    objectFlux.subscribe(Util::printThreadOnSubscribe);
    objectFlux.subscribe(Util::printThreadOnSubscribe);
    objectFlux.subscribe(Util::printThreadOnSubscribe);

    Util.sleep(2);
  }

  /**
   * A parte interessante disso é que quem for usar o flux e subscrever já vai ser interferido sem precisar saber que estará rodando em uma nova thread
   * A pipeline é executada na main as emissões mas as subscrições cada uma tem a sua propria pipeline
   * Main
   * ------ Emit 1
   * ------ Emit 2
   * ThreadA
   * ------  Subscribe 1
   * ThreadB
   * ------  Subscribe 2
   */
  @Test
  public void createSchedulerPublishOn() {
    Flux<Object> objectFlux = Flux.create(fluxSink -> {
      String fullName = Faker.instance().name().fullName();
      fluxSink.next(fullName);
      Util.printThreadOnNext(fullName);
    })
                                  //                                  .subscribeOn(Schedulers.boundedElastic());
                                  .publishOn(Schedulers.boundedElastic());
    objectFlux.subscribe(Util::printThreadOnSubscribe);
    objectFlux.subscribe(Util::printThreadOnSubscribe);
    objectFlux.subscribe(Util::printThreadOnSubscribe);

    Util.sleep(2);
  }

  /**
   * A parte interessante disso é que quem for usar o flux e subscrever já vai ser interferido sem precisar saber que estará rodando em uma nova thread
   * A pipeline é executada inteiramente em cada thread.
   * ThreadA
   * ------ Emit ----- Subscribe
   * ThreadB
   * ------ Emit ----- Subscribe
   */
  @Test
  public void createSchedulerSubscribeOn() {
    Flux<Object> objectFlux = Flux.create(fluxSink -> {
      String fullName = Faker.instance().name().fullName();
      fluxSink.next(fullName);
      Util.printThreadOnNext(fullName);
    })
                                  .subscribeOn(Schedulers.boundedElastic());
    //                                  .publishOn(Schedulers.boundedElastic());
    objectFlux.subscribe(Util::printThreadOnSubscribe);
    objectFlux.subscribe(Util::printThreadOnSubscribe);
    objectFlux.subscribe(Util::printThreadOnSubscribe);

    Util.sleep(2);
  }

  @Test
  public void generate() {
    Flux<Object> objectFlux = Flux.generate(AtomicInteger::new, (state, synchronousSink) -> {
      String fullName = Faker.instance().name().fullName();
      synchronousSink.next(fullName);
      Util.printThreadOnNext(fullName);
      state.incrementAndGet();
      if (state.intValue() == 4) {
        synchronousSink.complete();
      }
      return state;
    })
                                  .subscribeOn(Schedulers.boundedElastic());
    //                                  .publishOn(Schedulers.boundedElastic());
    objectFlux.subscribe(Util::printThreadOnSubscribe);
    objectFlux.subscribe(Util::printThreadOnSubscribe);
    objectFlux.subscribe(Util::printThreadOnSubscribe);

    Util.sleep(2);
  }

}
