package prodcons;

import swapper.Swapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Main {

    // Every one of $PRODUCERS producers creates $ORDER products
    private static final int PRODUCERS = 10, ORDER = 10;
    // Every one of $CONSUMERS consumers reads $TO_CONSUME products
    private static final int CONSUMERS = 5, TO_CONSUME = (PRODUCERS * ORDER / CONSUMERS);

    static int readable = 0, writable = 0;
    static final char[] buffer = new char[10];
    static final Swapperphore consumerSwapperphore = new Swapperphore(0), producerSwapperphore = new Swapperphore(10);
    private static final Swapper<Protection> protections = new Swapper<>();

    static void acquireProtection(Protection protection) throws InterruptedException {
        HashSet<Protection> temp = new HashSet<>();
        temp.add(protection);
        protections.swap(temp, new HashSet<>());
    }

    static void releaseProtection(Protection protection) throws InterruptedException {
        HashSet<Protection> temp = new HashSet<>();
        temp.add(protection);
        protections.swap(new HashSet<>(), temp);
    }

    public static void main(String args[]) {
        try {
            releaseProtection(Protection.CAN_CONSUME);
            releaseProtection(Protection.CAN_PRODUCE);

            List<Thread> threads = new ArrayList<>();
            char[] products = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
            for (int i = 0; i < PRODUCERS; i++)
                threads.add(new Thread(new Producer(products[i % products.length], ORDER), "Producer " + (i + 1)));

            for (int i = 0; i < CONSUMERS; i++)
                threads.add(new Thread(new Consumer(TO_CONSUME), "Consumer " + (i + 1)));

            for (Thread t : threads)
                t.start();

        } catch (InterruptedException e){
            System.err.println("Main thread interrupted before any new threads were created");
        }
    }

}