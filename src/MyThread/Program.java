package MyThread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


public class Program {
    static List<Person> personList = new ArrayList<>();
    static int COUNTER = 0;

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Program program = new Program();

//        program.doWorkWithMyThread();
//        program.doWorkWithThreadPool();
        program.doWorkWithFuture();

    }


////////////////////////MYTHREAD/////////////////////////////////

    public void doWorkWithMyThread() throws InterruptedException {
        long time = System.currentTimeMillis();
        System.out.println("List before work: " + personList.size());

        Thread thread1 = new Thread(new ThreadWork());
        Thread thread2 = new Thread(new ThreadWork());

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("List after work: " + personList.size() + " " + personList.toString());
        System.out.println("Time in work is: " + (System.currentTimeMillis() - time));
    }


    ////////////////////////EXECUTORSERVICE/////////////////////////////////
    public void doWorkWithThreadPool() {
        long time = System.currentTimeMillis();
        System.out.println("List before work: " + personList.size());
        ExecutorService executor = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 2; i++) {
            executor.submit(new ThreadWork());
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
            System.out.println("Works are finished");
        }
        System.out.println("List after work: " + personList.size() + " " + personList.toString());
        System.out.println("Time in work is: " + (System.currentTimeMillis() - time));
    }


//////////////////////FUTURE/////////////////////////////////

    public void doWorkWithFuture() throws ExecutionException, InterruptedException {
        long time = System.currentTimeMillis();
        System.out.println("List before work: " + personList.size());

        FutureTask futureTask = new FutureTask(new ThreadWorkCall());
        new Thread(futureTask).start();
        FutureTask futureTask1 = new FutureTask(new ThreadWorkCall());
        new Thread(futureTask1).start();

        futureTask.get();
        futureTask1.get();
        System.out.println("List after work: " + personList.size());
        System.out.println("Time in work is: " + (System.currentTimeMillis() - time));
    }


    static synchronized void increment() {
        for (int i = 0; i < 50; i++) {
            personList.add(new Person(COUNTER++));
        }
    }

    static class ThreadWork implements Runnable {

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " Start working");
            increment();
            System.out.println(Thread.currentThread().getName() + " Finish work");
        }
    }

    static class ThreadWorkCall implements Callable {

        @Override
        public List<Person> call() {
            System.out.println(Thread.currentThread().getName() + " Start working");
            increment();
            System.out.println(Thread.currentThread().getName() + " Finish work");
            return personList;
        }
    }
}



