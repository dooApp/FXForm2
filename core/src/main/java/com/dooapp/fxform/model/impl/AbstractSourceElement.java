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

import com.dooapp.fxform.model.Category;
import com.dooapp.fxform.model.Element;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Binding;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.LinkedList;
import java.util.List;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 16/10/13
 * Time: 14:46
 */
public abstract class AbstractSourceElement<SourceType, WrappedType> implements Element<WrappedType> {

    private final ObjectProperty<SourceType> source = new SimpleObjectProperty<>();

    protected List<ChangeListener<? super WrappedType>> changeListeners = new LinkedList<>();

    protected List<InvalidationListener> invalidationListeners = new LinkedList<>();

    private Binding<ObservableValue<WrappedType>> value;

    private StringProperty category;

    protected AbstractSourceElement() {
    }

    public SourceType getSource() {
        return source.get();
    }

    public void setSource(SourceType source) {
        this.source.set(source);
    }

    public Property sourceProperty() {
        return source;
    }

    public void addListener(ChangeListener<? super WrappedType> changeListener) {
        changeListeners.add(changeListener);
        if (wrappedProperty().getValue() != null) {
            wrappedProperty().getValue().addListener(changeListener);
        }
    }

    public void removeListener(ChangeListener<? super WrappedType> changeListener) {
        changeListeners.remove(changeListener);
        if (wrappedProperty().getValue() != null) {
            wrappedProperty().getValue().removeListener(changeListener);
        }
    }

    public void addListener(InvalidationListener invalidationListener) {
        invalidationListeners.add(invalidationListener);
        if (wrappedProperty().getValue() != null) {
            wrappedProperty().addListener(invalidationListener);
        }
    }

    public void removeListener(InvalidationListener invalidationListener) {
        invalidationListeners.remove(invalidationListener);
        if (wrappedProperty().getValue() != null) {
            wrappedProperty().removeListener(invalidationListener);
        }
    }

    public void dispose() {
        source.unbind();
        value.dispose();
    }

    public Object getBean() {
        return getSource();
    }

    public WrappedType getValue() {
        if (wrappedProperty().getValue() != null) {
            return wrappedProperty().getValue().getValue();
        } else {
            return null;
        }
    }

    public ObservableValue<ObservableValue<WrappedType>> wrappedProperty() {
        if (value == null) {
            value = createValue();
        }
        return value;
    }

    protected abstract Binding<ObservableValue<WrappedType>> createValue();

    protected abstract ObservableValue<WrappedType> computeValue();

    public String getCategory() {
        return categoryProperty().get();
    }

    public StringProperty categoryProperty() {
        if (category == null) {
            category = new SimpleStringProperty();
            Category categoryAnnotation = getAnnotation(Category.class);
            if (categoryAnnotation != null) {
                category.set(categoryAnnotation.value());
            }
        }
        return category;
    }

    public void setCategory(String category) {
        categoryProperty().set(category);
    }

}
