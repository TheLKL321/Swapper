package prodcons;

public class Producer implements Runnable {
    private final int order;
    private final char product;

    Producer(char product, int order) {
        this.product = product;
        this.order = order;
    }

    private void put(char product) throws InterruptedException {
        Main.producerSwapperphore.acquire();
        Main.acquireProtection(Protection.CAN_PRODUCE);

        Main.buffer[Main.writable] = product;
        Main.writable = (Main.writable + 1) % 10;

        Main.releaseProtection(Protection.CAN_PRODUCE);
        Main.consumerSwapperphore.release();
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < order; i++)
                put(product);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println(Thread.currentThread().getName() + " was interrupted");
        }
    }
}
