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

import com.dooapp.fxform.model.Element;
import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.util.Callback;

/**
 * Default implementation of FXFormNode providing some useful constructors.
 * <br>
 * Created at 17/10/11 15:54.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class FXFormNodeWrapper implements FXFormNode {

    private final Node node;

    private final Callback<Node, Void> callback;

    private final Property property;

    private boolean editable;

    public FXFormNodeWrapper(Node node, Property property) {
        this(node, property, new Callback<Node, Void>() {
            public Void call(Node node) {
                return null;
            }
        });
    }

    public FXFormNodeWrapper(Node node, Property property, Callback<Node, Void> callback) {
        this(node, property, callback, true);
    }

    public FXFormNodeWrapper(Node node, Property property, boolean editable) {
        this(node, property, new Callback<Node, Void>() {
            public Void call(Node node) {
                return null;
            }
        }, editable);
    }

    public FXFormNodeWrapper(Node node, Property property, Callback<Node, Void> callback, boolean editable) {
        this.node = node;
        this.callback = callback;
        this.property = property;
        this.editable = editable;
    }


    public void dispose() {
        callback.call(node);
    }

    public Node getNode() {
        return node;
    }

    public Property getProperty() {
        return property;
    }


    public void init(Element element) {
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public String toString() {
        return "FXFormNodeWrapper{" +
                "node=" + node +
                ", property=" + property +
                '}';
    }
}