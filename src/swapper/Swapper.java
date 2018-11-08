package swapper;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class Swapper<E> {
    private final Semaphore mutex = new Semaphore(1, true);
    private HashMap<Long, Collection<E>> requirements;
    private HashMap<Long, Semaphore> gates;
    private HashSet<E> set;

    public Swapper() {
        set = new HashSet<>();
        gates = new HashMap<>();
    }

    private void openGate(){

    }

    public void swap(Collection<E> removed, Collection<E> added) throws InterruptedException {
        long id = Thread.currentThread().getId();
        mutex.acquire();

        while (!set.containsAll(removed)) {
            Semaphore sem = new Semaphore(1);
            sem.acquire();
            requirements.put(id, removed);
            gates.put(id, sem);
            mutex.release();
            try {
                sem.acquire();
            } finally {
                requirements.remove(id);
                gates.remove(id);
            }
            mutex.acquire();
        }

        try {
            HashSet<E> temp = new HashSet<>(set);
            temp.removeAll(removed);
            temp.addAll(added);
            if (Thread.interrupted())
                throw new InterruptedException();
            set = temp;
        } finally {
            openGate();
            mutex.release();
        }
    }
}