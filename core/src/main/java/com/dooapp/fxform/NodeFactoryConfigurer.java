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

package com.dooapp.fxform;

import com.dooapp.fxform.model.ElementController;
import com.dooapp.fxform.utils.Configurer;
import com.dooapp.fxform.view.NodeCreationException;
import com.dooapp.fxform.view.NodeFactory;
import javafx.scene.Node;

/**
 * User: antoine
 * Date: 25/08/11
 * Time: 18:31
 */
public abstract class NodeFactoryConfigurer implements Configurer<ElementController> {

    private final NodeFactory nodeFactory;

    private final String idSuffix;

    private final String style;

    public NodeFactoryConfigurer(NodeFactory nodeFactory, String idSuffix, String style) {
        this.nodeFactory = nodeFactory;
        this.idSuffix = idSuffix;
        this.style = style;
    }

    protected NodeFactory createNodeFactory() {
        return new NodeFactory<ElementController>() {
            public Node createNode(ElementController controller) throws NodeCreationException {
                Node node = nodeFactory.createNode(controller);
                node.setId(controller.getFormField().getField().getName() + idSuffix);
                node.getStyleClass().add(style);
                return node;
            }
        };
    }

    public void unconfigure(ElementController toUnconfigure) {
        toUnconfigure.setLabelFactory(null);
    }
}
