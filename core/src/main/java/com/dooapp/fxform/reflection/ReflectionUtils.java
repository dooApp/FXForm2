/*
 * Copyright (c) 2012, dooApp <contact@dooapp.com>
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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 09/09/11
 * Time: 14:37
 */
public class ReflectionUtils {

    /**
     * Tries to retrieve the generic parameter of an ObjectProperty at runtime.
     */
    public static Class getObjectPropertyGeneric(Field field) {
        Type type = field.getGenericType();
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            Type type1 = pType.getActualTypeArguments()[0];
            if (type1 instanceof ParameterizedType) {
                return (Class) ((ParameterizedType) type1).getRawType();
            } else {
                return (Class) type1;
            }
        } else {
            return field.getType();
        }
    }

    /**
     * Tries to retrieve the generic parameter of an ObjectProperty return by a method at runtime.
     */
    public static Class getMethodReturnTypeGeneric(Method method) {
        Type type = method.getGenericReturnType();
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            Type type1 = pType.getActualTypeArguments()[0];
            if (type1 instanceof ParameterizedType) {
                return (Class) ((ParameterizedType) type1).getRawType();
            } else {
                return (Class) type1;
            }
        } else {
            return method.getReturnType();
        }
    }

}
