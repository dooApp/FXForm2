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

package com.dooapp.fxform.controller;

import com.dooapp.fxform.AbstractFXForm;
import com.dooapp.fxform.adapter.Adapter;
import com.dooapp.fxform.adapter.AdapterException;
import com.dooapp.fxform.adapter.AnnotationAdapterProvider;
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.model.PropertyElement;
import com.dooapp.fxform.validation.PropertyElementValidator;
import com.dooapp.fxform.view.FXFormNode;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.dooapp.fxform.utils.ObjectUtils.areSame;

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

    private final PropertyElementValidator propertyElementValidator;

    private ChangeListener viewChangeListener;
    private ChangeListener modelChangeListener;

    private final AnnotationAdapterProvider annotationAdapterProvider = new AnnotationAdapterProvider();

    private AtomicBoolean lock = new AtomicBoolean();
    private boolean disposed = false;

    public PropertyEditorController(AbstractFXForm fxForm, Element element) {
        super(fxForm, element);
        propertyElementValidator = new PropertyElementValidator((PropertyElement) element);
        propertyElementValidator.validatorProperty().bind(fxForm.fxFormValidatorProperty());

    }

    @Override
    protected void bind(final FXFormNode fxFormNode) {
        viewChangeListener = new ChangeListener() {
            public void changed(ObservableValue observableValue, Object o, Object o1) {
                if (lock.getAndSet(true)) {
                    return;
                }
                try {
                    Adapter adapter = annotationAdapterProvider.getAdapter(getElement().getType(), getNode().getProperty().getClass(), getElement(), getNode());
                    if (adapter == null) {
                        adapter = getFxForm().getAdapterProvider().getAdapter(getElement().getType(), getNode().getProperty().getClass(), getElement(), getNode());
                    }
                    Object newValue = propertyElementValidator.adapt(o1, adapter);
                    // check whether the value represented in the view is different from the current model value
                    if (!areSame(getElement().getValue(), newValue)) {
                        // yes, so validate this new value
                        propertyElementValidator.validate(newValue);
                        if (!propertyElementValidator.isInvalid()) {
                            if (!((PropertyElement) getElement()).isBound()) {
                                // and update the model if no constraint prevent from it
                                ((PropertyElement) getElement()).setValue(newValue);
                                // and perform a class level validation
                                getFxForm().getClassLevelValidator().validate();
                            }
                        }
                    }
                } catch (AdapterException e) {
                    // The input value can not be adapted as model value
                    // Nothing to do, a constraint violation should have been reported by the PropertyElementValidator
                } finally {
                    lock.set(false);
                }
            }
        };
        fxFormNode.getProperty().addListener(viewChangeListener);
        modelChangeListener = new ChangeListener() {
            public void changed(ObservableValue observableValue, Object o, Object o1) {
                updateView(o1, fxFormNode);
                // The element value was updated, so request a class level check again
                propertyElementValidator.validate(o1);
                getFxForm().getClassLevelValidator().validate();
            }
        };
        getElement().addListener(modelChangeListener);
        updateView(getElement().getValue(), getNode());
    }

    private void updateView(Object o1, FXFormNode fxFormNode) {
        try {
            Adapter adapter = annotationAdapterProvider.getAdapter(getElement().getType(), getNode().getProperty().getClass(), getElement(), getNode());
            if (adapter == null) {
                adapter = getFxForm().getAdapterProvider().getAdapter(getElement().getType(), getNode().getProperty().getClass(), getElement(), getNode());
            }
            Object currentViewValue = adapter.adaptFrom(fxFormNode.getProperty().getValue());
            // Make sure that the value represented by the view differ from the new model value
            if (!areSame(o1, currentViewValue)) {
                Object newValue = adapter.adaptTo(o1);
                // Update the view later in the JavaFX Thread
                Platform.runLater(() -> {
                    // make sure not to update the view if the current controller has been disposed
                    // between the trigger and the execution time of this Platform.runLater.
                    if (!isDisposed()) {
                        // make sure that this view update won't trigger a model update
                        lock.set(true);
                        fxFormNode.getProperty().setValue(newValue);
                        lock.set(false);
                    }
                });
            }
            if (!fxFormNode.getNode().disableProperty().isBound()) {
                fxFormNode.getNode().setDisable((((PropertyElement) getElement()).isBound()));
            }
        } catch (AdapterException e) {
            // The model value can not be adapted to the view
            logger.log(Level.FINE, e.getMessage(), e);
        }
    }

    public PropertyElementValidator getPropertyElementValidator() {
        return propertyElementValidator;
    }

    @Override
    protected void unbind(FXFormNode fxFormNode) {
        fxFormNode.getProperty().removeListener(viewChangeListener);
        getElement().removeListener(modelChangeListener);
    }
}
