package com.dooapp.fxform.model.impl.java;

import com.dooapp.fxform.model.FormException;
import javafx.beans.property.adapter.JavaBeanFloatPropertyBuilder;
import javafx.beans.property.adapter.JavaBeanProperty;

import java.lang.reflect.Field;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 14/10/12
 * Time: 13:12
 */
public class JavaBeanFloatPropertyElement extends AbstractJavaBeanElement<Number> {

    public JavaBeanFloatPropertyElement(Field field) throws FormException {
        super(field);
    }

    @Override
    protected JavaBeanProperty<Number> buildJavaBeanProperty() throws NoSuchMethodException {
        return JavaBeanFloatPropertyBuilder
                .create()
                .bean(sourceProperty().get())
                .name(field.getName())
                .build();
    }

}
