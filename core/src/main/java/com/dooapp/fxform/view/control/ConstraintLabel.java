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

package com.dooapp.fxform.view.control;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;

/**
 * Created at 28/09/12 17:52.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class ConstraintLabel extends VBox {

    private final static Image WARNING = new Image(ConstraintLabel.class.getResource("warning.png").toExternalForm());

    private ListProperty<ConstraintViolation> constraint = new SimpleListProperty<ConstraintViolation>();

    private final ListChangeListener<ConstraintViolation> listChangeListener = change -> {
        while (change.next()) {
            if (Platform.isFxApplicationThread()) {
                updateChildren();
            } else {
                Platform.runLater(() -> updateChildren());
            }
        }
    };

    public ConstraintLabel() {
        setAlignment(Pos.CENTER_LEFT);
        constraint.addListener(listChangeListener);
    }

    public ListProperty<ConstraintViolation> constraintProperty() {
        return constraint;
    }

    private void updateChildren() {
        getChildren().clear();
        List<ConstraintViolation> constraintViolations = new ArrayList(constraint.get());
        for (ConstraintViolation constraintViolation : constraintViolations) {
            Label errorLabel = new Label(constraintViolation.getMessage());
            if (constraintViolation.getConstraintDescriptor() != null) {
                errorLabel.getStyleClass().add(constraintViolation.getConstraintDescriptor().getAnnotation().getClass().getName());
            }
            errorLabel.setWrapText(true);
            errorLabel.setMinHeight(Region.USE_PREF_SIZE);
            ImageView warningView = new ImageView(WARNING);
            warningView.setFitHeight(15);
            warningView.setPreserveRatio(true);
            warningView.setSmooth(true);
            errorLabel.setGraphic(warningView);
            getChildren().add(errorLabel);
        }
    }

}
