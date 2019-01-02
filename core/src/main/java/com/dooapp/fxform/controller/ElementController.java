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
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.view.FXFormSkin;
import com.dooapp.fxform.view.NodeType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Skin;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 26/04/11
 * Time: 11:14
 * <p/>
 * The controller of an Element.
 */
public class ElementController<WrappedType> {

    protected final Element<WrappedType> element;

    protected final NodeController editorController;

    protected final NodeController labelController;

    protected final NodeController tooltipController;

    private final ChangeListener<Skin<?>> changeListener;

    protected final AbstractFXForm fxForm;

    public ElementController(AbstractFXForm fxForm, Element element) {
        this.element = element;
        this.fxForm = fxForm;
        labelController = new LabelController(fxForm, element, NodeType.LABEL);
        tooltipController = new LabelController(fxForm, element, NodeType.TOOLTIP);
        editorController = createEditorController(fxForm, element);
        changeListener = new ChangeListener<Skin<?>>() {
            public void changed(ObservableValue<? extends Skin<?>> observableValue, Skin<?> skin, Skin<?> skin1) {
                updateSkin((FXFormSkin) skin1);
            }
        };
        fxForm.skinProperty().addListener(changeListener);
        updateSkin((FXFormSkin) fxForm.getSkin());
    }

    protected NodeController createEditorController(AbstractFXForm fxForm, Element element) {
        return new ReadOnlyPropertyEditorController(fxForm, element);
    }

    protected void updateSkin(FXFormSkin skin) {
        editorController.setNode(skin.getEditor(element));
        labelController.setNode(skin.getLabel(element));
        tooltipController.setNode(skin.getTooltip(element));
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
        ((FXFormSkin) fxForm.getSkin()).removeElement(element);
    }

    public Element getElement() {
        return element;
    }

    public NodeController getEditorController() {
        return editorController;
    }

    public NodeController getLabelController() {
        return labelController;
    }

    public NodeController getTooltipController() {
        return tooltipController;
    }
}
