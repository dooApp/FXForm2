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

package com.dooapp.fxform.controller;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.view.ElementNodes;
import com.dooapp.fxform.view.FXFormSkin;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Skin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 26/04/11
 * Time: 11:14
 * <p/>
 * The controller of an Element.
 */
public class ElementController<WrappedType> {

    private final static Logger logger = LoggerFactory.getLogger(ElementController.class);

    protected final Element<WrappedType> element;

    private final NodeController editorController;

    private final NodeController labelController;

    private final NodeController tooltipController;

    private final ChangeListener<Skin<?>> changeListener;

    private final FXForm fxForm;

    public ElementController(FXForm fxForm, Element element) {
        this.element = element;
        this.fxForm = fxForm;
        labelController = new LabelController(fxForm, element);
        tooltipController = new TooltipController(fxForm, element);
        editorController = createEditorController(fxForm, element);
        changeListener = new ChangeListener<Skin<?>>() {
            public void changed(ObservableValue<? extends Skin<?>> observableValue, Skin<?> skin, Skin<?> skin1) {
                updateSkin((FXFormSkin) skin1);
            }
        };
        fxForm.skinProperty().addListener(changeListener);
        updateSkin((FXFormSkin) fxForm.getSkin());
    }

    protected NodeController createEditorController(FXForm fxForm, Element element) {
        return new EditorController(fxForm, element);
    }

    private void updateSkin(FXFormSkin skin) {
        ElementNodes elementNodes = skin.getOrCreateElementNodes(element);
        editorController.setNode(elementNodes.getEditor());
        labelController.setNode(elementNodes.getLabel());
        tooltipController.setNode(elementNodes.getTooltip());
    }

    /**
     * Dispose this controller. The controller should clear all existing bindings.
     */
    public void dispose() {
        element.dispose();
        labelController.dispose();
        tooltipController.dispose();
        editorController.dispose();
        fxForm.skinProperty().removeListener(changeListener);
    }

    public Element getElement() {
        return element;
    }

}
