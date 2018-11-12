package readwrite;

import swapper.Swapper;

import java.util.HashSet;

// Semaphore implementation using only Swapper as a synchronization mechanism
class Swapperphore {
    private Swapper<Boolean> mutex = new Swapper<>(), delay = new Swapper<>();
    private int n;

    Swapperphore(int n) {
        this.n = n;
        HashSet<Boolean> temp = new HashSet<>();
        temp.add(true);
        try {
            mutex.swap(new HashSet<>(), temp);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println(Thread.currentThread().getName() + ": Swapperphore failed to construct");
        }
    }

    private void mutexAcquire(Swapper<Boolean> mutex) throws InterruptedException {
        HashSet<Boolean> temp = new HashSet<>();
        temp.add(true);
        mutex.swap(temp, new HashSet<>());
    }

    private void mutexRelease(Swapper<Boolean> mutex) throws InterruptedException {
        HashSet<Boolean> temp = new HashSet<>();
        temp.add(true);
        mutex.swap(new HashSet<>(), temp);
    }

    void acquire() throws InterruptedException {
        mutexAcquire(mutex);
        n--;
        if (n <= -1){
            mutexRelease(mutex);
            mutexAcquire(delay);
        }
        mutexRelease(mutex);
    }

    void release() throws InterruptedException {
        mutexAcquire(mutex);
        n++;
        if (n <= 0)
            mutexRelease(delay);
        else
            mutexRelease(mutex);
    }
}
