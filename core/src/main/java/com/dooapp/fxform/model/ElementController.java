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

package com.dooapp.fxform.model;

import com.dooapp.fxform.i18n.ResourceBundleHelper;
import com.dooapp.fxform.view.factory.NodeFactory;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.*;
import java.util.MissingResourceException;
import java.util.Set;

/**
 * User: Antoine Mischler
 * Date: 26/04/11
 * Time: 11:14
 * <p/>
 * The controller of a Element.
 */
public class ElementController<WrappedType> implements ObservableValue<WrappedType>, WritableValue<WrappedType> {

    private final static Logger logger = LoggerFactory.getLogger(ElementController.class);

    public static String LABEL_SUFFIX = "-label";

    public static String TOOLTIP_SUFFIX = "-tooltip";

    protected final Element<?, WrappedType, ?> element;

    private final ObjectProperty<NodeFactory> editorFactory = new SimpleObjectProperty<NodeFactory>();

    private final ObjectProperty<NodeFactory> tooltipFactory = new SimpleObjectProperty<NodeFactory>();

    private final ObjectProperty<NodeFactory> labelFactory = new SimpleObjectProperty<NodeFactory>();

    private final BooleanProperty dirty = new SimpleBooleanProperty();

    private ObservableList<ConstraintViolation> constraintViolations = FXCollections.observableArrayList();

    ValidatorFactory factory;
    Validator validator;

    public ElementController(Element element) {
        this.element = element;
        try {
            factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
        } catch (ValidationException e) {
            // validation is not activated, since no implementation has been provided
            logger.trace("Validation disabled", e);
        }
    }

    /**
     * Get the label for this field.
     *
     * @return
     */
    public String getLabel() {
        try {
            return ResourceBundleHelper.$(element.getField().getName() + LABEL_SUFFIX);
        } catch (MissingResourceException e) {
            // label is undefined
            return element.getField().getName();
        }
    }

    /**
     * Get the tooltip for this field. Might return null if no tooltip has been defined.
     *
     * @return
     */
    public String getTooltip() {
        try {
            return ResourceBundleHelper.$(element.getField().getName() + TOOLTIP_SUFFIX);
        } catch (MissingResourceException e) {
            // tooltip is undefined
            return null;
        }
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

    public ObjectProperty<NodeFactory> editorFactory() {
        return editorFactory;
    }

    public ObjectProperty<NodeFactory> tooltipFactory() {
        return tooltipFactory;
    }

    public ObjectProperty<NodeFactory> labelFactory() {
        return labelFactory;
    }

    public NodeFactory getEditorFactory() {
        return editorFactory().get();
    }

    public NodeFactory getTooltipFactory() {
        return tooltipFactory.get();
    }

    public NodeFactory getLabelFactory() {
        return labelFactory().get();
    }

    public void setEditorFactory(NodeFactory nodeFactory) {
        this.editorFactory.set(nodeFactory);
    }

    public void setTooltipFactory(NodeFactory nodeFactory) {
        this.tooltipFactory().set(nodeFactory);
    }

    public void setLabelFactory(NodeFactory labelFactory) {
        this.labelFactory.set(labelFactory);
    }

    public ObservableList<ConstraintViolation> getConstraintViolations() {
        return constraintViolations;
    }

    public void setValue(WrappedType o1) {
        // mark controller as dirty
        dirty().set(true);
        if (validator != null) {
            Set<ConstraintViolation<Object>> constraintViolationSet = validator.validateValue((Class<Object>) (element.getSource().getClass()), element.getField().getName(), o1);
            constraintViolations.clear();
            constraintViolations.addAll(constraintViolationSet);
        }
        if (constraintViolations.size() == 0) {
            element.setValue(o1);
        }
    }

    public void addListener(ChangeListener<? super WrappedType> changeListener) {
        element.addListener(changeListener);
    }

    public void removeListener(ChangeListener<? super WrappedType> changeListener) {
        element.removeListener(changeListener);
    }

    public WrappedType getValue() {
        return element.getValue();
    }

    public void addListener(InvalidationListener invalidationListener) {
        element.addListener(invalidationListener);
    }

    public void removeListener(InvalidationListener invalidationListener) {
        element.removeListener(invalidationListener);
    }

    public Element getElement() {
        return element;
    }
}
