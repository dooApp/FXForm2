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
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Antoine Mischler
 * Date: 26/04/11
 * Time: 11:45
 * <p/>
 * A form field that represent an observable value.
 */
public class ReadOnlyPropertyElement<SourceType, WrappedType> extends Element<SourceType, WrappedType, ReadOnlyProperty<WrappedType>> {

    private final static Logger logger = LoggerFactory.getLogger(ReadOnlyPropertyElement.class);

    private List<ChangeListener> changeListeners = new LinkedList<ChangeListener>();

    private List<InvalidationListener> invalidationListeners = new LinkedList<InvalidationListener>();

    public ReadOnlyPropertyElement(Field field) throws FormException {
        super(field);
        if (!ObservableValue.class.isAssignableFrom(field.getType())) {
            throw new FormException("Trying to create an observable element with a non-observable field " + field.getType());
        }
        valueProperty().addListener(new ChangeListener<ReadOnlyProperty<WrappedType>>() {

            public void changed(ObservableValue<? extends ReadOnlyProperty<WrappedType>> observableValue, ReadOnlyProperty<WrappedType> wrappedTypeReadOnlyProperty, ReadOnlyProperty<WrappedType> wrappedTypeReadOnlyProperty1) {
                for (InvalidationListener invalidationListener : invalidationListeners) {
                    wrappedTypeReadOnlyProperty.removeListener(invalidationListener);
                    wrappedTypeReadOnlyProperty1.addListener(invalidationListener);
                    invalidationListener.invalidated(observableValue);
                }
                for (ChangeListener changeListener : changeListeners) {
                    wrappedTypeReadOnlyProperty.removeListener(changeListener);
                    wrappedTypeReadOnlyProperty1.addListener(changeListener);
                    changeListener.changed(observableValue, wrappedTypeReadOnlyProperty, wrappedTypeReadOnlyProperty1);
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
        // nothing to do, we are only observable
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
