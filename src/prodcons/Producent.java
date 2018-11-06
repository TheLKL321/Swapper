package prodcons;

import java.util.HashSet;

public class Producent implements Runnable {
    private HashSet<Integer> pool;

    public Producent(HashSet<Integer> pool) {
        this.pool = pool;
    }

    private void produce(HashSet<Integer> products) throws InterruptedException {
        Main.swapper.swap(new HashSet<>(), products);
    }

    @Override
    public void run() {
        try {
            // TODO: multiple elements at once?
            for (Integer integer : pool) {
                HashSet<Integer> products = new HashSet<>();
                products.add(integer);
                produce(products);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Production finished");
    }
}
