package ex2;

import java.util.Random;

class MyThread extends Thread {
  private int threadId;
  private int totalSteps;
  private Random random = new Random();

  public MyThread(int threadId, int totalSteps) {
    this.threadId = threadId;
    this.totalSteps = totalSteps;
  }

  @Override
  public void run() {
    for (int step = 1; step <= totalSteps; step++) {
      System.out.println("Thread with ID " + threadId + " is at step " + step + " out of " + totalSteps + ".");
      try {
        int sleepTime = random.nextInt(991) + 10;
        Thread.sleep(sleepTime);
      } catch (InterruptedException e) {
        System.err.println("Thread with ID " + threadId + " was interrupted.");
        break;
      }
    }
    System.out.println("Thread with ID " + threadId + " has stopped executing.");
  }

  public static void main(String[] args) {
    for (int i = 1; i <= 5; i++) {
      int steps = new Random().nextInt(31) + 20;
      MyThread thread = new MyThread(i, steps);
      thread.start();
    }
  }
}
