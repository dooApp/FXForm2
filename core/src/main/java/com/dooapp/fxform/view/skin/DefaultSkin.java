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

package com.dooapp.fxform.view.skin;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.model.ElementController;
import com.dooapp.fxform.view.FXFormSkin;
import com.dooapp.fxform.view.NodeCreationException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * User: Antoine Mischler
 * Date: 11/05/11
 * Time: 21:56
 */
public class DefaultSkin extends FXFormSkin {

    public DefaultSkin(FXForm fxForm) {
        super(fxForm);
    }

    private VBox controllerBox;

    @Override
    protected Node createRootNode() throws NodeCreationException {
        VBox titleBox = new VBox();
        titleBox.getChildren().add(createTitleNode());
        final VBox contentBox = new VBox();
        contentBox.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
        contentBox.getStyleClass().add("form-content-box");
        titleBox.getChildren().add(contentBox);
        contentBox.setSpacing(5.0);
        controllerBox = new VBox();
        contentBox.getChildren().addAll(controllerBox);
        return titleBox;
    }

    protected void addControllers(List<ElementController> controllers) {
        for (final ElementController controller : controllers) {
            Node editor = getEditor(controller);
            final Node label = getLabel(controller);
            controller.dirty().addListener(new ChangeListener<Boolean>() {
                public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean1) {
                    label.setOpacity(0.5);
                }
            });
            controllerBox.getChildren().addAll(label, editor, getConstraint(controller));
            if (controller.getTooltip() != null) {
                Node node = getTooltip(controller);
                controllerBox.getChildren().add(node);
            }
        }
    }

    protected void removeControllers(List<ElementController> controllers) {
        for (ElementController controller : controllers) {
            controllerBox.getChildren().removeAll(getLabel(controller), getEditor(controller), getTooltip(controller), getConstraint(controller));
        }
    }

    private Node createTitleNode() {
        Label label = new Label();
        label.getStyleClass().add("form-title");
        label.textProperty().bind(fxForm.titleProperty());
        return label;
    }

    @Override
    public String toString() {
        return "Default skin";
    }

}
