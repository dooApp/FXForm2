package com.dooapp.fxform.model;

import javafx.beans.property.ReadOnlyProperty;

import java.lang.reflect.Field;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 14/10/12
 * Time: 12:06
 */
public class DefaultElementFactory implements ElementFactory {

    private PropertyElementFactory propertyElementFactory = new PropertyElementFactory();

    private JavaBeanElementFactory javaBeanElementFactory = new JavaBeanElementFactory();

    @Override
    public Element create(Field field) throws FormException {
        if (ReadOnlyProperty.class.isAssignableFrom(field.getType())) {
            return propertyElementFactory.create(field);
        } else {
            return javaBeanElementFactory.create(field);
        }
    }

}
