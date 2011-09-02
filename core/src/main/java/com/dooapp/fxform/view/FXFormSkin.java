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

package com.dooapp.fxform.view;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.model.ElementController;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * User: Antoine Mischler
 * Date: 09/04/11
 * Time: 21:36
 * Skin of the FXForm control.
 */
public abstract class FXFormSkin implements Skin<FXForm> {

    private final Logger logger = LoggerFactory.getLogger(FXFormSkin.class);

    private final static Image WARNING = new Image(FXFormSkin.class.getResource("warning.png").toExternalForm());

    protected FXForm fxForm;
    private Node rootNode;

    private final Map<ElementController, Node> labelMap = new HashMap<ElementController, Node>();
    private final Map<ElementController, Node> tooltipMap = new HashMap<ElementController, Node>();
    private final Map<ElementController, Node> editorMap = new HashMap<ElementController, Node>();
    private final Map<ElementController, Node> constraintMap = new HashMap<ElementController, Node>();

    public FXFormSkin(FXForm fxForm) {
        this.fxForm = fxForm;
    }

    protected abstract Node createRootNode() throws NodeCreationException;

    public Node getNode() {
        if (rootNode == null) {
            logger.info("Creating skin node");
            try {
                rootNode = createRootNode();
                addControllers(fxForm.getControllers());
                fxForm.getControllers().addListener(new ListChangeListener() {
                    public void onChanged(Change change) {
                        logger.info("Updating controllers view");
                        while (change.next()) {
                            addControllers(change.getAddedSubList());
                            if (change.wasRemoved()) {
                                removeControllers(change.getRemoved());
                                unregisterControllers(change.getRemoved());
                            }
                        }
                    }
                });
            } catch (NodeCreationException e) {
                e.printStackTrace();
            }
        }
        return rootNode;
    }

    private void unregisterControllers(List<ElementController> removed) {
        logger.info("Clearing controllers nodes");
        for (ElementController controller : removed) {
            labelMap.remove(controller);
            editorMap.remove(controller);
            tooltipMap.remove(controller);
        }
    }

    protected abstract void removeControllers(List<ElementController> removed);

    protected abstract void addControllers(List<ElementController> addedSubList);

    protected Node getLabel(ElementController controller) {
        if (!labelMap.containsKey(controller)) {
            try {
                labelMap.put(controller, controller.getLabelFactory().createNode(controller));
            } catch (NodeCreationException e) {
                e.printStackTrace();
            }
        }
        return labelMap.get(controller);
    }

    protected Node getTooltip(ElementController controller) {
        if (!tooltipMap.containsKey(controller)) {
            try {
                tooltipMap.put(controller, controller.getTooltipFactory().createNode(controller));
            } catch (NodeCreationException e) {
                e.printStackTrace();
            }
        }
        return tooltipMap.get(controller);
    }

    protected Node getEditor(ElementController controller) {
        if (!editorMap.containsKey(controller)) {
            try {
                editorMap.put(controller, controller.getEditorFactory().createNode(controller));
            } catch (NodeCreationException e) {
                e.printStackTrace();
            }
        }
        return editorMap.get(controller);
    }

    protected Node getConstraint(ElementController controller) {
        if (!constraintMap.containsKey(controller)) {
            // maybe we should use a factory here too
            constraintMap.put(controller, createConstraintNode(controller));
        }
        return constraintMap.get(controller);
    }

    protected Node createConstraintNode(final ElementController controller) {
        final VBox constraintsBox = new VBox();
        constraintsBox.setAlignment(Pos.CENTER_LEFT);
        controller.getConstraintViolations().addListener(new ListChangeListener() {
            public void onChanged(Change change) {
                constraintsBox.getChildren().clear();
                for (Object o : controller.getConstraintViolations()) {
                    ConstraintViolation constraintViolation = (ConstraintViolation) o;
                    Label errorLabel = new Label(constraintViolation.getMessage());
                    ImageView warningView = new ImageView(WARNING);
                    warningView.setFitHeight(15);
                    warningView.setPreserveRatio(true);
                    warningView.setSmooth(true);
                    errorLabel.setGraphic(warningView);
                    constraintsBox.getChildren().add(errorLabel);
                }
            }
        });
        return constraintsBox;
    }

    public void dispose() {
        logger.info("Disposing skin");
        fxForm = null;
    }

    public FXForm getSkinnable() {
        return fxForm;
    }

}
