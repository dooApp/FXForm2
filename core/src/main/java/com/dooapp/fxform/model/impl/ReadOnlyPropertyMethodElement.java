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
package com.dooapp.fxform.model.impl;

import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.model.FormException;
import com.dooapp.fxform.reflection.ReflectionUtils;
import javafx.beans.value.ObservableValue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

/**
 * An element based on a Method to access a property.
 * <p/>
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 16/10/13
 * Time: 14:33
 */
public class ReadOnlyPropertyMethodElement<SourceType, WrappedType> extends AbstractFieldElement<SourceType, WrappedType> implements Element<WrappedType> {

    private final static Logger logger = Logger.getLogger(ReadOnlyPropertyMethodElement.class.getName());

    private Method method;

    public ReadOnlyPropertyMethodElement(Field field) throws NoSuchMethodException, FormException {
        super(field);
    }

    protected final Method getMethod() {
        if (method == null) {
            try {
                method = ReflectionUtils.getPropertyGetter(field);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return method;
    }

    @Override
    protected ObservableValue<WrappedType> computeValue() {
        try {
            return (ObservableValue<WrappedType>) getMethod().invoke(getSource());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Class<?> getType() {
        return getMethod().getReturnType();
    }

    @Override
    public Class<WrappedType> getWrappedType() {
        return ReflectionUtils.getMethodReturnTypeGeneric(getSource(), getMethod());
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        T annotation = getMethod().getAnnotation(annotationClass);
        if (annotation == null) {
            annotation = super.getAnnotation(annotationClass);
        }
        return annotation;
    }
}
