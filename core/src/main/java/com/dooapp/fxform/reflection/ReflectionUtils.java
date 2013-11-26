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
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 09/09/11
 * Time: 14:37
 */
public class ReflectionUtils {

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
                return (Class) ((ParameterizedType) source.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            } else {
                // The actual type is not declared, use the upper bound of the type e.g. U
                return (Class) ((TypeVariable) type1).getBounds()[0];
            }
        } else {
            return (Class) type1;
        }
    }

    public static List<Field> listFields(Class clazz) {
        List<Field> result = new LinkedList<Field>();
        ReflectionUtils.fillFields(clazz, result);
        return result;
    }

    public static void fillFields(Class clazz, List<Field> result) {
        for (Field field : clazz.getDeclaredFields()) {
            // ignore synthetic fields, see #21
            if (!field.isSynthetic()) {
                result.add(field);
            }
        }

        for (Field field : result) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
        }
        if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class) {
            fillFields(clazz.getSuperclass(), result);
        }
    }

}
