package com.dooapp.fxform.model.impl.java;

import com.dooapp.fxform.model.FormException;
import com.dooapp.fxform.model.PropertyElement;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.adapter.JavaBeanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 14/10/12
 * Time: 12:16
 */
public abstract class AbstractJavaBeanElement<W> implements PropertyElement<W> {

    private final Logger logger = Logger.getLogger(AbstractJavaBeanElement.class.getName());

    private JavaBeanProperty<W> javaBeanProperty;

    protected final Field field;

    private final ObjectProperty sourceProperty = new SimpleObjectProperty();
    private final ChangeListener changeListener;

    public AbstractJavaBeanElement(Field field) throws FormException {
        this.field = field;
        if (sourceProperty.get() != null) {
            try {
                javaBeanProperty = buildJavaBeanProperty();
            } catch (NoSuchMethodException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
        }
        changeListener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object o1) {
                try {
                    if (javaBeanProperty != null) {
                        javaBeanProperty.dispose();
                    }
                    javaBeanProperty = buildJavaBeanProperty();
                } catch (NoSuchMethodException e) {
                    logger.log(Level.WARNING, e.getMessage(), e);
                }
            }
        };
        sourceProperty.addListener(changeListener);
    }

    protected abstract JavaBeanProperty<W> buildJavaBeanProperty() throws NoSuchMethodException;

    @Override
    public Class<?> getType() {
        return javaBeanProperty.getClass();
    }

    @Override
    public Class getGenericType() {
        return field.getType();
    }

    @Override
    public ObjectProperty sourceProperty() {
        return sourceProperty;
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return field.getAnnotation(annotationClass);
    }

    @Override
    public void dispose() {
        sourceProperty.removeListener(changeListener);
        javaBeanProperty.dispose();
    }

    @Override
    public Object getBean() {
        return sourceProperty().get();
    }

    @Override
    public String getName() {
        return field.getName();
    }

    @Override
    public void addListener(ChangeListener changeListener) {
        javaBeanProperty.addListener(changeListener);
    }

    @Override
    public void removeListener(ChangeListener changeListener) {
        javaBeanProperty.removeListener(changeListener);
    }

    @Override
    public W getValue() {
        return javaBeanProperty.getValue();
    }

    @Override
    public void setValue(W w) {
         javaBeanProperty.setValue(w);
    }

    @Override
    public void addListener(InvalidationListener invalidationListener) {
        javaBeanProperty.addListener(invalidationListener);
    }

    @Override
    public void removeListener(InvalidationListener invalidationListener) {
        javaBeanProperty.removeListener(invalidationListener);
    }

    @Override
    public void bind(ObservableValue<? extends W> observableValue) {
        javaBeanProperty.bind(observableValue);
    }

    @Override
    public void unbind() {
    }

    @Override
    public boolean isBound() {
        return javaBeanProperty.isBound();
    }

    @Override
    public void bindBidirectional(Property<W> wProperty) {
        javaBeanProperty.bindBidirectional(wProperty);
    }

    @Override
    public void unbindBidirectional(Property<W> wProperty) {
        javaBeanProperty.unbindBidirectional(wProperty);
    }
}
