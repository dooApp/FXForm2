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
import com.dooapp.fxform.model.impl.PropertyFieldElement;
import com.dooapp.fxform.validation.PropertyElementValidator;
import com.dooapp.fxform.view.FXFormNode;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

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

    private final PropertyElementValidator propertyElementValidator;

    private ChangeListener viewChangeListener;
    private ChangeListener modelChangeListener;
    
    private Object newViewValue;
    private Object newModelValue;
    private FXFormNode fxFormNode;

    private final AnnotationAdapterProvider annotationAdapterProvider = new AnnotationAdapterProvider();

    public PropertyEditorController(AbstractFXForm fxForm, Element element) {
        super(fxForm, element);
        propertyElementValidator = new PropertyElementValidator((PropertyElement) element);
        propertyElementValidator.validatorProperty().bind(fxForm.fxFormValidatorProperty());

    }

    public void persistToModel(){
//        System.out.println("in persist to model");
        
        try {  
                    Adapter adapter = annotationAdapterProvider.getAdapter(getElement().getType(), getNode().getProperty().getClass(), getElement(), getNode());
                    if (adapter == null) {
                        adapter = getFxForm().getAdapterProvider().getAdapter(getElement().getType(), getNode().getProperty().getClass(), getElement(), getNode());
                    }
                    Object newValue = propertyElementValidator.adapt(fxFormNode.getProperty().getValue(), adapter);
                    propertyElementValidator.validate(newValue);
                    if (!propertyElementValidator.isInvalid()) {
                        if (!((PropertyElement) getElement()).isBound()) {
                            ((PropertyElement) getElement()).setValue(newValue);
                        }
                    }
                } catch (AdapterException e) {
                    // The input value can not be adapted as model value
                    // Nothing to do, a constraint violation should have been reported by the PropertyElementValidator
                }
    }
    public void persistToView(){
        updateView();
                // The element value was updated, so request a class level check again
        getFxForm().getClassLevelValidator().validate();
    }
    
    @Override
    protected void bind(final FXFormNode fxFormNode) {
        
       
        this.fxFormNode = fxFormNode;
        getFxForm().getPropertyEditorControllerList().add(this);
        viewChangeListener = new ChangeListener() {
            public void changed(ObservableValue observableValue, Object o, Object o1) { 
                newViewValue = o1;
                if(getFxForm().getAutoPersistToModel()){
                    persistToModel();
                }                  
            }
        };
        fxFormNode.getProperty().addListener(viewChangeListener);
        modelChangeListener = new ChangeListener() {
            public void changed(ObservableValue observableValue, Object o, Object o1) {
                newModelValue = o1;
                if(getFxForm().getAutoPersistToView()){ 
                    persistToView();
                }      
                   
            }
        };
        getElement().addListener(modelChangeListener);
        updateView();
        
        
    }

    private void updateView() {
        try {
            Adapter adapter = annotationAdapterProvider.getAdapter(getElement().getType(), getNode().getProperty().getClass(), getElement(), getNode());
            if (adapter == null) {
                adapter = getFxForm().getAdapterProvider().getAdapter(getElement().getType(), getNode().getProperty().getClass(), getElement(), getNode());
            }
            Object newValue = adapter.adaptTo(getElement().getValue());
            fxFormNode.getProperty().setValue(newValue);
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
       
        
        getFxForm().getPropertyEditorControllerList().remove(this);
        fxFormNode.getProperty().removeListener(viewChangeListener);
        getElement().removeListener(modelChangeListener);
    }

}