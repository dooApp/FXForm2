package com.dooapp.fxform.model.impl;

import com.dooapp.fxform.model.PropertyElement;
import javafx.beans.InvalidationListener;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.lang.annotation.Annotation;

/**
 * Implementation of {@link PropertyElement} wrapping an existing {@link Property} instance
 *
 * @param <W>
 */
public class PropertyElementWrapper<W> implements PropertyElement<W> {

    private final Property<W> property;

    private final Class<W> myClass;

    public PropertyElementWrapper(Property<W> property, Class<W> myClass) {
        this.property = property;
        this.myClass = myClass;
    }

    @Override
    public Class<?> getType() {
        return property.getClass();
    }

    @Override
    public Class<W> getWrappedType() {
        return myClass;
    }

    @Override
    public Property sourceProperty() {
        return new SimpleObjectProperty();
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return null;
    }

    @Override
    public Class getDeclaringClass() {
        return null;
    }

    @Override
    public String getCategory() {
        return null;
    }

    @Override
    public void setCategory(String category) {

    }

    @Override
    public void dispose() {
    }

    @Override
    public Object getBean() {
        return property.getBean();
    }

    @Override
    public String getName() {
        return property.getName();
    }

    @Override
    public void addListener(ChangeListener<? super W> listener) {
        property.addListener(listener);
    }

    @Override
    public void removeListener(ChangeListener<? super W> listener) {
        property.removeListener(listener);
    }

    @Override
    public W getValue() {
        return property.getValue();
    }

    @Override
    public void setValue(W value) {
        property.setValue(value);
    }

    @Override
    public void addListener(InvalidationListener listener) {
        property.addListener(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        property.removeListener(listener);
    }

    @Override
    public void bind(ObservableValue<? extends W> observable) {
        property.bind(observable);
    }

    @Override
    public void unbind() {
        property.unbind();
    }

    @Override
    public boolean isBound() {
        return property.isBound();
    }

    @Override
    public void bindBidirectional(Property<W> other) {
        property.bindBidirectional(other);
    }

    @Override
    public void unbindBidirectional(Property<W> other) {
        property.unbindBidirectional(other);
    }

}
