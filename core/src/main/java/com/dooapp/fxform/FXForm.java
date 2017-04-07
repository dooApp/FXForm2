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

import com.dooapp.fxform.model.DefaultElementProvider;
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.model.ElementProvider;
import com.dooapp.fxform.model.impl.BufferedElement;
import com.dooapp.fxform.model.impl.BufferedPropertyElement;
import com.dooapp.fxform.view.factory.DefaultFactoryProvider;
import com.dooapp.fxform.view.factory.DefaultLabelFactoryProvider;
import com.dooapp.fxform.view.factory.DefaultTooltipFactoryProvider;
import com.dooapp.fxform.view.factory.FactoryProvider;
import com.dooapp.fxform.view.skin.DefaultSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;

import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
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

    private final ObjectProperty<ElementProvider> elementProvider = new SimpleObjectProperty<ElementProvider>();

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
        initBundle();
        setLabelFactoryProvider(labelFactoryProvider);
        setTooltipFactoryProvider(tooltipFactoryProvider);
        setEditorFactoryProvider(editorFactoryProvider);
        setElementProvider(new DefaultElementProvider());
        this.source.addListener(new ChangeListener<T>() {
            public void changed(ObservableValue<? extends T> observableValue, T oldSource, T newSource) {
                if (newSource == null) {
                    elementsProperty().unbind();
                    elementsProperty().clear();
                } else if (oldSource == null || (newSource.getClass() != oldSource.getClass())) {
                    elementsProperty().unbind();
                    elementsProperty().bind(getElementProvider().getElements(sourceProperty()));
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

    /**
     * Auto loading of default resource bundle and css file.
     */
    private void initBundle() {
        final StackTraceElement element = getCallingClass();
        String bundle = element.getClassName();
        if (getResourceBundle() == null) {
            try {
                setResourceBundle(ResourceBundle.getBundle(bundle));
            } catch (MissingResourceException e) {
                // no default resource bundle found
            }
        }
        sceneProperty().addListener(new ChangeListener<Scene>() {
            public void changed(ObservableValue<? extends Scene> observableValue, Scene scene, Scene scene1) {
                String path = element.getClassName().replaceAll("\\.", "/") + ".css";
                URL css = FXForm.class.getClassLoader().getResource(path);
                if (css != null && observableValue.getValue() != null) {
                    getScene().getStylesheets().add(css.toExternalForm());
                }
            }
        });
    }

    /**
     * Retrieve the calling class in which the form is being created.
     *
     * @return the StackTraceElement representing the calling class
     */
    private StackTraceElement getCallingClass() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        int i = 1;
        while (stackTrace[i].getClassName().equals(getClass().getName())) {
            i++;
        }
        return stackTrace[i];
    }

    public ElementProvider getElementProvider() {
        return elementProvider.get();
    }

    public ObjectProperty<ElementProvider> elementProviderProperty() {
        return elementProvider;
    }

    public void setElementProvider(ElementProvider elementProvider) {
        this.elementProvider.set(elementProvider);
    }

    /**
     * Returns true if all from values are valid.
     *
     * @return true if all from values are valid
     */
    public boolean isValid() {
        return getConstraintViolations().isEmpty();
    }

    /**
     * Commits the form content to the Java bean if the form is buffered.
     *
     * @return true if the form values could be committed or false if one of the form values is invalid
     */
    public boolean commit() {
        if (isValid()) {
            for (Element element : getElements()) {
                if (element instanceof BufferedPropertyElement) {
                    ((BufferedPropertyElement) element).commit();
                }
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * Reloads the form content from the Java bean if the form is buffered.
     */
    public void reload() {
        for (Element element : getElements()) {
            if (element instanceof BufferedElement) {
                ((BufferedElement) element).reload();
            }
        }
    }

}
