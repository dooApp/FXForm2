/*
 * Copyright (c) 2013, dooApp <contact@dooapp.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of dooApp nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.dooapp.fxform.reflection;

import java.lang.reflect.*;
import java.util.LinkedList;
import java.util.List;

/**
 * This class contains reflection utilities to automatically extract fields and generic types.
 * <p/>
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 09/09/11
 * Time: 14:37
 */
public class ReflectionUtils {

    private final static System.Logger logger = System.getLogger(ReflectionUtils.class.getName());

    public final static String PROPERTY_GETTER = "Property";

    /**
     * Tries to retrieve the generic parameter of an ObjectProperty at runtime.
     */
    public static Class getObjectPropertyGeneric(Object source, Field field) {
        Type type = field.getGenericType();
        if (type instanceof ParameterizedType) {
            return getGenericClass(source, (ParameterizedType) type);
        } else {
            return field.getType();
        }
    }

    /**
     * Tries to retrieve the generic parameter of an ObjectProperty return by a method at runtime.
     */
    public static Class getMethodReturnTypeGeneric(Object source, Method method) {
        Type type = method.getGenericReturnType();
        if (type instanceof ParameterizedType) {
            return getGenericClass(source, (ParameterizedType) type);
        } else {
            return method.getReturnType();
        }
    }

    /**
     * Try to extract the generic type of the given ParameterizedType used in the given source object.
     *
     * @param source
     * @param type
     * @return
     */
    private static Class getGenericClass(Object source, ParameterizedType type) {
        Type type1 = type.getActualTypeArguments()[0];
        if (type1 instanceof ParameterizedType) {
            return (Class) ((ParameterizedType) type1).getRawType();
        } else if (type1 instanceof TypeVariable) {
            // Type is generic, try to get its actual type from the super class
            // e.g.: ObjectProperty<T> where T extends U
            if (source.getClass().getGenericSuperclass() instanceof ParameterizedType) {
                Type parameterizedType = ((ParameterizedType) source.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                if (parameterizedType instanceof ParameterizedType) {  // it means that the parent class is also generic
                    return (Class) ((ParameterizedType) parameterizedType).getRawType();
                } else {
                    return (Class) parameterizedType;
                }
            } else {
                // The actual type is not declared, use the upper bound of the type e.g. U
                return (Class) ((TypeVariable) type1).getBounds()[0];
            }
        } else {
            return (Class) type1;
        }
    }

    /**
     * Extract a collection of fields using their name from a given class and fill
     * a list with it.
     *
     * @param clazz  the Class to extract the fields from
     * @param result the list of Fields to fill with the extracted fields
     * @param fields the field names
     */
    public static void fillFieldsByName(Class clazz, List<Field> result, List<String> fields) {
        fillFieldsByName(clazz, result, fields.toArray(new String[fields.size()]));
    }

    /**
     * Extract a collection of fields using their name from a given class and fill
     * a list with it.
     *
     * @param clazz  the Class to extract the fields from
     * @param result the list of Fields to fill with the extracted fields
     * @param fields the field names
     */
    public static void fillFieldsByName(Class clazz, List<Field> result, String... fields) {
        for (String fieldName : fields) {
            Field field = getFieldByName(clazz, fieldName);
            if (field != null) {
                result.add(field);
            }
        }
    }

    /**
     * Extract a Field from a given class using its name.
     * This method will look into the given class declared fields. If the field is not
     * found, it will then look recursively in all super classes of the given class.
     *
     * @param clazz     the class to extract the Field from
     * @param fieldName the name of the field to extract
     * @return the extracted Field, null if not found
     */
    public static Field getFieldByName(Class clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            fixAccessible(field);
            return field;
        } catch (NoSuchFieldException e) {
            if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class) {
                return getFieldByName(clazz.getSuperclass(), fieldName);
            }
        }
        return null;
    }

    /**
     * Get a list of all fields from a given class. This includes all declared fields from the given class and its super classes.
     *
     * @param clazz the class to get the fields from
     * @return the list of all declared fields
     */
    public static List<Field> listFields(Class clazz) {
        List<Field> result = new LinkedList<Field>();
        ReflectionUtils.fillFields(clazz, result);
        return result;
    }

    /**
     * Fill a list with all fields from a given class.This includes all declared fields from the given class and its super classes.
     *
     * @param clazz  the class to get the fields from
     * @param result the list to fill
     */
    public static void fillFields(Class clazz, List<Field> result) {
        for (Field field : clazz.getDeclaredFields()) {
            result.add(field);
        }

        fixAccessible(result);
        if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class) {
            fillFields(clazz.getSuperclass(), result);
        }
    }

    /**
     * Fix the accessibility of a collection of fields to true
     *
     * @param result the collection of Fields to fix
     */
    private static void fixAccessible(List<Field> result) {
        for (Field field : result) {
            fixAccessible(field);
        }
    }

    /**
     * Fix the accessibility of a field to true
     *
     * @param field the fields to fix
     */
    private static void fixAccessible(Field field) {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    /**
     * This method tries to find the property getter corresponding to a given javafx property field.
     *
     * @param field
     * @return
     */
    public static Method getPropertyGetter(Field field) throws NoSuchMethodException {
        return field.getDeclaringClass().getMethod(field.getName() + PROPERTY_GETTER);
    }

    /**
     * Try to extract the value type of a MapProperty field.s
     *
     * @param field
     * @return
     */
    public static Class getMapPropertyValueType(Field field) {
        Type type = field.getGenericType();
        if (type instanceof ParameterizedType) {
            return (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
        } else {
            return null;
        }
    }

}
