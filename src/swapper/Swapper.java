package swapper;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Swapper<E> {
    private final ReentrantLock lock = new ReentrantLock(true);
    private final Condition setModified = lock.newCondition();
    private HashSet<E> set;

    public Swapper() {
        set = new HashSet<>();
    }

    public void swap(Collection<E> removed, Collection<E> added) throws InterruptedException {
        lock.lock();

        while (!set.containsAll(removed)){
            setModified.await();
        }

        try {
            set.removeAll(removed);
            set.addAll(added);
        } finally {
            setModified.signalAll();
            lock.unlock();
        }
    }
}