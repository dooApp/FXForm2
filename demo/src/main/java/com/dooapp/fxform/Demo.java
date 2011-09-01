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

import com.dooapp.fxform.filter.ReorderFilter;
import com.dooapp.fxform.model.ElementController;
import com.dooapp.fxform.view.NodeCreationException;
import com.dooapp.fxform.view.factory.DelegateFactory;
import com.dooapp.fxform.view.factory.NodeFactory;
import com.dooapp.fxform.view.handler.NamedFieldHandler;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.log4j.BasicConfigurator;

/**
 * User: Antoine Mischler
 * Date: 09/04/11
 * Time: 21:44
 * FXForm demo
 */
public class Demo extends Application {

    public static void main(String[] args) {
        BasicConfigurator.configure();
        Application.launch(Demo.class, args);
    }

    @Override
    public void start(Stage stage) {
        VBox root = new VBox();
        root.setPadding(new Insets(10, 10, 10, 10));
        final DemoObject instance1 = new SubDemoObject();
        instance1.setName("John");
        instance1.setAge(18);
        final DemoObject instance2 = new SubDemoObject();
        instance2.setName("Julio");
        instance2.setAge(4);
        new ObjectPropertyObserver(instance1);
        new ObjectPropertyObserver(instance2);
        final FXForm fxForm = createFXForm();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Demo.class.getResource("style.css").toExternalForm());
        ChoiceBox<FXFormSkinFactory> skinChoiceBox = new ChoiceBox<FXFormSkinFactory>();
        skinChoiceBox.getItems().addAll(FXFormSkinFactory.DEFAULT_FACTORY, FXFormSkinFactory.INLINE_FACTORY);
        skinChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<FXFormSkinFactory>() {
            public void changed(ObservableValue<? extends FXFormSkinFactory> observableValue, FXFormSkinFactory fxFormSkin, FXFormSkinFactory fxFormSkin1) {
                fxForm.setSkin(fxFormSkin1.createSkin(fxForm));
            }
        });
        ChoiceBox<DemoObject> instanceChoiceBox = new ChoiceBox<DemoObject>();
        instanceChoiceBox.getItems().addAll(instance1, instance2);
        instanceChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<DemoObject>() {
            public void changed(ObservableValue<? extends DemoObject> observableValue, DemoObject demoObject, DemoObject demoObject1) {
                fxForm.setSource(demoObject1);
            }
        });
        instanceChoiceBox.selectionModelProperty().get().selectFirst();
        skinChoiceBox.selectionModelProperty().get().selectFirst();
        root.getChildren().addAll(skinChoiceBox, instanceChoiceBox, fxForm);
        stage.setScene(scene);
        stage.setVisible(true);
        fxForm.addFilters(new ReorderFilter(new String[]{"age", "mail"}));
    }

    private FXForm<DemoObject> createFXForm() {
        DelegateFactory.addGlobalFactory(new NamedFieldHandler("height"), new NodeFactory<ElementController>() {
            public Node createNode(ElementController controller) throws NodeCreationException {
                return new Button("Custom factory");
            }
        });
        FXForm<DemoObject> fxForm = new FXForm<DemoObject>(new DelegateFactory());
        fxForm.setTitle("Dude, where is my form?");
        return fxForm;
    }
}
