package com.dooapp.fxform.model.impl.java;

import com.dooapp.fxform.model.FormException;
import javafx.beans.property.adapter.JavaBeanIntegerPropertyBuilder;
import javafx.beans.property.adapter.JavaBeanProperty;

import java.lang.reflect.Field;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 14/10/12
 * Time: 13:02
 */
public class JavaBeanIntegerPropertyElement extends AbstractJavaBeanElement<Number> {

    public JavaBeanIntegerPropertyElement(Field field) throws FormException {
        super(field);
    }

    @Override
    protected JavaBeanProperty<Number> buildJavaBeanProperty() throws NoSuchMethodException {
        return JavaBeanIntegerPropertyBuilder
                .create()
                .bean(sourceProperty().get())
                .name(field.getName())
                .build();
    }

}
