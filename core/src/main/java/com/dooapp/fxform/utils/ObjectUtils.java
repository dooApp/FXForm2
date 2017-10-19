package com.dooapp.fxform.utils;

import java.util.List;
import java.util.Map;

public class ObjectUtils {

    /**
     * Determine the equality between two objects in term of "model object"
     *
     * Two objects are identical if they are equals.
     * Special cases are applied on List and Map that are saw as container, so if two different instances of list
     * contain both the same elements (or if they are both empty) it will be see as different
     *
     * @param o1 the first object to compare
     * @param o2 the second object to compare
     * @return true if o1 is equals to o2 in term of "model object", false otherwise
     */
    public static boolean areSame(Object o1, Object o2) {
        if (o1 == null && o2 != null) return false;
        if (o1 != null && o2 == null) return false;
        if (o1 == null && o2 == null) return true;
        if (o1 instanceof List && o2 instanceof List) return o1 == o2;
        if (o1 instanceof Map && o2 instanceof Map) return o1 == o2;
        return o1.equals(o2);

    }
}
