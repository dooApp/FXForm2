package com.dooapp.fxform.model.impl.java;

import com.dooapp.fxform.model.FormException;
import com.dooapp.fxform.model.impl.java.AbstractJavaBeanElement;
import javafx.beans.property.adapter.JavaBeanObjectPropertyBuilder;
import javafx.beans.property.adapter.JavaBeanProperty;

import java.lang.reflect.Field;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 14/10/12
 * Time: 12:56
 */
public class JavaBeanObjectPropertyElement extends AbstractJavaBeanElement<Object> {


    public JavaBeanObjectPropertyElement(Field field) throws FormException {
        super(field);
    }

    @Override
    protected JavaBeanProperty<Object> buildJavaBeanProperty() throws NoSuchMethodException {
        return JavaBeanObjectPropertyBuilder
                .create()
                .bean(sourceProperty().get())
                .name(field.getName())
                .build();
    }
}
