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

import com.dooapp.fxform.controller.PropertyElementController;
import com.dooapp.fxform.view.factory.DisposableNode;
import com.dooapp.fxform.view.factory.DisposableNodeWrapper;
import com.dooapp.fxform.view.factory.FormatProvider;
import com.dooapp.fxform.view.factory.NodeFactory;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.util.Callback;

import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;

/**
 * User: Antoine Mischler <antoine@dooapp.com> Date: 17/04/11 Time: 17:31
 */
public abstract class AbstractNumberPropertyDelegate<T extends Number> implements
        NodeFactory<PropertyElementController<T>> {

    protected final FormatProvider formatProvider;

    public AbstractNumberPropertyDelegate(FormatProvider formatProvider) {
        this.formatProvider = formatProvider;
    }

    public DisposableNode createNode(final PropertyElementController<T> controller) {
        final TextField textBox = new TextField();
        final InvalidationListener textBoxListener = createTextBoxListener(controller, textBox);
        textBox.textProperty().addListener(textBoxListener);
        if (controller.getElement().getValue() != null) {
            textBox.textProperty().setValue(formatProvider.getFormat(controller.getElement()).format(controller.getElement().getValue()));
        }
        final ChangeListener controllerListener = createControllerListener(textBox, controller);
        controller.getElement().addListener(controllerListener);
        textBox.promptTextProperty().bind(controller.promptTextProperty());

        return new DisposableNodeWrapper(textBox, new Callback<Node, Void>() {
            public Void call(Node node) {
                controller.getElement().removeListener(controllerListener);
                textBox.textProperty().removeListener(textBoxListener);
                return null;
            }
        });
    }


    protected InvalidationListener createTextBoxListener(final PropertyElementController<T> controller, final TextField textBox) {
        return new InvalidationListener() {

            public void invalidated(Observable observable) {
                if (textBox.getText().trim().length() > 0) {
                    try {
                        ParsePosition parsePosition = new ParsePosition(0);
                        parsePosition.setIndex(0);
                        Number parsed = parse(formatProvider.getFormat(controller.getElement()), parsePosition, textBox.getText());
                        if (parsePosition.getIndex() != textBox.getText().length()) {
                            throw new ParseException(textBox.getText().substring(parsePosition.getIndex()), parsePosition.getIndex());
                        }
                        controller.getConstraintViolations().clear();
                        controller.getElement().setValue((T) parsed);
                    } catch (ParseException e) {
                        controller.getConstraintViolations().clear();
                        controller.getConstraintViolations().add(new ParseErrorConstraintViolation(e.getMessage()));
                    }
                } else {
                    controller.getConstraintViolations().clear();
                }
            }
        };
    }

    protected ChangeListener createControllerListener(final TextField textBox, final PropertyElementController<T> controller) {
        return new ChangeListener() {

            public void changed(ObservableValue observableValue, Object o, Object o1) {
                if (controller.getElement().getValue() != null) {
                    Number currentTextboxNumber = null;
                    try {
                        currentTextboxNumber = parse(formatProvider.getFormat(controller.getElement()), new ParsePosition(0), textBox.getText());
                    } catch (ParseException e) {
                    }
                    if (currentTextboxNumber != null && o1 != null && currentTextboxNumber.doubleValue() != ((Number) o1).doubleValue())
                        textBox.textProperty().setValue(formatProvider.getFormat(controller.getElement()).format(controller.getElement().getValue()));
                } else {
                    textBox.textProperty().set("");
                }
            }
        };
    }

    protected abstract Number parse(Format format, ParsePosition parsePosition, String text) throws ParseException;

}
