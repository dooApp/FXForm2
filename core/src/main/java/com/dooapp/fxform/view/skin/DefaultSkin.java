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

import com.dooapp.fxform.AbstractFXForm;
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.view.FXFormNode;
import com.dooapp.fxform.view.FXFormSkin;
import com.dooapp.fxform.view.NodeCreationException;
import com.dooapp.fxform.view.control.AutoHidableLabel;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

/**
 * A "vertical" skin.
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 11/05/11
 * Time: 21:56
 */
public class DefaultSkin extends FXFormSkin {

    public DefaultSkin(AbstractFXForm fxForm) {
        super(fxForm);
    }

    private Map<String, Pane> categoryBox = new HashMap<String, Pane>();

    private VBox contentBox;


    @Override
    protected Node createRootNode() throws NodeCreationException {
        VBox titleBox = new VBox();
        titleBox.getChildren().add(createTitleNode());
        contentBox = new VBox();
        contentBox.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
        contentBox.getStyleClass().add("form-content-box");
        titleBox.getChildren().add(contentBox);
        contentBox.setSpacing(5.0);
        contentBox.getChildren().addAll(createClassLevelConstraintNode());
        return titleBox;
    }

    @Override
    protected ElementNodes createElementNodes(Element element) {
        FXFormNode editor = createEditor(element);
        FXFormNode label = createLabel(element);
        FXFormNode constraint = createConstraint(element);
        FXFormNode tooltip = createTooltip(element);
        getCategoryBox(element.getCategory()).getChildren().addAll(label.getNode(), editor.getNode(), constraint.getNode(), tooltip.getNode());
        return new ElementNodes(label, editor, tooltip, constraint);
    }

    private Pane getCategoryBox(String category) {
        return categoryBox.get(category);
    }

    @Override
    protected void addCategory(String category) {
        Pane box = createCategoryBox(category);
        categoryBox.put(category, box);
        contentBox.getChildren().add(box);
    }

    protected Pane createCategoryBox(String category) {
        VBox vBox = new VBox();
        if (categoryBox.keySet().size() > 0) {
            vBox.getChildren().addAll(new Separator());
        }
        return vBox;
    }

    @Override
    protected void removeCategory(String category) {
        contentBox.getChildren().remove(getCategoryBox(category));
        categoryBox.remove(category);
    }

    @Override
    protected void deleteElementNodes(ElementNodes elementNodes) {
        for (Pane pane : categoryBox.values()) {
            pane.getChildren().removeAll(elementNodes.getConstraint().getNode(),
                    elementNodes.getEditor().getNode(),
                    elementNodes.getLabel().getNode(),
                    elementNodes.getTooltip().getNode());
        }
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
