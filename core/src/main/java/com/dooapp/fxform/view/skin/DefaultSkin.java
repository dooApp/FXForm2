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

package com.dooapp.fxform.view.skin;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.view.FXFormNode;
import com.dooapp.fxform.view.FXFormSkin;
import com.dooapp.fxform.view.NodeCreationException;
import com.dooapp.fxform.view.control.AutoHidableLabel;
import com.dooapp.fxform.view.control.ConstraintLabel;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * A "vertical" skin.
 * User: Antoine Mischler <antoine@dooapp.com>
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
        ConstraintLabel constraintLabel = new ConstraintLabel();
        constraintLabel.constraintProperty().bind(fxForm.getClassLevelValidator().constraintViolationsProperty());
        controllerBox = new VBox();
        contentBox.getChildren().addAll(constraintLabel, controllerBox);
        return titleBox;
    }

    @Override
    protected ElementNodes createElementNodes(Element element) {
        FXFormNode editor = createEditor(element);
        FXFormNode label = createLabel(element);
        FXFormNode constraint = createConstraint(element);
        FXFormNode tooltip = createTooltip(element);
        controllerBox.getChildren().addAll(label.getNode(), editor.getNode(), constraint.getNode());
        controllerBox.getChildren().add(tooltip.getNode());
        return new ElementNodes(label, editor, tooltip, constraint);
    }

    @Override
    protected void deleteElementNodes(ElementNodes elementNodes) {
        controllerBox.getChildren().removeAll(elementNodes.getConstraint().getNode(),
                elementNodes.getEditor().getNode(),
                elementNodes.getLabel().getNode(),
                elementNodes.getTooltip().getNode());
    }


    private Node createTitleNode() {
        Label label = new AutoHidableLabel();
        label.getStyleClass().add("form-title");
        label.textProperty().bind(fxForm.titleProperty());
        return label;
    }

    @Override
    public String toString() {
        return "Default skin";
    }

}
