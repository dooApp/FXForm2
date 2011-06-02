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

package com.dooapp.fxform.view.delegate;

import com.dooapp.fxform.model.impl.ObservableAndWritableFormFieldController;
import com.dooapp.fxform.view.EditorFactory;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.InvalidationListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TextBox;

import java.text.NumberFormat;
import java.text.ParseException;

/**
 * User: Antoine Mischler
 * Date: 17/04/11
 * Time: 17:31
 */
public abstract class AbstractNumberPropertyDelegate<T extends Number> implements EditorFactory<ObservableAndWritableFormFieldController<T>> {

    protected ObjectProperty<T> numberProperty = new ObjectProperty<T>();

    public Node createNode(final ObservableAndWritableFormFieldController<T> controller) {
        final TextBox textBox = new TextBox();
        textBox.textProperty().addListener(new InvalidationListener<String>() {
            public void invalidated(ObservableValue<? extends String> observableValue) {
                try {
                    Number parsed = parse(textBox.getText());
                    numberProperty.setValue((T) parsed);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        controller.getFormField().getObservable().addListener(new InvalidationListener() {
            public void invalidated(ObservableValue observableValue) {
                textBox.textProperty().setValue(getFormat().format(controller.getFormField().getObservable().getValue()));
            }
        });
        numberProperty.addListener(controller);
        return textBox;
    }

    private NumberFormat format;

    protected NumberFormat getFormat() {
        if (format == null) {
            format = createFormat();
        }
        return format;
    }

    protected abstract NumberFormat createFormat();

    protected abstract Number parse(String text) throws ParseException;

}
