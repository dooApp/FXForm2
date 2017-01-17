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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A skin where elements are inlined (label, editor). The tooltip is displayed under the editor.
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 24/08/11
 * Time: 11:03
 */
public class InlineSkin extends FXFormSkin {

    public InlineSkin(AbstractFXForm fxForm) {
        super(fxForm);
    }

    private Node createTitleNode() {
        Label label = new AutoHidableLabel();
        label.getStyleClass().add("form-title");
        label.textProperty().bind(getSkinnable().titleProperty());
        return label;
    }

    protected GridPane gridPane;
    protected int row = 0;
    private Map<String, Node> categoryNode = new HashMap<String, Node>();



    @Override
    protected Node createRootNode() throws NodeCreationException {
        VBox titleBox = new VBox();
        titleBox.getChildren().add(createTitleNode());
        VBox contentBox = new VBox();
        contentBox.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
        contentBox.getStyleClass().add("form-content-box");
        titleBox.getChildren().add(contentBox);
        contentBox.setSpacing(5.0);
        gridPane = new GridPane();
        gridPane.setHgap(5.0);
        gridPane.setVgap(5.0);
        contentBox.getChildren().addAll(createClassLevelConstraintNode(), gridPane);
        return titleBox;
    }

    @Override
    protected ElementNodes createElementNodes(Element element) {
        FXFormNode editor = createEditor(element);
        FXFormNode label = createLabel(element);
        FXFormNode constraint = createConstraint(element);
        FXFormNode tooltip = createTooltip(element);
        GridPane.setHgrow(editor.getNode(), Priority.SOMETIMES);
        gridPane.addRow(row++, label.getNode(), editor.getNode());
        gridPane.add(constraint.getNode(), 1, row++);
        gridPane.add(tooltip.getNode(), 1, row++);
        return new ElementNodes(label, editor, tooltip, constraint);
    }

    @Override
    protected void deleteElementNodes(ElementNodes elementNodes) {
        removeRow(GridPane.getRowIndex(elementNodes.getEditor().getNode()));
        removeRow(GridPane.getRowIndex(elementNodes.getTooltip().getNode()));
        removeRow(GridPane.getRowIndex(elementNodes.getConstraint().getNode()));
    }

    @Override
    protected void addCategory(String category) {
        if (categoryMap.keySet().size() > 1) {
            Node node = createCategoryNode(category);
            categoryNode.put(category, node);
            GridPane.setColumnSpan(node, 2);
            gridPane.add(node, 0, row++);
            row++;
        }
    }

    protected Node createCategoryNode(String category) {
        return new Separator();
    }

    @Override
    protected void removeCategory(String category) {
        if (categoryNode.containsKey(category)) {
            removeRow(GridPane.getRowIndex(categoryNode.get(category)));
            categoryNode.remove(category);
        }
    }


    /**
     * Remove a row by moving all nodes under this row one row up.
     *
     * @param row
     */
    protected void removeRow(int row) {
        // copy children to another list since we are going to iterate on it and modify the children list
        List<Node> children = new LinkedList<Node>(gridPane.getChildren());
        for (Node node : children) {
            int nodeRow = GridPane.getRowIndex(node);
            if (nodeRow == row) {
                gridPane.getChildren().remove(node);
            } else if (nodeRow > row) {
                GridPane.setRowIndex(node, nodeRow - 1);
            }
        }
        this.row--;
    }


    @Override
    public String toString() {
        return "Inline skin";
    }

}
