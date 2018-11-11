package prodcons;

import swapper.Swapper;

import java.util.HashSet;

public class Swapperphore {
    private Swapper<Boolean> mutex = new Swapper<>(), delay = new Swapper<>();
    private int n;

    public Swapperphore(int n) {
        this.n = n;
        HashSet<Boolean> temp = new HashSet<>();
        temp.add(true);
        try {
            mutex.swap(new HashSet<>(), temp);
        } catch (InterruptedException e) {
            System.err.println("Swapperphore failed to construct");
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

    public void acquire() throws InterruptedException {
        mutexAcquire(mutex);
        n--;
        if (n <= -1){
            mutexRelease(mutex);
            mutexAcquire(delay);
        }
        mutexRelease(mutex);
    }

    public void release() throws InterruptedException {
        mutexAcquire(mutex);
        n++;
        if (n <= 0)
            mutexRelease(delay);
        else
            mutexRelease(mutex);
    }
}
