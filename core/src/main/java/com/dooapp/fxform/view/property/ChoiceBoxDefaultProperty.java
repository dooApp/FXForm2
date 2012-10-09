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

package com.dooapp.fxform.view.property;

import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ChoiceBox;

/**
 * A virtual property to expose a Property for a ChoiceBox corresponding to the selected item.
 * <p/>
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 30/09/12
 * Time: 11:25
 */
public class ChoiceBoxDefaultProperty implements Property<Object> {

    private final ChoiceBox choiceBox;

    private final ObjectProperty property = new SimpleObjectProperty();

    public ChoiceBoxDefaultProperty(ChoiceBox choiceBox) {
        this.choiceBox = choiceBox;
        property.addListener(new ChangeListener<Object>() {
            public void changed(ObservableValue<? extends Object> observableValue, Object t, Object t1) {
                ChoiceBoxDefaultProperty.this.choiceBox.getSelectionModel().select(t1);
            }
        });
        this.choiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            public void changed(ObservableValue<? extends Object> observableValue, Object t, Object t1) {
                property.set(t1);
            }
        });
    }

    @Override
    public void bind(ObservableValue<? extends Object> observableValue) {
        property.bind(observableValue);
    }

    @Override
    public void unbind() {
        property.unbind();
    }

    @Override
    public boolean isBound() {
        return property.isBound();
    }

    @Override
    public void bindBidirectional(Property<Object> objectProperty) {
        property.bindBidirectional(objectProperty);
    }

    @Override
    public void unbindBidirectional(Property<Object> objectProperty) {
        objectProperty.unbindBidirectional(objectProperty);
    }

    @Override
    public Object getBean() {
        return property.getBean();
    }

    @Override
    public String getName() {
        return property.getName();
    }

    @Override
    public void addListener(ChangeListener<? super Object> changeListener) {
        property.addListener(changeListener);
    }

    @Override
    public void removeListener(ChangeListener<? super Object> changeListener) {
        property.removeListener(changeListener);
    }

    @Override
    public Object getValue() {
        return property.getValue();
    }

    @Override
    public void setValue(Object o) {
        property.setValue(o);
    }

    @Override
    public void addListener(InvalidationListener invalidationListener) {
        property.addListener(invalidationListener);
    }

    @Override
    public void removeListener(InvalidationListener invalidationListener) {
        property.removeListener(invalidationListener);
    }
}
