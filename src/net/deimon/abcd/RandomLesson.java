package net.deimon.abcd;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: deimon
 * Date: 4/23/12
 * Time: 12:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class RandomLesson extends Lesson {
    private int correctAnswersCount = 0;

    public RandomLesson(List<Memorial> memorials) {
        super(memorials);
    }

    @Override
    public Memorial next() {
        return randomMemorial();
    }

    @Override
    public boolean result(boolean ok) {
        if (ok) {
            correctAnswersCount++;
            memorialStatusMap.put(randomMemorial(), Status.MASTERED);
        }
        return correctAnswersCount > getMemorials().size() * 2;
    }
}
