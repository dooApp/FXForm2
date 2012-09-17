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
import com.dooapp.fxform.model.ElementController;
import com.dooapp.fxform.view.FXFormSkin;
import com.dooapp.fxform.view.NodeCreationException;
import com.dooapp.fxform.view.factory.AutoHidableLabel;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.GridPaneBuilder;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * A skin where elements are inlined (label, editor). The tooltip is displayed under the editor.
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 24/08/11
 * Time: 11:03
 */
public class InlineSkin extends FXFormSkin {

    public final static Logger logger = LoggerFactory.getLogger(InlineSkin.class);

    public InlineSkin(FXForm fxForm) {
        super(fxForm);
    }

    private Node createTitleNode() {
        Label label = new AutoHidableLabel();
        label.getStyleClass().add("form-title");
        label.textProperty().bind(fxForm.titleProperty());
        return label;
    }

    protected GridPane gridPane;
    protected int row = 0;

    @Override
    protected Node createRootNode() throws NodeCreationException {
        VBox titleBox = new VBox();
        titleBox.getChildren().add(createTitleNode());
        VBox contentBox = new VBox();
        contentBox.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
        contentBox.getStyleClass().add("form-content-box");
        titleBox.getChildren().add(contentBox);
        contentBox.setSpacing(5.0);
        gridPane = GridPaneBuilder.create().hgap(5.0).vgap(5.0).build();
        contentBox.getChildren().add(gridPane);
        return titleBox;
    }

    @Override
    protected void removeControllers(List<ElementController> removed) {
        for (ElementController controller : removed) {
            removeRow(GridPane.getRowIndex(getEditor(controller)));
            removeRow(GridPane.getRowIndex(getTooltip(controller)));
        }
    }

    @Override
    protected void addControllers(List<ElementController> addedSubList) {
        for (final ElementController controller : addedSubList) {
            GridPane.setHgrow(getEditor(controller), Priority.SOMETIMES);
            gridPane.addRow(row, getLabel(controller), getEditor(controller), getConstraint(controller));
            gridPane.add(getTooltip(controller), 1, ++row);
            row++;
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
