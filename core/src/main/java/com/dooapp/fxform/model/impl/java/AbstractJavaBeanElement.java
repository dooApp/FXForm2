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

package com.dooapp.fxform.model.impl.java;

import com.dooapp.fxform.model.FormException;
import com.dooapp.fxform.model.PropertyElement;
import com.dooapp.fxform.model.impl.AbstractFieldElement;
import javafx.beans.property.Property;
import javafx.beans.property.adapter.JavaBeanObjectPropertyBuilder;
import javafx.beans.property.adapter.JavaBeanProperty;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 14/10/12
 * Time: 12:16
 */
public abstract class AbstractJavaBeanElement<WrappedType> extends AbstractFieldElement<Object, WrappedType> implements PropertyElement<WrappedType> {

    private final static Logger logger = Logger.getLogger(AbstractJavaBeanElement.class.getName());

    public AbstractJavaBeanElement(Field field) throws FormException {
        super(field);
    }

    protected JavaBeanProperty<WrappedType> buildJavaBeanProperty() throws NoSuchMethodException {
        return JavaBeanObjectPropertyBuilder
                .create()
                .bean(sourceProperty().getValue())
                .name(field.getName())
                .build();
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return field.getAnnotation(annotationClass);
    }

    @Override
    protected ObservableValue<WrappedType> computeValue() {
        try {
            return buildJavaBeanProperty();
        } catch (NoSuchMethodException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void setValue(WrappedType wrappedType) {
        ((WritableValue<WrappedType>) wrappedProperty().getValue()).setValue(wrappedType);
    }

    @Override
    public Class<?> getType() {
        if (wrappedProperty().getValue() != null) {
            return wrappedProperty().getValue().getClass();
        } else {
            return null;
        }
    }

    @Override
    public Class<WrappedType> getWrappedType() {
        return (Class<WrappedType>) field.getType();
    }

    @Override
    public void bind(ObservableValue<? extends WrappedType> observableValue) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void unbind() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean isBound() {
        return false;
    }

    @Override
    public void bindBidirectional(Property<WrappedType> wrappedTypeProperty) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void unbindBidirectional(Property<WrappedType> wrappedTypeProperty) {
        throw new UnsupportedOperationException("Not implemented");
    }

}
