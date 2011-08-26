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

import com.dooapp.fxform.model.ElementController;
import com.dooapp.fxform.view.FXFormSkin;
import com.dooapp.fxform.view.NodeCreationException;
import com.dooapp.fxform.view.factory.DelegateFactory;
import com.dooapp.fxform.view.factory.NodeFactory;
import com.dooapp.fxform.view.handler.NamedFieldHandler;
import com.dooapp.fxform.view.skin.DefaultSkin;
import com.dooapp.fxform.view.skin.InlineSkin;
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

/**
 * User: Antoine Mischler
 * Date: 09/04/11
 * Time: 21:44
 * FXForm demo
 */
public class Demo extends Application {

    public static void main(String[] args) {
        Application.launch(Demo.class, args);
    }

    @Override
    public void start(Stage stage) {
        VBox root = new VBox();
        root.setPadding(new Insets(10, 10, 10, 10));
        final FXForm fxForm = createFXForm();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Demo.class.getResource("style.css").toExternalForm());
        ChoiceBox<FXFormSkin> skinChoiceBox = new ChoiceBox<FXFormSkin>();
        skinChoiceBox.getItems().addAll(new DefaultSkin(fxForm), new InlineSkin(fxForm));
        skinChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<FXFormSkin>() {
            public void changed(ObservableValue<? extends FXFormSkin> observableValue, FXFormSkin fxFormSkin, FXFormSkin fxFormSkin1) {
                fxForm.setSkin(fxFormSkin1);
            }
        });
        root.getChildren().addAll(skinChoiceBox, fxForm);
        stage.setScene(scene);
        stage.setVisible(true);

    }

    private FXForm createFXForm() {
        DemoObject demoObject = new SubDemoObject();
        demoObject.setName("John Hudson");
        demoObject.setLetter(TestEnum.B);
        new ObjectPropertyObserver(demoObject);
        DelegateFactory.addGlobalFactory(new NamedFieldHandler("height"), new NodeFactory<ElementController>() {
            public Node createNode(ElementController controller) throws NodeCreationException {
                return new Button("Custom factory");
            }
        });
        FXForm<DemoObject> fxForm = new FXForm<DemoObject>(demoObject, new DelegateFactory());
        fxForm.setTitle("Dude, where is my form?");
        return fxForm;
    }
}
