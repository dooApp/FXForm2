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

package com.dooapp.fxform;

import com.dooapp.fxform.filter.FieldFilter;
import com.dooapp.fxform.filter.NonVisualFilter;
import com.dooapp.fxform.model.DefaultElementProvider;
import com.dooapp.fxform.model.ElementProvider;
import com.dooapp.fxform.view.factory.DefaultFactoryProvider;
import com.dooapp.fxform.view.factory.DefaultLabelFactoryProvider;
import com.dooapp.fxform.view.factory.DefaultTooltipFactoryProvider;
import com.dooapp.fxform.view.factory.FactoryProvider;
import com.dooapp.fxform.view.skin.DefaultSkin;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.logging.Logger;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 09/04/11
 * Time: 21:26
 * The FXForm control
 */
public class FXForm<T> extends AbstractFXForm {

    private final static Logger logger = Logger.getLogger(FXForm.class.getName());

    private final ObjectProperty<T> source = new SimpleObjectProperty<T>();

    private final ListProperty<FieldFilter> filters = new SimpleListProperty<FieldFilter>(FXCollections.<FieldFilter>observableArrayList());

    public FXForm() {
        this(new DefaultFactoryProvider());
    }

    public FXForm(T source) {
        this(source, new DefaultFactoryProvider());
    }

    public FXForm(FactoryProvider editorFactoryProvider) {
        this(null, new DefaultLabelFactoryProvider(), new DefaultTooltipFactoryProvider(), editorFactoryProvider);
    }

    public FXForm(T source, FactoryProvider editorFactoryProvider) {
        this(source,
                new DefaultLabelFactoryProvider(),
                new DefaultTooltipFactoryProvider(),
                editorFactoryProvider
        );
    }

    public FXForm(FactoryProvider labelFactoryProvider, FactoryProvider tooltipFactoryProvider, FactoryProvider editorFactoryProvider) {
        this(null, labelFactoryProvider, tooltipFactoryProvider, editorFactoryProvider);
    }

    public FXForm(final T source, FactoryProvider labelFactoryProvider, FactoryProvider tooltipFactoryProvider, FactoryProvider editorFactoryProvider) {
        super();
        setLabelFactoryProvider(labelFactoryProvider);
        setTooltipFactoryProvider(tooltipFactoryProvider);
        setEditorFactoryProvider(editorFactoryProvider);
        filters.add(new NonVisualFilter());
        final ElementProvider elementProvider = new DefaultElementProvider();
        this.source.addListener(new ChangeListener<T>() {
            public void changed(ObservableValue<? extends T> observableValue, T oldSource, T newSource) {
                if (newSource == null) {
                    elementsProperty().unbind();
                    elementsProperty().clear();
                } else if (oldSource == null || (newSource.getClass() != oldSource.getClass())) {
                    elementsProperty().unbind();
                    elementsProperty().bind(elementProvider.getElements(sourceProperty(), filters));
                }
            }
        });
        this.setSkin(new DefaultSkin(this));
        getClassLevelValidator().beanProperty().bind(sourceProperty());
        setSource(source);
    }

    public T getSource() {
        return source.get();
    }

    public void setSource(T source) {
        this.source.set(source);
    }

    public ObjectProperty<T> sourceProperty() {
        return source;
    }

    public ObservableList<FieldFilter> getFilters() {
        return filters;
    }

    public void addFilters(FieldFilter... filters) {
        this.filters.addAll(filters);
    }

}
