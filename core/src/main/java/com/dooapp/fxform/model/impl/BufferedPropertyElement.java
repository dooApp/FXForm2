package com.dooapp.fxform.model.impl;

import com.dooapp.fxform.model.PropertyElement;
import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;

/**
 * A wrapper for {@link PropertyElement} which buffers the element value instead of writing it directly back to the source bean.
 *
 * @author Stefan Endrullis (endrullis@iat.uni-leipzig.de)
 */
public class BufferedPropertyElement<WrappedType> extends BufferedElement<WrappedType> implements PropertyElement<WrappedType> {

    private final PropertyElement<WrappedType> element;

    /**
     * Wraps the given element to create a buffered version of it.
     *
     * @param element element to buffer
     */
    public BufferedPropertyElement(PropertyElement<WrappedType> element) {
        super(element);
        this.element = element;
    }

    @Override
    public void bind(ObservableValue<? extends WrappedType> observable) {
        bufferedValue.bind(observable);
    }

    @Override
    public void unbind() {
        bufferedValue.unbind();
    }

    @Override
    public boolean isBound() {
        return bufferedValue.isBound();
    }

    @Override
    public void bindBidirectional(Property<WrappedType> other) {
        bufferedValue.bindBidirectional(other);
    }

    @Override
    public void unbindBidirectional(Property<WrappedType> other) {
        bufferedValue.unbindBidirectional(other);
    }

    @Override
    public void setValue(WrappedType value) {
        bufferedValue.setValue(value);
    }

    /**
     * Writes the buffered value to the source bean.
     */
    public void commit() {
        element.setValue(bufferedValue.getValue());
    }

}
