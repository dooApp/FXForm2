package com.dooapp.fxform.utils;

import java.util.List;
import java.util.Map;

public class ObjectUtils {

    /**
     * Check whether two objects are the same, i.e. they are representing the same model object
     *
     * By default, we use the equals implementation of the given objects to compare them.
     *
     * Special cases are applied on List and Map. In there basic equals implementation List and Map are considered equals
     * if they have the same content. In our implementation we want List and Map to be considered as different if their
     * instances are different whatever their content.
     * See {@link java.util.List#equals(Object)} and {@link java.util.Map#equals(Object)} for more details.
     *
     * @param o1 the first object to compare
     * @param o2 the second object to compare
     * @return true if o1 and o2 represent the same model object, false otherwise
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
