package com.dooapp.fxform.utils;

import java.util.List;

public class ObjectUtils {

    /**
     * Determine the equality between two objects in term of "model object"
     *
     * @param o1 the first object to compare
     * @param o2 the second object to compare
     * @return true if o1 is equals to o2 in term of model object
     */
    public static boolean areSame(Object o1, Object o2) {
        // particular case is applied for list because two different instances of empty list are considered equals
        return !(o2 != null && !o2.equals(o1)
                || (o2 == null && o1 != null)
                || (o2 != null && o1 != null && o2 instanceof List && o2 != o1));
    }
}
