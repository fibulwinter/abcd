package net.deimon.abcd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: deimon
 * Date: 4/22/12
 * Time: 9:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class LessonFactory {
    private Aspect[] aspects;
    private List<Memorial> memorials = new ArrayList<Memorial>();
    private boolean shuffle;
    private boolean shuffled;

    public LessonFactory(Aspect... aspects) {
        this.aspects = aspects;
    }

    public void add(String name, Object... objects) {
        Memorial memorial = new Memorial(name);
        for (int i = 0; i < objects.length; i++) {
            memorial.put(aspects[i], objects[i]);
        }
        memorials.add(memorial);
    }

    public List<Memorial> getMemorials() {
        if (shuffle && !shuffled) {
            Collections.shuffle(memorials);
            shuffled = true;
        }
        return memorials;
    }

    public void setShuffle(boolean shuffle) {
        this.shuffle = shuffle;
    }

    private static class M {
        int i;
        int j;

        private M(int i, int j) {
            this.i = i;
            this.j = j;
        }

        int level() {
            if (isEasy(i) || isEasy(j)) {
                return 1;
            } else if (isMedium(i) || isMedium(j)) {
                return 2;
            }
            return 3;
        }

        boolean isEasy(int n) {
            return n == 1 || n == 10;
        }

        boolean isMedium(int n) {
            return n == 5 || n == 2 || n == 9;
        }

        @Override
        public String toString() {
            return "        <Memorial>" +
                    (i + "*" + j) +
                    "," +
                    (i * j) + "," +
                    (i * j) +
                    "</Memorial>";
        }
    }

    public static void main(String[] args) {
        ArrayList<M> list = new ArrayList<M>();
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                list.add(new M(i, j));
            }
        }
        Collections.shuffle(list);
        System.out.println(1);
        print(count(list, 1));
        System.out.println(2);
        print(count(list, 2));
        System.out.println(3);
        print(count(list, 3));
//        print(list);
    }

    private static void print(List<M> list) {
        for (M m : list) {
            System.out.println(m);
        }
    }

    private static List<M> count(ArrayList<M> list, int i) {
        ArrayList<M> res = new ArrayList<M>();
        for (M m : list) {
            if (m.level() == i) {
                res.add(m);
            }
        }
        return res;
    }
}
