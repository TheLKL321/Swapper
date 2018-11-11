package prodcons;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

public class Producent implements Runnable {
    private int number;

    public Producent(int number) {
        this.number = number;
    }

    @Override
    public void run() {
        HashSet<Protection> protections = new HashSet<>();
        protections.add(Protection.CAN_PRODUCE);
        protections.add(Protection.CAN_CONSUME);
    }
}
