package net.deimon.abcd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: deimon
 * Date: 4/23/12
 * Time: 1:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class SmartLesson extends Lesson {
    private static final int LAST_LEN = 6;
    private List<Memorial> future = new ArrayList<Memorial>();
    private Memorial suggested = null;
    private Map<Memorial, Integer> gaps = new HashMap<Memorial, Integer>();

    public SmartLesson(List<Memorial> memorials) {
        super(memorials);
        for (Memorial memorial : getMemorials()) {
            gaps.put(memorial, 2);
        }
        for (int i = 0; i < 16; i++) {
            future.add(null);
        }
    }

    //	public void restart() {
//        for(Memorable memorable:memorables){
//        	memorable.clear();
//        }
//        Collections.fill(future, null);
//	}
//
    @Override
    public Memorial next() {
        Memorial last = future.remove(0);
        future.add(null);
        if (last != null) {
            int d = gaps.get(last);
            for (int i = d - 1; i < future.size(); i++) {
                if (future.get(i) == null) {
                    if (i - 1 > 0 && future.get(i - 1) == last) {
                        continue;
                    }
                    if (i + 1 < future.size() && future.get(i + 1) == last) {
                        continue;
                    }
                    future.set(i, last);
                    break;
                }
            }
        }
        if (future.get(0) == null) {
            Memorial best = null;//memorables.get((int) (Math.random()*memorables.size()))
            float sumErrorRate = 0;
            float pos = 1;
            for (int i = getMemorials().size() - 1; i >= 0; i--) {
//            for(int i=0;i<memorables.size();i++){
                Memorial memorable = getMemorials().get(i);
                if (best == null) {
                    best = memorable;
                }
                pos *= 2.5f;
                if (suggested == memorable || future.get(1) == memorable) continue;
                float errorRate;
                Status status = getStatus(memorable);
                if (status == Status.UNKNOWN) {
                    errorRate = 5f * pos;
                } else if (status == Status.MASTERED) {
                    errorRate = 1f;//*memorable.bestErrorRate()*/;
                } else {
                    errorRate = 1f;
                }
                float weight = errorRate;
                float weightedErrorRate = errorRate * weight;
                sumErrorRate += weightedErrorRate;
                if (Math.random() < weightedErrorRate / sumErrorRate) {
                    best = memorable;
                }
            }
            future.set(0, best);
        }
        return suggested = future.get(0);
    }

    @Override
    public boolean result(boolean ok) {
        if (getStatus(suggested) == Status.UNKNOWN) {
            if (ok) {
                gaps.put(suggested, 16);
            } else {
                memorialStatusMap.put(suggested, Status.LEARNING);
            }
        }
        if (ok) {
            int newGap = gaps.get(suggested) * 2;
            gaps.put(suggested, newGap);
            if (newGap >= 16) {
                memorialStatusMap.put(suggested, Status.MASTERED);
            }
        } else {
            gaps.put(suggested, 2);
        }
        for (Status status : memorialStatusMap.values()) {
            if (status != Status.MASTERED) {
                return false;
            }
        }
        return true;
    }
}
