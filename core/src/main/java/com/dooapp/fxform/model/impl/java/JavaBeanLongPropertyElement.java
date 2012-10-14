package com.dooapp.fxform.model.impl.java;

import com.dooapp.fxform.model.FormException;
import javafx.beans.property.adapter.JavaBeanLongPropertyBuilder;
import javafx.beans.property.adapter.JavaBeanProperty;

import java.lang.reflect.Field;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 14/10/12
 * Time: 13:15
 */
public class JavaBeanLongPropertyElement extends AbstractJavaBeanElement<Number> {

    public JavaBeanLongPropertyElement(Field field) throws FormException {
        super(field);
    }

    @Override
    protected JavaBeanProperty<Number> buildJavaBeanProperty() throws NoSuchMethodException {
        return JavaBeanLongPropertyBuilder
                .create()
                .bean(sourceProperty().get())
                .name(field.getName())
                .build();
    }

}
