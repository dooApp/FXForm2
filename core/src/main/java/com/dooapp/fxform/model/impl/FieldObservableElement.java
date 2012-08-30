/*
 * Copyright (c) 2011, dooApp <contact@dooapp.com>
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

import com.dooapp.fxform.model.FormException;
import com.dooapp.fxform.model.ObservableElement;
import com.dooapp.fxform.reflection.Util;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 11/04/11
 * Time: 22:22
 * Model object wrapping an object field.
 */
public class FieldObservableElement<SourceType, WrappedType, FieldType extends ObservableValue<WrappedType>> implements ObservableElement<WrappedType> {

    private final Logger logger = LoggerFactory.getLogger(FieldObservableElement.class);

    protected final Field field;

    private final ObjectProperty<SourceType> source = new SimpleObjectProperty<SourceType>();

    private final ObjectBinding<FieldType> value = new ObjectBinding<FieldType>() {

        {
            super.bind(sourceProperty());
        }

        @Override
        protected FieldType computeValue() {
            if (getSource() == null) {
                return null;
            }
            try {
                return (FieldType) field.get(getSource());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }
    };

    private List<ChangeListener> changeListeners = new LinkedList<ChangeListener>();

    private List<InvalidationListener> invalidationListeners = new LinkedList<InvalidationListener>();

    public FieldObservableElement(Field field) throws FormException {
        this.field = field;
        if (!ObservableValue.class.isAssignableFrom(field.getType())) {
            throw new FormException("Trying to create an observable element with a non-observable field " + field.getType());
        }
        valueProperty().addListener(new ChangeListener<FieldType>() {

            public void changed(ObservableValue<? extends FieldType> observableValue, FieldType fieldType, FieldType fieldType1) {
                for (InvalidationListener invalidationListener : invalidationListeners) {
                    fieldType.removeListener(invalidationListener);
                    if (fieldType1 != null) {
                        fieldType1.addListener(invalidationListener);
                    }
                    invalidationListener.invalidated(observableValue);
                }
                for (ChangeListener changeListener : changeListeners) {
                    fieldType.removeListener(changeListener);
                    if (fieldType1 != null) {
                        fieldType1.addListener(changeListener);
                        changeListener.changed(observableValue, fieldType.getValue(), fieldType1.getValue());
                    }
                }
            }
        });
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

    public ObjectBinding<FieldType> valueProperty() {
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

    public Class<? extends ObservableValue<WrappedType>> getType() {
        return (Class<? extends ObservableValue<WrappedType>>) field.getType();
    }

    public Class<? extends WrappedType> getValueType() {
        try {
            return Util.getObjectPropertyGeneric(field);
        } catch (Exception e) {
        }
        return null;
    }

    public String getName() {
        return field.getName();
    }

    public Field getField() {
        return field;
    }
}
