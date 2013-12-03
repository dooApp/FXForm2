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

package com.dooapp.fxform.controller;

import com.dooapp.fxform.AbstractFXForm;
import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.utils.Disposable;
import com.dooapp.fxform.view.FXFormNode;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * Created at 27/09/12 15:33.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public abstract class NodeController implements Disposable {

    private final ObjectProperty<FXFormNode> node = new SimpleObjectProperty<FXFormNode>();
    private final ChangeListener<FXFormNode> changeListener;
    private final Element element;
    private final AbstractFXForm fxForm;

    public NodeController(AbstractFXForm fxForm, Element element) {
        this.element = element;
        this.fxForm = fxForm;
        changeListener = new ChangeListener<FXFormNode>() {
            public void changed(ObservableValue<? extends FXFormNode> observableValue, FXFormNode fxFormNode, FXFormNode fxFormNode1) {
                if (fxFormNode != null) {
                    unbind(fxFormNode);
                    fxFormNode.dispose();
                }
                if (fxFormNode1 != null) {
                    fxFormNode1.init(NodeController.this.element);
                    bind(fxFormNode1);
                }
            }
        };
        node.addListener(changeListener);
    }

    protected abstract void bind(FXFormNode fxFormNode);

    protected void unbind(FXFormNode fxFormNode) {
        fxFormNode.getProperty().unbind();
    }

    public void dispose() {
        setNode(null);
        node.removeListener(changeListener);
    }

    public FXFormNode getNode() {
        return node.get();
    }

    public void setNode(FXFormNode node1) {
        node.set(node1);
    }

    public ObjectProperty<FXFormNode> nodeProperty() {
        return node;
    }

    protected Element getElement() {
        return element;
    }

    protected AbstractFXForm getFxForm() {
        return fxForm;
    }
}