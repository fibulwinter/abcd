package net.deimon.abcd;

import java.util.EnumMap;
import java.util.Map;

/**
 * User: deimon
 * Date: 4/22/12
 * Time: 4:52 PM
 */
public class Memorial {
    private Map<Aspect, Object> aspectData = new EnumMap<Aspect, Object>(Aspect.class);
    private String name;

    public Memorial(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void put(Aspect aspect, Object value) {
        aspectData.put(aspect, value);
    }

    public Object get(Aspect aspect) {
        return aspectData.get(aspect);
    }

    public Object getGroup() {
        Object group = aspectData.get(Aspect.GROUP);
        if (group == null) {
            group = name;
        }
        return group;
    }
}
