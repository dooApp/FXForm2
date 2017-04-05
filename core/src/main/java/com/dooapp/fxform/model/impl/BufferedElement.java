package com.dooapp.fxform.model.impl;

import com.dooapp.fxform.model.Element;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;

import java.lang.annotation.Annotation;

/**
 * A wrapper for {@link Element} which buffers the element value.
 *
 * @author Stefan Endrullis (endrullis@iat.uni-leipzig.de)
 */
public class BufferedElement<WrappedType> implements Element<WrappedType> {

    ObjectProperty<WrappedType> bufferedValue = new SimpleObjectProperty<>();

    /**
     * The wrapped element.
     */
    private final Element<WrappedType> element;

    /**
     * Wraps the given element to create a buffered version of it.
     *
     * @param element element to buffer
     */
    public BufferedElement(Element<WrappedType> element) {
        this.element = element;
        reload();

        sourceProperty().addListener((observable, oldValue, newValue) -> reload());
    }


    @Override
    public void dispose() {
        element.dispose();
    }

    @Override
    public Class<?> getType() {
        return element.getType();
    }

    @Override
    public Class getWrappedType() {
        return element.getWrappedType();
    }

    @Override
    public Property sourceProperty() {
        return element.sourceProperty();
    }

    @Override
    public Class getDeclaringClass() {
        return element.getDeclaringClass();
    }

    @Override
    public String getCategory() {
        return element.getCategory();
    }

    @Override
    public void setCategory(String category) {
        element.setCategory(category);
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return element.getAnnotation(annotationClass);
    }

    @Override
    public Object getBean() {
        return element.getBean();
    }

    @Override
    public String getName() {
        return element.getName();
    }

    @Override
    public void addListener(ChangeListener<? super WrappedType> listener) {
        bufferedValue.addListener(listener);
    }

    @Override
    public void removeListener(ChangeListener<? super WrappedType> listener) {
        bufferedValue.removeListener(listener);
    }

    @Override
    public WrappedType getValue() {
        return bufferedValue.get();
    }

    @Override
    public void addListener(InvalidationListener listener) {
        bufferedValue.addListener(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        bufferedValue.removeListener(listener);
    }

    /**
     * Updates the buffered value by loading it from the source bean.
     */
    public void reload() {
        bufferedValue.setValue(element.getValue());
    }

}
