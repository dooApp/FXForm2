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

package com.dooapp.fxform.validation;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;

import javax.validation.ConstraintViolation;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 21/11/2013
 * Time: 11:00
 */
public class ClassLevelValidator {

    private final ObjectProperty bean = new SimpleObjectProperty();

    private final ObjectProperty<FXFormValidator> validator = new SimpleObjectProperty<FXFormValidator>();

    private final ListProperty<ConstraintViolation> constraintViolations = new SimpleListProperty<ConstraintViolation>(FXCollections.<ConstraintViolation>observableArrayList());


    public ClassLevelValidator() {
        bean.addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object o2) {
                validate();
            }
        });
        validator.addListener(new ChangeListener<FXFormValidator>() {
            @Override
            public void changed(ObservableValue<? extends FXFormValidator> observableValue, FXFormValidator validator, FXFormValidator validator2) {
                validate();
            }
        });
    }

    public void validate() {
        constraintViolations.setAll(validator.get().validateClassConstraint(bean.getValue()));
    }

    public Object getBean() {
        return bean.get();
    }

    public ObjectProperty beanProperty() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean.set(bean);
    }

    public FXFormValidator getValidator() {
        return validator.get();
    }

    public ObjectProperty<FXFormValidator> validatorProperty() {
        return validator;
    }

    public void setValidator(FXFormValidator validator) {
        this.validator.set(validator);
    }

    public ReadOnlyListProperty<ConstraintViolation> constraintViolationsProperty() {
        return constraintViolations;
    }
}
