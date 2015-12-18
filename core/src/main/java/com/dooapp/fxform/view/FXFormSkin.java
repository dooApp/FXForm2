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

import com.dooapp.fxform.AbstractFXForm;
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.view.control.ConstraintLabel;
import com.dooapp.fxform.view.factory.AnnotationFactoryProvider;
import com.dooapp.fxform.view.factory.FactoryProvider;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.util.Callback;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 09/04/11
 * Time: 21:36
 * Skin of the FXForm control.
 */
public abstract class FXFormSkin extends SkinBase<AbstractFXForm> implements Skin<AbstractFXForm> {

    private final static Logger logger = Logger.getLogger(FXFormSkin.class.getName());

    private Node rootNode;

    private AnnotationFactoryProvider annotationFactoryProvider = new AnnotationFactoryProvider();

    protected Map<String, List<Element>> categoryMap = new HashMap<String, List<Element>>();

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
                label.getNode().getStyleClass().add(NodeType.LABEL.getStyle());
            if (editor != null)
                editor.getNode().getStyleClass().add(NodeType.EDITOR.getStyle());
            if (tooltip != null)
                tooltip.getNode().getStyleClass().add(NodeType.TOOLTIP.getStyle());
            if (constraint != null)
                constraint.getNode().getStyleClass().add(NodeType.CONSTRAINT.getStyle());
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


    public FXFormSkin(AbstractFXForm fxForm) {
        super(fxForm);
        buildNode();
    }

    protected void buildNode() {
        try {
            rootNode = createRootNode();
            if (rootNode != null) {
                getChildren().add(rootNode);
            }
        } catch (NodeCreationException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    protected abstract Node createRootNode() throws NodeCreationException;

    public FXFormNode getLabel(Element element) {
        initElementNodes(element);
        return ((ElementNodes) rootNode.getProperties().get(element)).getLabel();
    }

    public FXFormNode getTooltip(Element element) {
        initElementNodes(element);
        return ((ElementNodes) rootNode.getProperties().get(element)).getTooltip();
    }

    public FXFormNode getEditor(Element element) {
        initElementNodes(element);
        return ((ElementNodes) rootNode.getProperties().get(element)).getEditor();
    }

    public FXFormNode getConstraint(Element element) {
        initElementNodes(element);
        return ((ElementNodes) rootNode.getProperties().get(element)).getConstraint();
    }

    private void initElementNodes(Element element) {
        if (!rootNode.getProperties().containsKey(element)) {
            if (!categoryMap.containsKey(element.getCategory())) {
                categoryMap.put(element.getCategory(), new LinkedList<Element>());
                addCategory(element.getCategory());
            }
            categoryMap.get(element.getCategory()).add(element);
            ElementNodes elementNodes = createElementNodes(element);
            rootNode.getProperties().put(element, elementNodes);
        }
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
        return createFXFormNode(element, getSkinnable().getLabelFactoryProvider(), NodeType.LABEL.getIdSuffix());
    }

    protected FXFormNode createEditor(Element element) {
        Callback<Void, FXFormNode> factory = annotationFactoryProvider.getFactory(element);
        if (factory != null) {
            FXFormNode fxFormNode = factory.call(null);
            if (fxFormNode.getNode().getId() == null) {
                fxFormNode.getNode().setId(element.getName() + NodeType.EDITOR.getIdSuffix());
            }
            return fxFormNode;
        } else {
            return createFXFormNode(element, getSkinnable().getEditorFactoryProvider(), NodeType.EDITOR.getIdSuffix());
        }
    }

    protected FXFormNode createTooltip(Element element) {
        return createFXFormNode(element, getSkinnable().getTooltipFactoryProvider(), NodeType.TOOLTIP.getIdSuffix());
    }

    protected FXFormNode createConstraint(Element element) {
        return createFXFormNode(element, getSkinnable().getConstraintFactoryProvider(), NodeType.CONSTRAINT.getIdSuffix());
    }

    @Override
    public void dispose() {
        getChildren().remove(rootNode);
        removeAllElements();
        rootNode = null;
        super.dispose();
    }

    protected void removeAllElements() {
        List<Object> keys = new LinkedList<>(rootNode.getProperties().keySet());
        keys.stream()
                .forEach(key -> {
                    if (key instanceof Element) {
                        removeElement((Element) key);
                    }
                });
    }

    public void removeElement(Element element) {
        ElementNodes elementNodes = (ElementNodes) rootNode.getProperties().get(element);
        categoryMap.get(element.getCategory()).remove(element);
        if (categoryMap.get(element.getCategory()).size() == 0) {
            categoryMap.remove(element.getCategory());
            removeCategory(element.getCategory());
        }
        if (elementNodes != null) {
            deleteElementNodes(elementNodes);
        }
        rootNode.getProperties().remove(element);
    }

    protected abstract ElementNodes createElementNodes(Element element);

    protected abstract void deleteElementNodes(ElementNodes elementNodes);

    /**
     * A new category is used in the form. A skin could create some specific nodes here, such as a separator.
     *
     * @param category
     */
    protected void addCategory(String category) {
    }

    /**
     * This category is not used anymore in the form, so remove anything related to this category.
     *
     * @param category
     */
    protected void removeCategory(String category) {
    }

    protected Node createClassLevelConstraintNode() {
        ConstraintLabel constraintLabel = new ConstraintLabel();
        constraintLabel.getStyleClass().add(NodeType.CONSTRAINT.getStyle());
        constraintLabel.constraintProperty().bind(getSkinnable().getClassLevelValidator().constraintViolationsProperty());
        return constraintLabel;
    }

    /**
     * Fix for : Form min height is not respected when the width of the parent causes the tooltips to be wrapped
     * on multiple line #115
     * <p/>
     * Can be removed when https://bugs.openjdk.java.net/browse/JDK-8144128 is resolved
     */
    @Override
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        double minY = 0;
        double maxY = 0;
        boolean firstManagedChild = true;
        for (int i = 0; i < getChildren().size(); i++) {
            Node node = getChildren().get(i);
            if (node.isManaged()) {
                final double y = node.getLayoutBounds().getMinY() + node.getLayoutY();
                if (!firstManagedChild) {
                    minY = Math.min(minY, y);
                    maxY = Math.max(maxY, y + node.minHeight(width));
                } else {
                    minY = y;
                    maxY = y + node.minHeight(width);
                    firstManagedChild = false;
                }
            }
        }
        double minHeight = maxY - minY;
        return topInset + minHeight + bottomInset;
    }

}
