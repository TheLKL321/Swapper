package prodcons;

import swapper.Swapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {
        Swapperphore sem = new Swapperphore(1);
        try {
            sem.acquire();
            sem.release();
        } catch (Exception e){
            //shutup
        }
        System.out.println("done");
    }
}
