package readwrite;

import swapper.Swapper;

import java.util.HashSet;

public class Main {
    // Every one of $WRITERS writers fills the book with their id once
    private static final int WRITERS = 5;
    // Every one of $READERS readers reads the book $TIMES_READ times
    private static final int READERS = 10, TIMES_READ = 3;

    static final int[] book = new int[20];

    static int readerCount = 0, writerCount = 0, readersWaiting = 0, writersWaiting = 0;
    private static final Swapper<Boolean> mutex = new Swapper<>();
    static final Swapperphore readerSwapperphore = new Swapperphore(0), writerSwapperphore = new Swapperphore(0);

    static void acquireMutex() throws InterruptedException {
        HashSet<Boolean> temp = new HashSet<>();
        temp.add(true);
        mutex.swap(temp, new HashSet<>());
    }

    static void releaseMutex() throws InterruptedException {
        HashSet<Boolean> temp = new HashSet<>();
        temp.add(true);
        mutex.swap(new HashSet<>(), temp);
    }

    public static void main(String[] args) {
        try {
            releaseMutex();

            for (int i = 0; i < 20; i++)
                book[i] = -1;

            HashSet<Thread> threads = new HashSet<>();

            for (int i = 0; i < WRITERS; i++)
                threads.add(new Thread(new Writer(i), "Writer " + i));

            for (int i = 0; i < READERS; i++)
                threads.add(new Thread(new Reader(TIMES_READ), "Reader " + i));

            for (Thread thread : threads)
                thread.start();

        } catch (InterruptedException e) {
            System.err.println("Main thread was interrupted before any new threads were created");
        }
    }
}
