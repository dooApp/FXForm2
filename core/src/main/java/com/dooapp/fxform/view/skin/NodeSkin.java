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
import com.dooapp.fxform.view.*;
import com.dooapp.fxform.view.control.ConstraintLabel;
import javafx.beans.property.Property;
import javafx.scene.Node;

import java.util.concurrent.Callable;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 26/11/2013
 * Time: 11:53
 */
public class NodeSkin extends FXFormSkin {

    private final System.Logger logger = System.getLogger(NodeSkin.class.getName());

    private Callable<Node> onCreateNode;

    protected NodeSkin(FXForm fxForm) {
        super(fxForm);
    }

    public NodeSkin(FXForm fxForm, Callable<Node> onCreateNode) {
        super(fxForm);
        setOnCreateNode(onCreateNode);
        buildNode();
    }

    @Override
    protected void buildNode() {
        if (onCreateNode != null) {
            super.buildNode();
        }
    }

    public void setOnCreateNode(Callable<Node> onCreateNode) {
        this.onCreateNode = onCreateNode;
    }

    @Override
    protected Node createRootNode() throws NodeCreationException {
        try {
            Node rootNode = onCreateNode.call();
            initClassLevelConstraintNode(rootNode);
            return rootNode;
        } catch (Exception e) {
            throw new NodeCreationException(e);
        }
    }

    protected void initClassLevelConstraintNode(Node rootNode) {
        ConstraintLabel constraintLabel = (ConstraintLabel) rootNode.lookup("#" + NodeType.GLOBAL_CONSTRAINT.getIdSuffix());
        if (constraintLabel != null) {
            constraintLabel.constraintProperty().bind(getSkinnable().getClassLevelValidator().constraintViolationsProperty());
        }
    }

    @Override
    protected ElementNodes createElementNodes(Element element) {
        FXFormNode label = getFXFormNode(element, NodeType.LABEL.getIdSuffix());
        FXFormNode editor = getFXFormNode(element, NodeType.EDITOR.getIdSuffix());
        FXFormNode tooltip = getFXFormNode(element, NodeType.TOOLTIP.getIdSuffix());
        FXFormNode constraint = getFXFormNode(element, NodeType.CONSTRAINT.getIdSuffix());
        return new ElementNodes(label, editor, tooltip, constraint);

    }

    @Override
    protected void deleteElementNodes(ElementNodes elementNodes) {
    }

    private FXFormNode getFXFormNode(Element element, String suffix) {
        Node node = lookupNode(element, suffix);
        if (node != null) {
            Property property = getSkinnable().getPropertyProvider().getProperty(node);
            if (property != null) {
                return new FXFormNodeWrapper(node, property);
            } else {
                logger.log(System.Logger.Level.WARNING, "Unable to find the property to bind for " + node + "\n" +
                        "Check that you configured the PropertyProvider correctly. See FXForm#setPropertyProvider");
                return null;
            }
        } else
            return null;
    }

    protected Node lookupNode(Element element, String suffix) {
        return getNode().lookup("#" + element.getName() + suffix);
    }

}
