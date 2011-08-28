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

import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.model.FormException;
import javafx.beans.InvalidationListener;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Antoine Mischler
 * Date: 26/04/11
 * Time: 12:11
 * An observable and writable form field.
 */
public class PropertyElement<SourceType, WrappedType> extends Element<SourceType, WrappedType, Property<WrappedType>> {

    private List<ChangeListener> changeListeners = new LinkedList<ChangeListener>();

    private List<InvalidationListener> invalidationListeners = new LinkedList<InvalidationListener>();


    public PropertyElement(Field field) throws FormException {
        super(field);
        if (!WritableValue.class.isAssignableFrom(field.getType())) {
            throw new FormException("Trying to create a writable element with a non-writable field: " + field.getType());
        }
        valueProperty().addListener(new ChangeListener<Property<WrappedType>>() {

            public void changed(ObservableValue<? extends Property<WrappedType>> observableValue, Property<WrappedType> wrappedTypeProperty, Property<WrappedType> wrappedTypeProperty1) {
                for (InvalidationListener invalidationListener : invalidationListeners) {
                    wrappedTypeProperty.removeListener(invalidationListener);
                    wrappedTypeProperty1.addListener(invalidationListener);
                    invalidationListener.invalidated(observableValue);
                }
                for (ChangeListener changeListener : changeListeners) {
                    wrappedTypeProperty.removeListener(changeListener);
                    wrappedTypeProperty1.addListener(changeListener);
                    changeListener.changed(observableValue, wrappedTypeProperty.getValue(), wrappedTypeProperty1.getValue());
                }
            }
        });
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

    public void setValue(WrappedType o) {
        valueProperty().get().setValue(o);
    }

    public void addListener(InvalidationListener invalidationListener) {
        invalidationListeners.add(invalidationListener);
        valueProperty().addListener(invalidationListener);
    }

    public void removeListener(InvalidationListener invalidationListener) {
        invalidationListeners.remove(invalidationListener);
        valueProperty().removeListener(invalidationListener);
    }

}
