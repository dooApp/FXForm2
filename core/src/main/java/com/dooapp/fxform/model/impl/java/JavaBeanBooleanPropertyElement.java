package com.dooapp.fxform.model.impl.java;

import com.dooapp.fxform.model.FormException;
import javafx.beans.property.adapter.JavaBeanBooleanPropertyBuilder;
import javafx.beans.property.adapter.JavaBeanProperty;

import java.lang.reflect.Field;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 14/10/12
 * Time: 13:00
 */
public class JavaBeanBooleanPropertyElement extends AbstractJavaBeanElement<Boolean> {

    public JavaBeanBooleanPropertyElement(Field field) throws FormException {
        super(field);
    }

    @Override
    protected JavaBeanProperty<Boolean> buildJavaBeanProperty() throws NoSuchMethodException {
        return JavaBeanBooleanPropertyBuilder.create()
                .name(field.getName())
                .bean(sourceProperty().get())
                .build();
    }

}
