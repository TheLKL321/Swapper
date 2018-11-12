package swapper;

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
        requirements = new HashMap<>();
    }

    // Wakes up a thread that is now permitted to change the set and returns true. If no threads are permitted, returns false
    private boolean openGate(){
        for (Long id : requirements.keySet()) {
            if (set.containsAll(requirements.get(id))) {
                gates.get(id).release();
                return true;
            }
        }
        return false;
    }

    public void swap(Collection<E> removed, Collection<E> added) throws InterruptedException {
        long currentId = Thread.currentThread().getId();
        mutex.acquire();

        if (!set.containsAll(removed)) {
            Semaphore sem = new Semaphore(0);
            requirements.put(currentId, removed);
            gates.put(currentId, sem);
            mutex.release();
            try {
                sem.acquire();
            } catch (InterruptedException e){
                mutex.acquire();
                requirements.remove(currentId);
                gates.remove(currentId);
                mutex.release();
                throw new InterruptedException();
            }
            requirements.remove(currentId);
            gates.remove(currentId);
        }

        try {
            HashSet<E> temp = new HashSet<>(set);
            temp.removeAll(removed);
            temp.addAll(added);
            if (Thread.interrupted())
                throw new InterruptedException();
            set = temp;
        } finally {
            if (!openGate())
                mutex.release();
        }
    }
}