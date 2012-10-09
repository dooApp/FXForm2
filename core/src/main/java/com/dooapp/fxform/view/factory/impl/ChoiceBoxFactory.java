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

package com.dooapp.fxform.view.factory.impl;

import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.model.impl.ReadOnlyPropertyFieldElement;
import com.dooapp.fxform.reflection.ReflectionUtils;
import com.dooapp.fxform.view.FXFormNode;
import com.dooapp.fxform.view.property.ChoiceBoxDefaultProperty;
import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.util.Callback;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 17/04/11
 * Time: 00:19
 */
public class ChoiceBoxFactory implements Callback<Void, FXFormNode> {

    private final static Logger logger = Logger.getLogger(ChoiceBoxFactory.class.getName());

    public FXFormNode call(Void aVoid) {
        final ChoiceBox<Enum> choiceBox = new ChoiceBox<Enum>();
        return new FXFormNode() {

            private final Property property = new ChoiceBoxDefaultProperty(choiceBox);

            public Property getProperty() {
                return property;
            }

            public void init(Element element) {
                Enum[] constants = new Enum[0];
                try {
                    constants = (Enum[]) element.getGenericType().getEnumConstants();
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Could not retrieve enum constants from element " + element, e);
                }
                choiceBox.setItems(FXCollections.observableList(Arrays.asList(constants)));
                choiceBox.getSelectionModel().select((Enum) element.getValue());
            }

            public Node getNode() {
                return choiceBox;
            }

            public void dispose() {
                choiceBox.setItems(FXCollections.<Enum>emptyObservableList());
            }
        };
    }
}
