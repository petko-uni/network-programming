package dev.petko.np.lab5;

public class Main {

  /** Thread A: computes sin(x). */
  static class ThreadA extends Thread {
    private final double x;
    private volatile double result;

    ThreadA(double x) {
      super("Thread-A(x=" + x + ")");
      this.x = x;
    }

    @Override
    public void run() {
      result = Math.sin(x);
      System.out.printf("[%s] sin(%.4f) = %.6f%n", getName(), x, result);
    }

    double getResult() { return result; }
  }

  /**
   * Thread B: waits for Thread A, then computes cos(y) with y = result of A.
   */
  static class ThreadB extends Thread {
    private final ThreadA a;
    private volatile double result;

    ThreadB(ThreadA a, double x) {
      super("Thread-B(x=" + x + ")");
      this.a = a;
    }

    @Override
    public void run() {
      try {
        a.join(); // wait for Thread A to finish
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        System.err.println(getName() + " interrupted while waiting for A");
        return;
      }
      double y = a.getResult();
      result = Math.cos(y);
      System.out.printf("[%s] cos(%.6f) = %.6f%n", getName(), y, result);
    }

    double getResult() { return result; }
  }

  /**
   * Thread C: waits for Thread B, then computes tan(y) with y = result of B.
   */
  static class ThreadC extends Thread {
    private final ThreadB b;
    private volatile double result;

    ThreadC(ThreadB b, double x) {
      super("Thread-C(x=" + x + ")");
      this.b = b;
    }

    @Override
    public void run() {
      try {
        b.join(); // wait for Thread B to finish
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        System.err.println(getName() + " interrupted while waiting for B");
        return;
      }
      double y = b.getResult();
      result = Math.tan(y);
      System.out.printf("[%s] tan(%.6f) = %.6f%n", getName(), y, result);
    }

    double getResult() { return result; }
  }

  public static void main(String[] args) throws InterruptedException {
    // The main thread acts as the master thread "MathThreads"
    Thread.currentThread().setName("MathThreads");
    System.out.println("[" + Thread.currentThread().getName() +
                       "] starting worker threads");

    // Different data inputs (radians) assigned to different sets of workers
    double[] inputs = {0.5, 1.0, 1.5};

    var as = new ThreadA[inputs.length];
    var bs = new ThreadB[inputs.length];
    var cs = new ThreadC[inputs.length];

    // Create and start all worker threads (they run concurrently)
    for (int i = 0; i < inputs.length; i++) {
      as[i] = new ThreadA(inputs[i]);
      bs[i] = new ThreadB(as[i], inputs[i]);
      cs[i] = new ThreadC(bs[i], inputs[i]);
      as[i].start();
      bs[i].start();
      cs[i].start();
    }

    // Master waits for every worker to complete before combining results
    for (int i = 0; i < inputs.length; i++) {
      as[i].join();
      bs[i].join();
      cs[i].join();
    }

    // Combine individual results
    System.out.println("\n[" + Thread.currentThread().getName() +
                       "] all threads finished. Results:");
    double sum = 0;
    for (int i = 0; i < inputs.length; i++) {
      double r = cs[i].getResult();
      sum += r;
      System.out.printf("x = %.2f -> tan(cos(sin(x))) = %.6f%n", inputs[i], r);
    }
    System.out.printf("Combined (sum) of results = %.6f%n", sum);
  }
}
