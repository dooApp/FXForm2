package com.dooapp.fxform.model;

import javafx.beans.value.WritableValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.*;
import java.util.Set;

/**
 * User: antoine
 * Date: 07/09/11
 * Time: 14:52
 */
public class PropertyElementController<WrappedType> extends ElementController<WrappedType> implements WritableValue<WrappedType> {

    private final Logger logger = LoggerFactory.getLogger(PropertyElementController.class);

    ValidatorFactory factory;
    Validator validator;

    public PropertyElementController(PropertyElement element) {
        super(element);
        try {
            factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
        } catch (ValidationException e) {
            // validation is not activated, since no implementation has been provided
            logger.trace("Validation disabled", e);
        }
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
            ((PropertyElement) element).setValue(o1);
        }
    }

}
