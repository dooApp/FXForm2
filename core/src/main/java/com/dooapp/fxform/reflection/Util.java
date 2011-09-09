package com.dooapp.fxform.reflection;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: antoine
 * Date: 09/09/11
 * Time: 14:37
 */
public class Util {

    private final static String PATTERN_STRING = "<L(.*);>;";

    private final static Pattern PATTTERN = Pattern.compile(PATTERN_STRING);

    private final static String SIGNATURE = "signature";

    /**
     * Tries to retrieve the generic parameter of an ObjectProperty at runtime. Some kind of magic need to be done there.
     *
     * @param field a Field of ObjectProperty<T> type
     * @return the Class of the generic parameter of the given field
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    public static Class getObjectPropertyGeneric(Field field) throws NoSuchFieldException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        Field signatureField = field.getClass().getDeclaredField(SIGNATURE);
        signatureField.setAccessible(true);
        String signature = (String) signatureField.get(field);
        Matcher matcher = PATTTERN.matcher(signature);
        if (matcher.find()) {
            return Class.forName(matcher.group(1).replaceAll("/", "."));
        }
        throw new ClassNotFoundException("Generic class could not be retrieved");
    }

}
