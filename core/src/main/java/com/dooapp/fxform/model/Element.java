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

import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;

import java.lang.reflect.Field;

/**
 * User: Antoine Mischler
 * Date: 11/04/11
 * Time: 22:22
 * Model object wrapping an object field.
 */
public abstract class Element<SourceType, WrappedType, FieldType> implements ObservableValue<WrappedType>, WritableValue<WrappedType> {

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

    public Element(Field field) {
        this.field = field;
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
    public String toString() {
        return "Element{" +
                "field=" + field +
                '}';
    }

    public ObjectBinding<FieldType> valueProperty() {
        return value;
    }

}
