package com.dooapp.fxform;

import com.dooapp.fxform.reflection.impl.ReflectionFieldProvider;

import java.lang.reflect.Field;
import java.util.List;

/**
 * User: antoine
 * Date: 12/09/11
 * Time: 16:53
 */
public class TestUtils {

    /**
     * Check whether a list of fields contains a field with the given name.
     *
     * @param name
     * @param fields
     * @return
     */
    public static boolean containsNamedField(String name, List<Field> fields) {
        for (Field field : fields) {
            if (name.equals(field.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get a list of test fields based from TestBean.
     *
     * @return
     */
    public static List<Field> getTestFields() {
        return new ReflectionFieldProvider().getProperties(new TestBean());
    }

}
