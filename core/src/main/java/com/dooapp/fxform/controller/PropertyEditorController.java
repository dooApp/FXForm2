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
import com.dooapp.fxform.view.FXFormNode;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;

import javax.validation.*;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created at 27/09/12 17:32.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class PropertyEditorController extends NodeController {
    /**
     * The logger
     */
    private static final Logger logger = Logger.getLogger(PropertyEditorController.class.getName());

    private final ObservableList<ConstraintViolation> constraintViolations;

    static ValidatorFactory factory;

    Validator validator;
    private ChangeListener viewChangeListener;
    private ChangeListener modelChangeListener;

    /**
     * Initialize the constraint validator. Might be null after that if no implementation has been provided.
     */
    private void createValidator() {
        try {
            if (factory == null) {
                factory = Validation.buildDefaultValidatorFactory();
            }
            validator = factory.getValidator();
        } catch (ValidationException e) {
            // validation is not activated, since no implementation has been provided
            logger.log(Level.INFO, "Validation disabled", e);
        }
    }

    public PropertyEditorController(FXForm fxForm, Element element, ObservableList<ConstraintViolation> constraintViolations) {
        super(fxForm, element);
        createValidator();
        this.constraintViolations = constraintViolations;
    }

    @Override
    protected void bind(final FXFormNode fxFormNode) {
        viewChangeListener = new ChangeListener() {
            public void changed(ObservableValue observableValue, Object o, Object o1) {
                Object newValue = getFxForm().getAdapterProvider().getAdapter(getElement().getType(), getNode().getProperty().getClass(), getElement(), getNode()).adaptFrom(o1);
                if (validator != null) {
                    Set<ConstraintViolation<Object>> constraintViolationSet = validator.validateValue((Class<Object>) (getElement().getBean().getClass()), getElement().getName(), newValue);
                    constraintViolations.clear();
                    constraintViolations.addAll(constraintViolationSet);
                }
                if (constraintViolations.size() == 0) {
                    ((PropertyElement) getElement()).setValue(newValue);
                }
            }
        };
        fxFormNode.getProperty().addListener(viewChangeListener);
        modelChangeListener = new ChangeListener() {
            public void changed(ObservableValue observableValue, Object o, Object o1) {
                Object newValue = getFxForm().getAdapterProvider().getAdapter(getElement().getType(), getNode().getProperty().getClass(), getElement(), getNode()).adaptTo(o1);
                fxFormNode.getProperty().setValue(newValue);
            }
        };
        getElement().addListener(modelChangeListener);
        try {
            fxFormNode.getProperty().setValue(getFxForm().getAdapterProvider().getAdapter(getElement().getType(), getNode().getProperty().getClass(), getElement(), getNode()).adaptTo(getElement().getValue()));
        } catch (UnsupportedOperationException e) {
            // this might happen, if the Adapter is not able to convert the user input into a model value,
            // for example when no factory has been found and that we are using the default editor
        }
    }

    @Override
    protected void unbind(FXFormNode fxFormNode) {
        fxFormNode.getProperty().removeListener(viewChangeListener);
        getElement().removeListener(modelChangeListener);
    }
}