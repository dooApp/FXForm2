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

package com.dooapp.fxform;

import com.dooapp.fxform.i18n.ResourceBundleHelper;
import com.dooapp.fxform.model.FormException;
import com.dooapp.fxform.model.FormFieldController;
import com.dooapp.fxform.model.FormFieldControllerFactory;
import com.dooapp.fxform.model.impl.FormFieldControllerFactoryImpl;
import com.dooapp.fxform.model.impl.ReflectionFieldProvider;
import com.dooapp.fxform.view.skin.DefaultSkin;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Control;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Antoine Mischler
 * Date: 09/04/11
 * Time: 21:26
 * The FXForm control
 */
public class FXForm<T> extends Control implements FormAPI<T> {

    private final T source;

    private StringProperty title = new SimpleStringProperty();

    private final ObservableList<Field> fields;

    private final ObservableList<FormFieldController> elements;

    private FormFieldControllerFactory formFieldControllerFactory = new FormFieldControllerFactoryImpl();

    public void setTitle(String title) {
        this.title.set(title);
    }

    public FXForm(T source) {
        initBundle();
        this.source = source;
        this.fields = FXCollections.observableList(new ReflectionFieldProvider().getProperties(source));
        this.elements = createElements();
        this.setSkin(new DefaultSkin(this));
    }

    private ObservableList<FormFieldController> createElements() {
        List<FormFieldController> fields = new LinkedList();
        for (Field field : this.fields) {
            FormFieldController controller = null;
            try {
                controller = formFieldControllerFactory.create(field, source);
            } catch (FormException e) {
                e.printStackTrace();
            }
            fields.add(controller);
        }
        return FXCollections.observableList(fields);
    }

    private void initBundle() {
        try {
            throw new Exception();
        } catch (Exception e) {
            final StackTraceElement element = e.getStackTrace()[2];
            String bundle = element.getClassName();
            ResourceBundleHelper.init(bundle);
            sceneProperty().addListener(new ChangeListener<Scene>() {
                public void changed(ObservableValue<? extends Scene> observableValue, Scene scene, Scene scene1) {
                    URL css = FXForm.class.getResource(element.getFileName().substring(0, element.getFileName().indexOf(".")) + ".css");
                    if (css != null && observableValue.getValue() != null) {
                        System.out.println("Registering " + css + " in " + observableValue.getValue());
                        getScene().getStylesheets().add(css.toExternalForm());
                    }
                }
            });
        }
    }

    public StringProperty titleProperty() {
        return title;
    }

    public ObservableList<FormFieldController> getElements() {
        return elements;
    }

    public T getSource() {
        return source;
    }

}
