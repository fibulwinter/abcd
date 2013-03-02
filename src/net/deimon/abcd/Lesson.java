package net.deimon.abcd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: deimon
 * Date: 4/23/12
 * Time: 12:11 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Lesson {
    public enum Status {
        UNKNOWN,
        LEARNING,
        MASTERED
    }

    private List<Memorial> memorials;
    protected Map<Memorial, Status> memorialStatusMap;

    public Lesson(List<Memorial> memorials) {
        this.memorials = memorials;
        memorialStatusMap = new HashMap<Memorial, Status>();
        for (Memorial memorial : memorials) {
            memorialStatusMap.put(memorial, Status.UNKNOWN);
        }
    }

    public List<Memorial> getMemorials() {
        return memorials;
    }

    public Memorial randomMemorial() {
        return memorials.get((int) (Math.random() * memorials.size()));
    }

    public abstract Memorial next();

    /**
     * Feedback on memorial that was returned by last {@link net.deimon.abcd.Lesson#getMemorials()}
     *
     * @param ok if memorial was answered correctly
     * @return true if lesson is over
     */
    public abstract boolean result(boolean ok);

    public Status getStatus(Memorial memorial) {
        return memorialStatusMap.get(memorial);
    }

}
