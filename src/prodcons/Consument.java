package prodcons;

import java.util.HashSet;

public class Consument implements Runnable {
    private HashSet<Integer> myNumbers;
    private int consumedCount = 0, toConsume;

    public Consument(HashSet<Integer> myNumbers, int toConsume) {
        this.myNumbers = myNumbers;
        this.toConsume = toConsume;
    }

    private void consume() throws InterruptedException{
        Main.swapper.swap(myNumbers, new HashSet<>());
        consumedCount++;
    }

    @Override
    public void run() {
        try {
            while (consumedCount < toConsume) {
                consume();
            }
        } catch (InterruptedException e){
            e.printStackTrace(); // TODO
        } finally {
            System.out.println("Consument " + Thread.currentThread().getName() + " stopped consuming, consumed " + consumedCount);
        }
    }
}
