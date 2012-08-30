/*
 * Copyright (c) 2011, dooApp <contact@dooapp.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of dooApp nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.dooapp.fxform;

import com.dooapp.fxform.annotation.FormFactory;
import com.dooapp.fxform.annotation.NonVisual;
import com.dooapp.fxform.filter.ReorderFilter;
import com.dooapp.fxform.controller.PropertyElementController;
import com.dooapp.fxform.view.FXFormSkinFactory;
import com.dooapp.fxform.view.NodeCreationException;
import com.dooapp.fxform.view.factory.DisposableNode;
import com.dooapp.fxform.view.factory.DisposableNodeWrapper;
import com.dooapp.fxform.view.factory.NodeFactory;
import javafx.application.Application;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.SceneBuilder;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.log4j.BasicConfigurator;
import org.hibernate.validator.constraints.Email;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 09/04/11
 * Time: 21:44
 * FXForm demo
 */
public class Demo extends Application {

    /**
     * The bean to be edited.
     */
    private static class MyBean {

        private static enum Subject {
            CONTACT, QUESTION, BUG, FEEDBACK
        }

        private final StringProperty name = new SimpleStringProperty();

        private final StringProperty email = new SimpleStringProperty();

        @FormFactory(TextAreaFactory.class)
        private final StringProperty message = new SimpleStringProperty();

        private final StringProperty website = new SimpleStringProperty();

        private final BooleanProperty subscribe = new SimpleBooleanProperty();

        private final ObjectProperty<Subject> subject = new SimpleObjectProperty<Subject>();

        private final DoubleProperty age = new SimpleDoubleProperty();

        private MyBean(String name, String email, String message, String website, boolean subscribe, Subject subject) {
            this.name.set(name);
            this.email.set(email);
            this.message.set(message);
            this.website.set(website);
            this.subscribe.set(subscribe);
            this.subject.set(subject);
        }

        @Email
        public String getEmail() {
            return email.get();
        }
    }

    /**
     * Example of custom factory
     */
    public static class TextAreaFactory implements NodeFactory<PropertyElementController<String>> {
        public DisposableNode createNode(PropertyElementController<String> controller) throws NodeCreationException {
            TextArea textArea = new TextArea();
            return new DisposableNodeWrapper(textArea, new Callback<Node, Void>() {
                public Void call(Node node) {
                    return null;
                }
            });
        }
    }

    private FXForm<MyBean> fxForm = new FXForm<MyBean>();

    private StackPane root = new StackPane();

    private final String css = Demo.class.getResource("style.css").toExternalForm();

    protected void setup() {
        MyBean joe = new MyBean("Joe", "contact@", "How does this crazy form works?", "www.dooapp.com", true, MyBean.Subject.QUESTION);
        new ObjectPropertyObserver(joe);
        fxForm.setSource(joe);
        fxForm.addFilters(new ReorderFilter("name", "email", "website", "subject", "message"));
        fxForm.setTitle("Dude, where is my form?");
        root.getChildren().add(createNode());
    }

    private Node createNode() {
        VBox vBox = new VBox();
        vBox.getChildren().addAll(createSkinSelector(), createCSSNode(), fxForm);
        return vBox;
    }

    private Node createCSSNode() {
        CheckBox checkBox = new CheckBox("Use css");
        checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean1) {
                if (aBoolean1) {
                    root.getScene().getStylesheets().add(css);
                } else {
                    root.getScene().getStylesheets().remove(css);
                }
            }
        });
        checkBox.setSelected(true);
        return checkBox;
    }

    /**
     * Create a selector that changes the skin of the form.
     *
     * @return
     */
    private Node createSkinSelector() {
        ChoiceBox<FXFormSkinFactory> choiceBox = new ChoiceBox<FXFormSkinFactory>();
        choiceBox.getItems().addAll(FXFormSkinFactory.DEFAULT_FACTORY, FXFormSkinFactory.INLINE_FACTORY);
        choiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<FXFormSkinFactory>() {
            public void changed(ObservableValue<? extends FXFormSkinFactory> observableValue, FXFormSkinFactory fxFormSkinFactory, FXFormSkinFactory fxFormSkinFactory1) {
                fxForm.setSkin(fxFormSkinFactory1.createSkin(fxForm));
            }
        });
        choiceBox.getSelectionModel().selectFirst();
        return choiceBox;
    }

    public static void main(String[] args) {
        BasicConfigurator.configure();
        Application.launch(Demo.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("FXForm Demo");
        stage.setScene(SceneBuilder.create().root(root).build());
        setup();
        stage.show();
    }

}
