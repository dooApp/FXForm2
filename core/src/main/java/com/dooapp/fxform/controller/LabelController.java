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

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.adapter.Adapter;
import com.dooapp.fxform.adapter.AdapterException;
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.view.FXFormNode;
import com.dooapp.fxform.view.NodeType;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.StringProperty;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created at 27/09/12 15:45.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class LabelController extends NodeController {

    public final static Logger logger = Logger.getLogger(LabelController.class.getName());

    private final NodeType nodeType;

    public LabelController(FXForm fxForm, Element element, NodeType nodeType) {
        super(fxForm, element);
        this.nodeType = nodeType;
    }

    @Override
    protected void bind(FXFormNode fxFormNode) {
        final Adapter adapter = getFxForm().getAdapterProvider().getAdapter(StringProperty.class, getNode().getProperty().getClass(), getElement(), getNode());
        final StringProperty value = getFxForm().getResourceProvider().getString(getElement(), nodeType);
        fxFormNode.getProperty().bind(new ObjectBinding() {
            @Override
            protected Object computeValue() {
                {
                    bind(value);
                }

                try {
                    return adapter.adaptTo(value.get());
                } catch (AdapterException e) {
                    logger.log(Level.WARNING, e.getMessage(), e);
                }
                return null;
            }
        });
    }

}