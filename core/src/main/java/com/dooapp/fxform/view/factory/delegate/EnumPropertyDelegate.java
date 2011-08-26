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

package com.dooapp.fxform.view.factory.delegate;

import com.dooapp.fxform.type.EnumProperty;
import com.dooapp.fxform.model.impl.WritableElementController;
import com.dooapp.fxform.view.NodeFactory;
import com.dooapp.fxform.view.NodeCreationException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;

import java.util.Arrays;

/**
 * User: Antoine Mischler
 * Date: 17/04/11
 * Time: 00:19
 */
public class EnumPropertyDelegate implements NodeFactory<WritableElementController<Enum>> {


    public Node createNode(WritableElementController<Enum> formFieldController) throws NodeCreationException {
        Object[] constants = ((EnumProperty) formFieldController.getFormField().getObservable()).getEnum().getEnumConstants();
        final ChoiceBox choiceBox = new ChoiceBox();
        choiceBox.setItems(FXCollections.observableList(Arrays.asList(constants)));
        choiceBox.getSelectionModel().select(formFieldController.getFormField().getObservable().getValue());
        choiceBox.getSelectionModel().selectedItemProperty().addListener(formFieldController);
        formFieldController.getFormField().getObservable().addListener(new ChangeListener() {
            public void changed(ObservableValue observableValue, Object o, Object o1) {
                choiceBox.getSelectionModel().select(observableValue.getValue());
            }
        });
        return choiceBox;
    }
}
