package com.dooapp.fxform.model.impl;

import com.dooapp.fxform.model.FormException;
import javafx.beans.value.WritableValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * Created at 30/08/12 14:00.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class FieldPropertyElement {
    /**
     * The logger
     */
    private static final Logger logger = LoggerFactory.getLogger(FieldPropertyElement.class);

    public FieldPropertyElement(Field field) throws FormException {
        super(field);
        if (!WritableValue.class.isAssignableFrom(field.getType())) {
            throw new FormException("Trying to create a writable element with a non-writable field: " + field.getType());
        }
    }

    public void setValue(WrappedType o) {
        valueProperty().get().setValue(o);
    }

}