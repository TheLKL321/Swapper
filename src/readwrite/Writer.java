package readwrite;

public class Writer implements Runnable {
    private final int id;

    public Writer(int id) {
        this.id = id;
    }

    private void write(){
        for (int i = 0; i < 20; i++)
            Main.book[i] = id;
    }

    @Override
    public void run() {
        try {
            Main.acquireMutex();
            if (Main.writerCount + Main.readerCount > 0) {
                Main.writersWaiting++;
                Main.releaseMutex();
                Main.writerSwapperphore.acquire();
                Main.writersWaiting--;
            }
            Main.writerCount++;
            Main.releaseMutex();
            write();
            Main.acquireMutex();
            Main.writerCount--;
            if (Main.readersWaiting > 0)
                Main.readerSwapperphore.release();
            else if (Main.writersWaiting > 0)
                Main.writerSwapperphore.release();
            else
                Main.releaseMutex();
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
            System.err.println(Thread.currentThread().getName() + " was interrupted");
        }
    }
}
