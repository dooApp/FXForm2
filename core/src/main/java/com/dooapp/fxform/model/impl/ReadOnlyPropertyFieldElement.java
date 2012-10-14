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

package com.dooapp.fxform.model.impl;

import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.model.FormException;
import com.dooapp.fxform.reflection.ReflectionUtils;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * Created at 27/09/12 13:32.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class ReadOnlyPropertyFieldElement<SourceType, WrappedType> implements Element<WrappedType> {

    protected final Field field;

    private final ObjectProperty<SourceType> source = new SimpleObjectProperty<SourceType>();

    private final ObjectBinding<ObservableValue<WrappedType>> value = new ObjectBinding<ObservableValue<WrappedType>>() {

        {
            super.bind(sourceProperty());
        }

        @Override
        protected ObservableValue<WrappedType> computeValue() {
            if (getSource() == null) {
                return null;
            }
            try {
                return (ObservableValue<WrappedType>) getField().get(getSource());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void dispose() {
            super.dispose();
            unbind(sourceProperty());
        }
    };

    private List<ChangeListener> changeListeners = new LinkedList<ChangeListener>();

    private List<InvalidationListener> invalidationListeners = new LinkedList<InvalidationListener>();

    public ReadOnlyPropertyFieldElement(Field field) throws FormException {
        this.field = field;
        if (!ObservableValue.class.isAssignableFrom(field.getType())) {
            throw new FormException("Trying to create an observable field element with a non-observable field " + field.getType());
        }
        valueProperty().addListener(new ChangeListener<ObservableValue<WrappedType>>() {
            public void changed(ObservableValue<? extends ObservableValue<WrappedType>> observableValue, ObservableValue<WrappedType> wrappedTypeObservableValue, ObservableValue<WrappedType> wrappedTypeObservableValue1) {
                for (InvalidationListener invalidationListener : invalidationListeners) {
                    wrappedTypeObservableValue.removeListener(invalidationListener);
                    if (wrappedTypeObservableValue1 != null) {
                        wrappedTypeObservableValue1.addListener(invalidationListener);
                    }
                    invalidationListener.invalidated(observableValue);
                }
                for (ChangeListener changeListener : changeListeners) {
                    wrappedTypeObservableValue.removeListener(changeListener);
                    if (wrappedTypeObservableValue1 != null) {
                        wrappedTypeObservableValue1.addListener(changeListener);
                        changeListener.changed(observableValue, wrappedTypeObservableValue.getValue(), wrappedTypeObservableValue1.getValue());
                    }
                }
            }
        });
    }

    public Field getField() {
        return field;
    }

    public SourceType getSource() {
        return source.get();
    }

    public void setSource(SourceType source) {
        this.source.set(source);
    }

    public ObjectProperty<SourceType> sourceProperty() {
        return source;
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return field.getAnnotation(annotationClass);
    }

    public ObjectBinding<ObservableValue<WrappedType>> valueProperty() {
        return value;
    }

    public void addListener(ChangeListener changeListener) {
        changeListeners.add(changeListener);
        valueProperty().get().addListener(changeListener);

    }

    public void removeListener(ChangeListener changeListener) {
        changeListeners.remove(changeListener);
        if (valueProperty().get() != null) {
            valueProperty().get().removeListener(changeListener);
        }
    }

    public WrappedType getValue() {
        if (valueProperty().get() != null) {
            return valueProperty().get().getValue();
        } else {
            return null;
        }
    }

    public void addListener(InvalidationListener invalidationListener) {
        invalidationListeners.add(invalidationListener);
        valueProperty().addListener(invalidationListener);
    }

    public void removeListener(InvalidationListener invalidationListener) {
        invalidationListeners.remove(invalidationListener);
        if (valueProperty().get() != null) {
            valueProperty().removeListener(invalidationListener);
        }
    }

    public void dispose() {
        value.dispose();
        source.unbind();
    }

    public Object getBean() {
        return getSource();
    }

    public String getName() {
        return field.getName();
    }

    public Class<?> getType() {
        return field.getType();
    }

    @Override
    public Class<WrappedType> getGenericType() {
        return ReflectionUtils.getObjectPropertyGeneric(field);
    }

    @Override
    public String toString() {
        return "ReadOnlyPropertyFieldElement{" +
                "field=" + field +
                '}';
    }
}