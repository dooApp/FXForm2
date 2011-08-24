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

package com.dooapp.fxform.view.skin;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.model.FormFieldController;
import com.dooapp.fxform.view.FXFormSkin;
import com.dooapp.fxform.view.NodeCreationException;
import javafx.builders.GridPaneBuilder;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * User: antoine
 * Date: 24/08/11
 * Time: 11:03
 */
public class InlineSkin extends FXFormSkin {

    private final static Image WARNING = new Image(DefaultSkin.class.getResource("warning.png").toExternalForm());

    public InlineSkin(FXForm fxForm) {
        super(fxForm);
    }

    private Node createTitleNode() {
        Label label = new Label();
        label.getStyleClass().add("form-title");
        label.textProperty().bind(fxForm.titleProperty());
        return label;
    }

    @Override
    protected Node createRootNode() throws NodeCreationException {
        VBox titleBox = new VBox();
        titleBox.getChildren().add(createTitleNode());
        VBox contentBox = new VBox();
        contentBox.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
        contentBox.getStyleClass().add("form-content-box");
        titleBox.getChildren().add(contentBox);
        contentBox.setSpacing(5.0);
        GridPane gridPane = GridPaneBuilder.create().build();
        int row = 0;
        for (final Object o : fxForm.getElements()) {
            FormFieldController controller = (FormFieldController) o;
            gridPane.addRow(row, createLabel(controller), createEditor(controller));
            row++;
        }
        contentBox.getChildren().add(gridPane);
        return titleBox;
    }

    @Override
    public String toString() {
        return "Inline skin";
    }

}
