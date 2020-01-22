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
import com.dooapp.fxform.adapter.Adapter;
import com.dooapp.fxform.adapter.AdapterException;
import com.dooapp.fxform.adapter.AnnotationAdapterProvider;
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.view.FXFormNode;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;


/**
 * Created at 27/09/12 17:15.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class ReadOnlyPropertyEditorController extends NodeController {

    public final static System.Logger logger = System.getLogger(ReadOnlyPropertyEditorController.class.getName());

    private ChangeListener changeListener;
    private WeakChangeListener weakChangeListener;

    private final AnnotationAdapterProvider annotationAdapterProvider = new AnnotationAdapterProvider();

    public ReadOnlyPropertyEditorController(AbstractFXForm fxForm, Element element) {
        super(fxForm, element);
    }

    @Override
    protected void bind(final FXFormNode fxFormNode) {
        if (fxFormNode.isEditable()) {
            fxFormNode.getNode().setDisable(true);
        }
        changeListener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object o2) {
                if (Platform.isFxApplicationThread()) {
                    updateView(fxFormNode);
                } else {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            updateView(fxFormNode);
                        }
                    });
                }
            }
        };
        weakChangeListener = new WeakChangeListener(changeListener);
        getElement().addListener(weakChangeListener);
        updateView(fxFormNode);

    }

    private void updateView(FXFormNode fxFormNode) {
        try {
            Adapter adapter = annotationAdapterProvider.getAdapter(getElement().getType(), getNode().getProperty().getClass(), getElement(), getNode());
            if (adapter == null) {
                adapter = getFxForm().getAdapterProvider().getAdapter(getElement().getType(), getNode().getProperty().getClass(), getElement(), getNode());
            }
            fxFormNode.getProperty().setValue(adapter.adaptTo(getElement().getValue()));
        } catch (AdapterException e) {
            logger.log(System.Logger.Level.TRACE, e.getMessage(), e);
        }
    }

    @Override
    protected void unbind(FXFormNode fxFormNode) {
        getElement().removeListener(weakChangeListener);
    }
}
