package com.jm.simpletron;


// class Task implements Runnable {
//     public Task() {

//     }

//     @Override
//     public void run() {

//     }
// }

// class Worker extends Thread {
//     private volatile boolean isRunning = true;
//     private BlockingQueue<Runnable> taskQueue;

//     public Worker(BlockingQueue<Runnable> queue, String name) {
//         super(name);
//         this.taskQueue = queue;
//     }

//     @Override
//     public void run() {
//         try {
//             while (isRunning) {
//                 Runnable task = taskQueue.take();

//                 if (task == Processor.POISON_PILL) {
//                     break;
//                 }
//                 task.run();
//             }
//         } catch (InterruptedException e) {
//             System.err.println("Thread interrupted!: " + this.getName());
//             Thread.currentThread().interrupt();
//         }
//     }
// }

// public class Processor{
//     final int DEFAULT_THREAD_COUNT = 2;

//     static final Runnable POISON_PILL = () -> {};
//     private Thread[] threadPool;
//     private BlockingQueue<Runnable> taskQueue;
    
//     public Processor() {
//         threadPool = new Worker[DEFAULT_THREAD_COUNT];
//         taskQueue = new LinkedBlockingQueue<Runnable>();

//         for (int i = 0; i < DEFAULT_THREAD_COUNT; i++) {
//             threadPool[i] = new Worker(taskQueue, "Worker: " + (i + 1));
//             threadPool[i].start();
//         }
//     }

//     public Processor(int threads) {
//         threadPool = new Worker[threads];
//         taskQueue = new LinkedBlockingQueue<Runnable>();

//         for (int i = 0; i < threads; i++) {
//             threadPool[i] = new Worker(taskQueue, "Worker" + (i + 1));
//             threadPool[i].start();
//         }
//     }

//     public void submit(Runnable task) {
//         taskQueue.offer(task);
//     }

//     public void shutdown() {
//         for (int i = 0; i < threadPool.length; i++) {
//             taskQueue.offer(POISON_PILL);
//         }
//     }
// }


public class MultiProcessor {
    
}
