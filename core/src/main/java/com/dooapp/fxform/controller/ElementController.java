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

import com.dooapp.fxform.model.ObservableElement;
import com.dooapp.fxform.view.NodeCreationException;
import com.dooapp.fxform.view.factory.ConstraintNodeFactory;
import com.dooapp.fxform.view.factory.DisposableNode;
import com.dooapp.fxform.view.factory.NodeFactory;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import java.util.ResourceBundle;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 26/04/11
 * Time: 11:14
 * <p/>
 * The controller of a FieldObservableElement.
 */
public class ElementController<WrappedType> {

    private final static Logger logger = LoggerFactory.getLogger(ElementController.class);

    public static String LABEL_SUFFIX = "-label";

    public static String TOOLTIP_SUFFIX = "-tooltip";

    public static String PROMPT_TEXT_SUFFIX = "-prompt";

    protected final ObservableElement<WrappedType> element;

    private final NodeFactory editorFactory;

    private final NodeFactory tooltipFactory;

    private final NodeFactory labelFactory;

    private final NodeFactory constraintFactory = new ConstraintNodeFactory();

    private DisposableNode editor;

    private DisposableNode label;

    private DisposableNode tooltip;

    private final ObjectProperty<ResourceBundle> resourceBundle = new SimpleObjectProperty<ResourceBundle>();

    private final BooleanProperty dirty = new SimpleBooleanProperty();

    protected ObservableList<ConstraintViolation> constraintViolations = FXCollections.observableArrayList();

    public ElementController(ObservableElement<WrappedType> element, NodeFactory editorFactory, NodeFactory tooltipFactory, NodeFactory labelFactory) {
        this.element = element;
        this.editorFactory = editorFactory;
        this.tooltipFactory = tooltipFactory;
        this.labelFactory = labelFactory;
    }

    /**
     * Get the label for this field.
     *
     * @return
     */
    public StringProperty labelProperty() {
        StringProperty stringProperty = new SimpleStringProperty();
        stringProperty.bind(new StringBinding() {
            {
                super.bind(resourceBundle);
            }

            @Override
            protected String computeValue() {
                try {
                    return resourceBundle.get().getString(element.getName() + LABEL_SUFFIX);
                } catch (Exception e) {
                    // label is undefined
                    return (element.getName());
                }
            }
        });
        return stringProperty;
    }

    /**
     * Get the tooltip for this field. Might return null if no tooltip has been defined.
     *
     * @return
     */
    public StringProperty tooltipProperty() {
        StringProperty stringProperty = new SimpleStringProperty();
        stringProperty.bind(new StringBinding() {
            {
                super.bind(resourceBundle);
            }

            @Override
            protected String computeValue() {
                try {
                    return resourceBundle.get().getString(element.getName() + TOOLTIP_SUFFIX);
                } catch (Exception e) {
                    // label is undefined
                    return null;
                }
            }
        });
        return stringProperty;
    }

    /**
     * Get the prompt text for this field. Might return null if no prompt text has been defined.
     *
     * @return
     */
    public StringProperty promptTextProperty() {
        StringProperty stringProperty = new SimpleStringProperty();
        stringProperty.bind(new StringBinding() {
            {
                super.bind(resourceBundle);
            }

            @Override
            protected String computeValue() {
                try {
                    return resourceBundle.get().getString(element.getName() + PROMPT_TEXT_SUFFIX);
                } catch (Exception e) {
                    // label is undefined
                    return null;
                }
            }
        });
        return stringProperty;
    }

    @Override
    public String toString() {
        return "AbstractFormFieldController{" +
                "element=" + element +
                '}';
    }

    public BooleanProperty dirty() {
        return dirty;
    }

    public boolean isDirty() {
        return dirty.get();
    }

    public void setDirty(boolean dirty) {
        this.dirty().set(dirty);
    }

    /**
     * Dispose this controller. The controller should clear all existing bindings.
     */
    public void dispose() {
        element.dispose();
    }

    public ObjectProperty<ResourceBundle> resourceBundleProperty() {
        return resourceBundle;
    }

    public DisposableNode getEditor() throws NodeCreationException {
        if (editor == null) {
            editor = editorFactory.createNode(this);
        }
        return editor;
    }

    public DisposableNode getTooltip() throws NodeCreationException {
        if (tooltip == null) {
            tooltip = tooltipFactory.createNode(this);
        }
        return tooltip;
    }

    public DisposableNode getLabel() throws NodeCreationException {
        if (label == null) {
            label = labelFactory.createNode(this);
        }
        return label;
    }

    public ObservableList<ConstraintViolation> getConstraintViolations() {
        return constraintViolations;
    }

    public ObservableElement<WrappedType> getElement() {
        return element;
    }

    public DisposableNode getConstraint() throws NodeCreationException {
        return constraintFactory.createNode(this);
    }

}
