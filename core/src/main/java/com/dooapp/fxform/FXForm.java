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
import com.dooapp.fxform.model.ElementController;
import com.dooapp.fxform.model.ElementControllerFactory;
import com.dooapp.fxform.model.FormException;
import com.dooapp.fxform.model.impl.ElementControllerFactoryImpl;
import com.dooapp.fxform.model.impl.ReflectionFieldProvider;
import com.dooapp.fxform.utils.ConfigurationStore;
import com.dooapp.fxform.view.factory.DefaultLabelFactory;
import com.dooapp.fxform.view.factory.DelegateFactoryImpl;
import com.dooapp.fxform.view.factory.NodeFactory;
import com.dooapp.fxform.view.handler.FieldHandler;
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

/**
 * User: Antoine Mischler
 * Date: 09/04/11
 * Time: 21:26
 * The FXForm control
 */
public class FXForm<T> extends Control implements FormAPI<T> {

    public static final String LABEL_ID_SUFFIX = "-form-label";

    public static final String LABEL_STYLE = "form-label";

    public static final String EDITOR_ID_SUFFIX = "-form-editor";

    public static final String EDITOR_STYLE = "form-editor";

    public static final String TOOLTIP_ID_SUFFIX = "-form-tooltip";

    public static final String TOOLTIP_STYLE = "form-tooltip";

    /**
     * Register a global factory for all forms.
     *
     * @param handler
     * @param factory
     */
    public static void registerGlobalFactory(FieldHandler handler, NodeFactory factory) {

    }

    private final T source;

    private StringProperty title = new SimpleStringProperty();

    private final ConfigurationStore<ElementController> controllers = new ConfigurationStore<ElementController>();

    private ElementControllerFactory elementControllerFactory = new ElementControllerFactoryImpl();

    public void setTitle(String title) {
        this.title.set(title);
    }

    public FXForm(T source) {
        initBundle();
        this.source = source;
        controllers.addConfigurer(new NodeFactoryConfigurer(new DefaultLabelFactory(), LABEL_ID_SUFFIX, LABEL_STYLE) {

            @Override
            protected void applyTo(NodeFactory factory, ElementController controller) {
                controller.setLabelFactory(factory);
            }
        });
        controllers.addConfigurer(new NodeFactoryConfigurer(new DefaultLabelFactory(), TOOLTIP_ID_SUFFIX, TOOLTIP_STYLE) {

            @Override
            protected void applyTo(NodeFactory factory, ElementController controller) {
                controller.setTooltipFactory(factory);
            }
        });
        controllers.addConfigurer(new NodeFactoryConfigurer(new DelegateFactoryImpl(), EDITOR_ID_SUFFIX, EDITOR_STYLE) {

            @Override
            protected void applyTo(NodeFactory factory, ElementController controller) {
                controller.setEditorFactory(factory);
            }
        });
        createElements();
        this.setSkin(new DefaultSkin(this));
    }

    private void createElements() {
        for (Field field : FXCollections.observableList(new ReflectionFieldProvider().getProperties(source))) {
            ElementController controller = null;
            try {
                controller = elementControllerFactory.create(field, source);
            } catch (FormException e) {
                e.printStackTrace();
            }
            controllers.add(controller);
        }
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

    public ObservableList<ElementController> getControllers() {
        return controllers;
    }

    public T getSource() {
        return source;
    }

}
