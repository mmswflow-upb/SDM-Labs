package ex2;

import java.util.Random;

class MyThread extends Thread {
  private int threadId;
  private int totalSteps;
  private Random random = new Random();

  // Constructor that sets the unique thread ID and the total number of steps.
  public MyThread(int threadId, int totalSteps) {
    this.threadId = threadId;
    this.totalSteps = totalSteps;
  }

  @Override
  public void run() {
    // Iterate over all steps
    for (int step = 1; step <= totalSteps; step++) {
      // Print current state
      System.out.println("Thread with ID " + threadId + " is at step " + step + " out of " + totalSteps + ".");
      // Sleep for a random duration between 10 and 1000 milliseconds
      try {
        int sleepTime = random.nextInt(991) + 10; // Generates a number between 10 and 1000
        Thread.sleep(sleepTime);
      } catch (InterruptedException e) {
        System.err.println("Thread with ID " + threadId + " was interrupted.");
        // Optionally, handle interruption here
        break;
      }
    }
    System.out.println("Thread with ID " + threadId + " has stopped executing.");
  }

  // Main method to initialize and start 5 threads
  public static void main(String[] args) {
    // Create and start 5 threads with unique IDs and varying numbers of steps
    for (int i = 1; i <= 5; i++) {
      // For example, assign each thread a random number of steps between 20 and 50
      int steps = new Random().nextInt(31) + 20; // (0 to 30) + 20 => 20 to 50 steps
      MyThread thread = new MyThread(i, steps);
      thread.start();
    }
  }
}
