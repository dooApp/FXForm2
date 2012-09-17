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

import com.dooapp.fxform.model.PropertyElementController;
import com.dooapp.fxform.view.NodeCreationException;
import com.dooapp.fxform.view.factory.DisposableNode;
import com.dooapp.fxform.view.factory.DisposableNodeWrapper;
import com.dooapp.fxform.view.factory.NodeFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.util.Callback;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 16/04/11
 * Time: 23:28
 */
public class StringPropertyDelegate implements NodeFactory<PropertyElementController<String>> {

    public DisposableNode createNode(final PropertyElementController<String> controller) throws NodeCreationException {
        final TextField text = new TextField();
        String value = controller.getValue();
        if (value != null) {
            text.setText(value);
        }
        final ChangeListener textPropertyListener = new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observableValue, String s, String s1) {
                controller.setValue(s1);
            }
        };
        text.textProperty().addListener(textPropertyListener);
        final ChangeListener<String> controllerListener = new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observableValue, String s, String s1) {
                if (s1 != null) {
                    if (!text.getText().equals(s1)) {
                        text.setText(s1);
                    }
                } else {
                    text.setText("");
                }
            }
        };
        controller.addListener(controllerListener);

        // TODO Try/Catch will be removed once 2.0.2 is released (http://javafx-jira.kenai.com/browse/RT-17280)
        try {
            text.promptTextProperty().bind(controller.getPromptText());
        } catch (Exception e) {
        }

        return new DisposableNodeWrapper(text, new Callback<Node, Void>() {
            public Void call(Node node) {
                text.textProperty().removeListener(textPropertyListener);
                controller.removeListener(controllerListener);
                return null;
            }
        });
    }
}
