package readwrite;

import java.util.Arrays;

public class Reader implements Runnable {
    private final int toRead;

    Reader(int toRead) {
        this.toRead = toRead;
    }

    private void read(int iteration){
        boolean result = true;
        for (int i = 0; i < 19; i++)
            result &= Main.book[i] == Main.book[i + 1];
        System.out.println(Thread.currentThread().getName() + ": " + Arrays.toString(Main.book) +
                " Iteration: " + iteration + " Book uncorrupted: " + result);
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < toRead; i++) {
                Main.acquireMutex();
                if (Main.writerCount + Main.writersWaiting > 0) {
                    Main.readersWaiting++;
                    Main.releaseMutex();
                    Main.readerSwapperphore.acquire();
                    Main.readersWaiting--;
                }
                Main.readerCount++;
                if (Main.readersWaiting > 0)
                    Main.readerSwapperphore.release();
                else
                    Main.releaseMutex();

                read(i + 1);

                Main.acquireMutex();
                Main.readerCount--;
                if (Main.readerCount == 0 && Main.writersWaiting > 0)
                    Main.writerSwapperphore.release();
                else
                    Main.releaseMutex();
            }
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
            System.err.println(Thread.currentThread().getName() + " was interrupted");
        }

    }
}
