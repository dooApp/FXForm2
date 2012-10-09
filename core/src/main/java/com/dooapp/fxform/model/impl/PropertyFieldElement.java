package com.dooapp.fxform.model.impl;

import com.dooapp.fxform.model.FormException;
import com.dooapp.fxform.model.PropertyElement;
import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;

import java.lang.reflect.Field;

/**
 * Created at 27/09/12 13:42.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class PropertyFieldElement<SourceType, WrappedType> extends ReadOnlyPropertyFieldElement<SourceType, WrappedType> implements PropertyElement<WrappedType> {

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
        throw new UnsupportedOperationException("Not implemented");
    }

    public void unbind() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean isBound() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void bindBidirectional(Property<WrappedType> wrappedTypeProperty) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void unbindBidirectional(Property<WrappedType> wrappedTypeProperty) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public String toString() {
        return "PropertyFieldElement{" +
                "field=" + field +
                '}';
    }

}