package com.dooapp.fxform.model.impl;

import com.dooapp.fxform.model.FormException;
import com.dooapp.fxform.model.PropertyElement;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created at 30/08/12 14:00.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class FieldPropertyElement<SourceType, WrappedType, FieldType extends ObservableValue<WrappedType>> extends FieldObservableElement<SourceType, WrappedType, FieldType> implements PropertyElement<WrappedType> {


    private ArrayList constraintViolations = new ArrayList();

    public FieldPropertyElement(Field field) throws FormException {
        super(field);
        if (!WritableValue.class.isAssignableFrom(field.getType())) {
            throw new FormException("Trying to create a writable element with a non-writable field: " + field.getType());
        }
    }

    static ValidatorFactory factory;

    Validator validator;

    private PropertyElement delegate;

    public void setValue(WrappedType o) {
        if (validator != null) {
            Set<ConstraintViolation<Object>> constraintViolationSet = validator.validateValue((Class<Object>) (getSource().getClass()), getName(), o);
            constraintViolations.clear();
            constraintViolations.addAll(constraintViolationSet);
        }
        if (constraintViolations.size() == 0) {
            ((WritableValue) valueProperty().get()).setValue(o);
        }
    }

}