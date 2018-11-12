package prodcons;

public class Producer implements Runnable {
    private final int order;
    private final char product;

    Producer(char product, int order) {
        this.product = product;
        this.order = order;
    }

    private void put(char product) {
        Main.buffer[Main.writable] = product;
        Main.writable = (Main.writable + 1) % 10;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < order; i++) {
                Main.producerSwapperphore.acquire();
                Main.acquireProtection(Protection.CAN_PRODUCE);

                put(product);

                Main.releaseProtection(Protection.CAN_PRODUCE);
                Main.consumerSwapperphore.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println(Thread.currentThread().getName() + " was interrupted");
        }
    }
}
