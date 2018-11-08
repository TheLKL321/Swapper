package prodcons;

import swapper.Swapper;

import java.util.HashSet;
import java.util.LinkedList;

public class Main {
    static final Swapper<Integer> swapper = new Swapper<>();

    public static void main(String[] args) {
        LinkedList<Integer> products = new LinkedList<>();
        HashSet<Thread> threads = new HashSet<>();

        HashSet<Integer> one = new HashSet<>();
        one.add(1);
        threads.add(new Thread(new Consument(one, 5), "one"));
        for (int i = 0; i < 5; i++){
            products.add(1);
        }

        HashSet<Integer> two = new HashSet<>();
        two.add(2);
        threads.add(new Thread(new Consument(two, 7), "two"));
        for (int i = 0; i < 7; i++){
            products.add(2);
        }

        HashSet<Integer> three = new HashSet<>();
        three.add(3);
        three.add(4);
        threads.add(new Thread(new Consument(three, 3), "three"));
        for (int i = 0; i < 3; i++){
            products.addAll(three);
        }

        HashSet<Integer> four = new HashSet<>();
        four.add(1);
        four.add(2);
        four.add(3);
        threads.add(new Thread(new Consument(four, 4), "four"));
        for (int i = 0; i < 4; i++){
            products.addAll(four);
        }

        threads.add(new Thread(new Producent(products), "PRODUCENT"));
        for (Thread thread : threads) {
            thread.start();
        }
    }
}
