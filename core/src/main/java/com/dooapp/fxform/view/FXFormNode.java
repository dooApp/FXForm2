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
import javafx.beans.property.Property;
import javafx.scene.Node;

/**
 * This interface is used the enrich a Node with additional information.<br>
 * <br>
 * Created at 17/10/11 15:53.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public interface FXFormNode<N extends Node> extends DisposableNode<N> {

    /**
     * Get the property of the view to bind to the model.
     *
     * @return
     */
    public Property getProperty();

    /**
     * This method is called before the node is bound to the model if initialization is required.
     *
     * @param element
     */
    public void init(Element element, AbstractFXForm fxForm);

    /**
     * This boolean indicate whether this node allow the user to input values.
     * This is used by FXForm to know whether the node should be disabled in some case, e.g.
     * when bound to a read-only model element.
     *
     * @return true is the node is editable, i.e. the user can use it to input values
     */
    public boolean isEditable();

}