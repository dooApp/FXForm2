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

package com.dooapp.fxform.view;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.view.factory.AnnotationFactoryProvider;
import com.dooapp.fxform.view.factory.FactoryProvider;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.util.Callback;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 09/04/11
 * Time: 21:36
 * Skin of the FXForm control.
 */
public abstract class FXFormSkin implements Skin<FXForm> {

    private final static Logger logger = Logger.getLogger(FXFormSkin.class.getName());

    protected FXForm fxForm;

    private Node rootNode;

    private AnnotationFactoryProvider annotationFactoryProvider = new AnnotationFactoryProvider();

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
            if (label != null)
                label.getNode().getStyleClass().add(FXForm.LABEL_STYLE);
            if (editor != null)
                editor.getNode().getStyleClass().add(FXForm.EDITOR_STYLE);
            if (tooltip != null)
                tooltip.getNode().getStyleClass().add(FXForm.TOOLTIP_STYLE);
            if (constraint != null)
                constraint.getNode().getStyleClass().add(FXForm.CONSTRAINT_STYLE);
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

    protected FXFormNode createFXFormNode(Element element, FactoryProvider factoryProvider, String suffixId) {
        Callback<Void, FXFormNode> factory = factoryProvider.getFactory(element);
        if (factory == null) {
            logger.log(Level.WARNING, "No factory found for " + element + ", using " + factoryProvider + "\nCheck your factory provider.");
            Label label = new Label();
            return new FXFormNodeWrapper(label, label.textProperty());
        }
        FXFormNode fxFormNode = factory.call(null);
        fxFormNode.getNode().setId(element.getName() + suffixId);
        return fxFormNode;
    }

    protected FXFormNode createLabel(Element element) {
        return createFXFormNode(element, fxForm.getLabelFactoryProvider(), FXForm.LABEL_ID_SUFFIX);
    }

    protected FXFormNode createEditor(Element element) {
        Callback<Void, FXFormNode> factory = annotationFactoryProvider.getFactory(element);
        if (factory != null) {
            FXFormNode fxFormNode = factory.call(null);
            if (fxFormNode.getNode().getId() == null) {
                fxFormNode.getNode().setId(element.getName() + FXForm.EDITOR_ID_SUFFIX);
            }
            return fxFormNode;
        } else {
            return createFXFormNode(element, fxForm.getEditorFactoryProvider(), FXForm.EDITOR_ID_SUFFIX);
        }
    }

    protected FXFormNode createTooltip(Element element) {
        return createFXFormNode(element, fxForm.getTooltipFactoryProvider(), FXForm.TOOLTIP_ID_SUFFIX);
    }

    protected FXFormNode createConstraint(Element element) {
        return createFXFormNode(element, fxForm.getConstraintFactoryProvider(), FXForm.CONSTRAINT_ID_SUFFIX);
    }

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
