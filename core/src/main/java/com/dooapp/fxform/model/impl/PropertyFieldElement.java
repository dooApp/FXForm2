package com.dooapp.fxform.model.impl;

import com.dooapp.fxform.model.FormException;
import com.dooapp.fxform.model.PropertyElement;
import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * Created at 27/09/12 13:42.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class PropertyFieldElement<SourceType, WrappedType> extends ReadOnlyPropertyFieldElement<SourceType, WrappedType> implements PropertyElement<WrappedType> {
    /**
     * The logger
     */
    private static final Logger logger = LoggerFactory.getLogger(PropertyFieldElement.class);

    public PropertyFieldElement(Field field) throws FormException {
        super(field);
        if (!WritableValue.class.isAssignableFrom(field.getType())) {
            throw new FormException("Trying to create a writable field element with a non-writable field: " + field.getType());
        }
    }

    public void setValue(WrappedType o) {
        ((Property<WrappedType>) valueProperty().get()).setValue(o);
    }


    public void bind(ObservableValue<? extends WrappedType> observableValue) {
        //TODO write this method
        throw new UnsupportedOperationException("Not implemented");
    }

    public void unbind() {
        //TODO write this method
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean isBound() {
        //TODO write this method
        throw new UnsupportedOperationException("Not implemented");
    }

    public void bindBidirectional(Property<WrappedType> wrappedTypeProperty) {
        //TODO write this method
        throw new UnsupportedOperationException("Not implemented");
    }

    public void unbindBidirectional(Property<WrappedType> wrappedTypeProperty) {
        //TODO write this method
        throw new UnsupportedOperationException("Not implemented");
    }
}