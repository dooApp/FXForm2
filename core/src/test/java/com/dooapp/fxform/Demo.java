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

import javafx.application.Application;
import javafx.application.Launcher;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
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
        Launcher.launch(Demo.class, args);
    }

    @Override
    public void start(Stage stage) {
        VBox root = new VBox();
        root.setPadding(new Insets(10, 10, 10, 10));
        Node fxForm = createFXForm();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Demo.class.getResource("style.css").toExternalForm());
        root.getChildren().add(fxForm);
        stage.setScene(scene);
        stage.setVisible(true);

    }

    private FXForm createFXForm() {
        DemoObject demoObject = new DemoObject();
        demoObject.setName("John Hudson");
        new ObjectPropertyObserver(demoObject);
        FXForm fxForm = new FXForm(demoObject);
        fxForm.setTitle("Dude, where is my form?");
        return fxForm;
    }
}
