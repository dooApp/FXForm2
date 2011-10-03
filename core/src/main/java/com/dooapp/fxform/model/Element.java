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

package com.dooapp.fxform.model;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 11/04/11
 * Time: 22:22
 * Model object wrapping an object field.
 */
public class Element<SourceType, WrappedType, FieldType extends ObservableValue<WrappedType>> implements ObservableValue<WrappedType> {

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
                return (FieldType) getField().get(getSource());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }
    };

    private List<ChangeListener> changeListeners = new LinkedList<ChangeListener>();

    private List<InvalidationListener> invalidationListeners = new LinkedList<InvalidationListener>();

    public Element(Field field) throws FormException {
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

    //@Override
    //public String toString() {
    //    return field.getName() + "[" + getSource().getClass() + "]";
    //}

    public ObjectBinding<FieldType> valueProperty() {
        return value;
    }

    public void addListener(ChangeListener changeListener) {
        changeListeners.add(changeListener);
        valueProperty().get().addListener(changeListener);

    }

    public void removeListener(ChangeListener changeListener) {
        changeListeners.remove(changeListener);
        valueProperty().get().removeListener(changeListener);
    }

    public WrappedType getValue() {
        return valueProperty().get().getValue();
    }

    public void addListener(InvalidationListener invalidationListener) {
        invalidationListeners.add(invalidationListener);
        valueProperty().addListener(invalidationListener);
    }

    public void removeListener(InvalidationListener invalidationListener) {
        invalidationListeners.remove(invalidationListener);
        valueProperty().removeListener(invalidationListener);
    }

    public void dispose() {
        value.dispose();
    }
}
