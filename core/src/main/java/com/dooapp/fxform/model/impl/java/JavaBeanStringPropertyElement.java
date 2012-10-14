package com.dooapp.fxform.model.impl.java;

import com.dooapp.fxform.model.FormException;
import javafx.beans.property.adapter.JavaBeanProperty;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;

import java.lang.reflect.Field;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 14/10/12
 * Time: 12:40
 */
public class JavaBeanStringPropertyElement extends AbstractJavaBeanElement<String> {

    public JavaBeanStringPropertyElement(Field field) throws FormException {
        super(field);
    }

    @Override
    protected JavaBeanProperty<String> buildJavaBeanProperty() throws NoSuchMethodException {
        return JavaBeanStringPropertyBuilder
                .create()
                .bean(sourceProperty().get())
                .name(field.getName())
                .build();
    }

}
