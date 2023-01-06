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

import com.dooapp.fxform.adapter.Adapter;
import com.dooapp.fxform.adapter.AdapterException;
import com.dooapp.fxform.model.PropertyElement;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;

import javax.validation.ConstraintViolation;
import javax.validation.ElementKind;
import javax.validation.Path;
import java.util.List;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 21/11/2013
 * Time: 10:39
 */
public class PropertyElementValidator {

    private final PropertyElement element;

    private final ObjectProperty<FXFormValidator> validator = new SimpleObjectProperty<FXFormValidator>();

    private final ListProperty<ConstraintViolation> constraintViolations = new SimpleListProperty<>(FXCollections.synchronizedObservableList(FXCollections.<ConstraintViolation>observableArrayList()));

    private final ListProperty<ConstraintViolation> classLevelConstraintViolations = new SimpleListProperty<ConstraintViolation>(FXCollections.synchronizedObservableList(FXCollections.<ConstraintViolation>observableArrayList()));

    private final BooleanProperty invalid = new SimpleBooleanProperty(false);

    private final BooleanProperty warning = new SimpleBooleanProperty(false);

    private NotAdaptableInputValue notAdaptableInputValue;

    public PropertyElementValidator(final PropertyElement element) {
        this.element = element;
        validator.addListener((observableValue, validator, validator2) -> validate(element.getValue()));
    }

    public Object adapt(final Object newValue, Adapter adapter) throws AdapterException {
        if (notAdaptableInputValue != null) {
            constraintViolations.remove(notAdaptableInputValue);
            notAdaptableInputValue = null;
        }
        try {
            return adapter.adaptFrom(newValue);
        } catch (Exception e) {
            notAdaptableInputValue = new NotAdaptableInputValue(element, newValue, validator.get().getMessageInterpolator());
            constraintViolations.add(notAdaptableInputValue);
            throw new AdapterException(e);
        }
    }

    public void validate(Object newValue) {
        constraintViolations.clear();
        FXFormValidator validator = getValidator();
        if (validator != null) {
            // Validate strict constraints that prevent the model value from being updated
            constraintViolations.addAll(validator.validate(element, newValue));
            invalid.set(!constraintViolations.isEmpty());
            // Validate warnings constraints
            List<ConstraintViolation> warningList = validator.validate(element, newValue, Warning.class);
            warning.set(!warningList.isEmpty());
            constraintViolations.addAll(warningList);
        }
    }

    /**
     * This method will check for each of those class level constraint violations
     * if the violation relates to this element and add it to this list of violated constraints if required.
     * <p/>
     * Return the list of constraints related to the element handled by this property validator.
     */
    public List<ConstraintViolation> reportClassLevelConstraintViolation(List<ConstraintViolation> violations) {
        constraintViolations.removeAll(classLevelConstraintViolations);
        invalid.set(!constraintViolations.isEmpty());
        classLevelConstraintViolations.clear();
        for (ConstraintViolation constraintViolation : violations) {
            if (isRelated(constraintViolation)) {
                classLevelConstraintViolations.add(constraintViolation);
                invalid.set(true);
            }
        }
        constraintViolations.addAll(classLevelConstraintViolations);
        return classLevelConstraintViolations;
    }

    /**
     * This method checks if the given constraint violation is related to the element handled by this validator.
     *
     * @param constraintViolation
     * @return
     */
    private boolean isRelated(ConstraintViolation constraintViolation) {
        if (constraintViolation.getPropertyPath() == null)
            return false;
        for (Path.Node node : constraintViolation.getPropertyPath()) {
            if (node.getKind() == ElementKind.PROPERTY) {
                if (element.getName().equals(node.getName())) {
                    return true;
                }
            }

        }
        return false;
    }

    public ReadOnlyListProperty<ConstraintViolation> constraintViolationsProperty() {
        return constraintViolations;
    }

    public boolean isInvalid() {
        return invalid.get();
    }

    public ReadOnlyBooleanProperty invalidProperty() {
        return invalid;
    }

    public boolean isWarning() {
        return warning.get();
    }

    public ReadOnlyBooleanProperty warningProperty() {
        return warning;
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

}
