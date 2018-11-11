package prodcons;

import java.util.HashMap;
import java.util.HashSet;

public class Consumer implements Runnable {
    private final int toConsume;
    private HashSet<Character> consumed = new HashSet<>();
    private HashMap<Character, Integer> counters = new HashMap<>();

    Consumer(int toConsume) {
        this.toConsume = toConsume;
    }

    private void get() throws InterruptedException {
        Main.consumerSwapperphore.acquire();
        Main.acquireProtection(Protection.CAN_CONSUME);

        char readed = Main.buffer[Main.readable]; //because "read" can be read wrong
        consumed.add(readed);
        counters.merge(readed, 1, (a, b) -> a + b);
        Main.readable = (Main.readable + 1) % 10;

        Main.releaseProtection(Protection.CAN_CONSUME);
        Main.producerSwapperphore.release();
    }

    @Override
    public void run() {
        try {
            int readCounter = 0;
            for (int i = 0; i < toConsume; i++) {
                get();
                readCounter++;
            }

            StringBuilder sb = new StringBuilder(Thread.currentThread().getName() + ": readCounter=" + readCounter + " {");
            for (Character character : consumed)
                sb.append(character).append('x').append(counters.get(character)).append(", ");
            sb.delete(sb.length() - 2, sb.length()).append("}");
            System.out.println(sb);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println(Thread.currentThread().getName() + " was interrupted");
        }
    }
}
