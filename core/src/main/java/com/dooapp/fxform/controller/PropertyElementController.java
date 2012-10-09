/*
 * Copyright (c) 2012, dooApp <contact@dooapp.com>
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
import com.dooapp.fxform.model.PropertyElement;
import com.dooapp.fxform.view.FXFormSkin;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;

/**
 * Created at 27/09/12 13:51.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class PropertyElementController<WrappedType> extends ElementController<WrappedType> {

    protected ObservableList<ConstraintViolation> constraintViolations;

    private final NodeController constraintController;

    public PropertyElementController(FXForm fxForm, PropertyElement element) {
        super(fxForm, element);
        constraintController = new ConstraintController(fxForm, element, constraintViolations);
        updateSkin((FXFormSkin) fxForm.getSkin());
        constraintViolations.addListener(new ListChangeListener<ConstraintViolation>() {

            @Override
            public void onChanged(Change<? extends ConstraintViolation> change) {
                if (constraintViolations.size() > 0) {
                    if (labelController.getNode() != null)
                        labelController.getNode().getNode().getStyleClass().add(FXForm.LABEL_STYLE + FXForm.INVALID_STYLE);
                    if (editorController.getNode() != null)
                        editorController.getNode().getNode().getStyleClass().add(FXForm.EDITOR_STYLE + FXForm.INVALID_STYLE);
                    if (tooltipController.getNode() != null)
                        tooltipController.getNode().getNode().getStyleClass().add(FXForm.TOOLTIP_STYLE + FXForm.INVALID_STYLE);
                } else {
                    if (labelController.getNode() != null)
                        labelController.getNode().getNode().getStyleClass().remove(FXForm.LABEL_STYLE + FXForm.INVALID_STYLE);
                    if (editorController.getNode() != null)
                        editorController.getNode().getNode().getStyleClass().remove(FXForm.EDITOR_STYLE + FXForm.INVALID_STYLE);
                    if (tooltipController.getNode() != null)
                        tooltipController.getNode().getNode().getStyleClass().remove(FXForm.TOOLTIP_STYLE + FXForm.INVALID_STYLE);
                }
            }
        });
    }

    public ObservableList<ConstraintViolation> getConstraintViolations() {
        return constraintViolations;
    }

    @Override
    protected NodeController createEditorController(FXForm fxForm, Element element) {
        constraintViolations = FXCollections.observableArrayList(new ArrayList<ConstraintViolation>());
        return new PropertyEditorController(fxForm, element, constraintViolations);
    }

    @Override
    protected void updateSkin(FXFormSkin skin) {
        super.updateSkin(skin);
        if (constraintController != null)
            constraintController.setNode(skin.getConstraint(element));
    }
}