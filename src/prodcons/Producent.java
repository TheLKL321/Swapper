package prodcons;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

public class Producent implements Runnable {
    private LinkedList<Integer> pool;

    public Producent(LinkedList<Integer> pool) {
        Collections.shuffle(pool);
        this.pool = pool;
    }

    private void produce(HashSet<Integer> products) throws InterruptedException {
        Main.swapper.swap(new HashSet<>(), products);
    }

    @Override
    public void run() {
        try {
            // TODO: multiple elements at once?
            while (!pool.isEmpty()){
                HashSet<Integer> products = new HashSet<>();
                products.add(pool.remove());
                produce(products);
            }
        } catch (InterruptedException e) {
            e.printStackTrace(); // TODO
        } finally {
            if (pool.isEmpty())
                System.out.println("Producent " + Thread.currentThread().getName() + " stopped producing, is empty");
            else
                System.out.println("Producent " + Thread.currentThread().getName() + " stopped producing, not empty");
        }
    }
}
