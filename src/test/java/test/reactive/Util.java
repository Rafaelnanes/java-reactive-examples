package test.reactive;

public class Util {

  public static void printThread(Object value) {
    System.out.println(String.format("%s : %s", Thread.currentThread().getName(), value));
  }

  public static void printThreadOnNext(Object value) {
    System.out.println(String.format("%s : OnNext: %s", Thread.currentThread().getName(), value));
  }

  public static void printThreadOnSubscribe(Object value) {
    System.out.println(String.format("%s : OnSub: %s", Thread.currentThread().getName(), value));
  }

  public static void printThreadOnError(Object value) {
    System.out.println(String.format("%s : OnError: %s", Thread.currentThread().getName(), value));
  }

  public static void sleep(int value) {
    try {
      Thread.sleep(value * 1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
