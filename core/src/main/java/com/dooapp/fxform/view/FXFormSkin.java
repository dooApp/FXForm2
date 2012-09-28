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
import com.dooapp.fxform.model.Element;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 09/04/11
 * Time: 21:36
 * Skin of the FXForm control.
 */
public abstract class FXFormSkin implements Skin<FXForm> {

    private final Logger logger = LoggerFactory.getLogger(FXFormSkin.class);

    private final static Image WARNING = new Image(FXFormSkin.class.getResource("warning.png").toExternalForm());

    protected FXForm fxForm;

    private Node rootNode;

    protected static class ElementNodes {

        private final FXFormNode label;

        private final FXFormNode editor;

        private final FXFormNode tooltip;

        private final FXFormNode constraint;

        public ElementNodes(FXFormNode label, FXFormNode editor, FXFormNode tooltip, FXFormNode constraint) {
            this.label = label;
            this.editor = editor;
            this.tooltip = tooltip;
            this.constraint = constraint;
        }

        public FXFormNode getLabel() {
            return label;
        }

        public FXFormNode getEditor() {
            return editor;
        }

        public FXFormNode getTooltip() {
            return tooltip;
        }

        public FXFormNode getConstraint() {
            return constraint;
        }
    }


    public FXFormSkin(FXForm fxForm) {
        this.fxForm = fxForm;
    }

    protected abstract Node createRootNode() throws NodeCreationException;

    public Node getNode() {
        if (rootNode == null) {
            logger.debug("Creating skin node");
            try {
                rootNode = createRootNode();
            } catch (NodeCreationException e) {
                e.printStackTrace();
            }
        }
        return rootNode;
    }

    public FXFormNode getLabel(Element element) {
        if (!getNode().getProperties().containsKey(element)) {
            ElementNodes elementNodes = createElementNodes(element);
            getNode().getProperties().put(element, elementNodes);
        }
        return ((ElementNodes) getNode().getProperties().get(element)).getLabel();
    }


    public FXFormNode getTooltip(Element element) {
        if (!getNode().getProperties().containsKey(element)) {
            ElementNodes elementNodes = createElementNodes(element);
            getNode().getProperties().put(element, elementNodes);
        }
        return ((ElementNodes) getNode().getProperties().get(element)).getTooltip();
    }

    public FXFormNode getEditor(Element element) {
        if (!getNode().getProperties().containsKey(element)) {
            ElementNodes elementNodes = createElementNodes(element);
            getNode().getProperties().put(element, elementNodes);
        }
        return ((ElementNodes) getNode().getProperties().get(element)).getEditor();
    }

    public FXFormNode getConstraint(Element element) {
        if (!getNode().getProperties().containsKey(element)) {
            ElementNodes elementNodes = createElementNodes(element);
            getNode().getProperties().put(element, elementNodes);
        }
        return ((ElementNodes) getNode().getProperties().get(element)).getConstraint();
    }

    protected FXFormNode createLabel(Element element) {
        return fxForm.getLabelFactoryProvider().getFactory(element).call(null);
    }

    protected FXFormNode createEditor(Element element) {
        return fxForm.getEditorFactoryProvider().getFactory(element).call(null);
    }

    protected FXFormNode createTooltip(Element element) {
        return fxForm.getTooltipFactoryProvider().getFactory(element).call(null);
    }

    protected FXFormNode createConstraint(Element element) {
        return fxForm.getConstraintFactoryProvider().getFactory(element).call(null);
    }

    /**
     * protected FXFormNode createConstraintNode(final ElementController controller) {
     * final VBox constraintsBox = new VBox();
     * constraintsBox.setAlignment(Pos.CENTER_LEFT);
     * controller.getConstraintViolations().addListener(new ListChangeListener() {
     * public void onChanged(Change change) {
     * constraintsBox.getChildren().clear();
     * for (Object o : controller.getConstraintViolations()) {
     * ConstraintViolation constraintViolation = (ConstraintViolation) o;
     * Label errorLabel = new Label(constraintViolation.getMessage());
     * ImageView warningView = new ImageView(WARNING);
     * warningView.setFitHeight(15);
     * warningView.setPreserveRatio(true);
     * warningView.setSmooth(true);
     * errorLabel.setGraphic(warningView);
     * constraintsBox.getChildren().add(errorLabel);
     * }
     * }
     * });
     * return new FXFormNodeWrapper(constraintsBox);
     * }
     */

    public void dispose() {
        fxForm = null;
    }

    public FXForm getSkinnable() {
        return fxForm;
    }

    public void removeElement(Element element) {
        ElementNodes elementNodes = (ElementNodes) getNode().getProperties().get(element);
        if (elementNodes != null) {
            deleteElementNodes(elementNodes);
        }
        getNode().getProperties().remove(element);

    }

    protected abstract ElementNodes createElementNodes(Element element);

    protected abstract void deleteElementNodes(ElementNodes elementNodes);

}
