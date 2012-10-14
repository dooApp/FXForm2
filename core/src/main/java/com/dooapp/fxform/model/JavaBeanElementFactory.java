package com.dooapp.fxform.model;

import com.dooapp.fxform.model.impl.java.JavaBeanBooleanPropertyElement;
import com.dooapp.fxform.model.impl.java.JavaBeanIntegerPropertyElement;
import com.dooapp.fxform.model.impl.java.JavaBeanObjectPropertyElement;
import com.dooapp.fxform.model.impl.java.JavaBeanStringPropertyElement;

import java.lang.reflect.Field;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 14/10/12
 * Time: 12:05
 */
public class JavaBeanElementFactory implements ElementFactory {

    @Override
    public Element create(Field field) throws FormException {
        if (String.class.isAssignableFrom(field.getType())) {
            return new JavaBeanStringPropertyElement(field);
        } else if (Boolean.class.isAssignableFrom(field.getType()) || field.getType() == Boolean.TYPE) {
            return new JavaBeanBooleanPropertyElement(field);
        } else if (Integer.class.isAssignableFrom(field.getType()) || field.getType() == Integer.TYPE) {
            return new JavaBeanIntegerPropertyElement(field);
        } else if (Float.class.isAssignableFrom(field.getType()) || field.getType() == Float.TYPE) {
            return new JavaBeanIntegerPropertyElement(field);
        } else if (Long.class.isAssignableFrom(field.getType()) || field.getType() == Long.TYPE) {
            return new JavaBeanIntegerPropertyElement(field);
        } else if (Double.class.isAssignableFrom(field.getType()) || field.getType() == Double.TYPE) {
            return new JavaBeanIntegerPropertyElement(field);
        }
        return new JavaBeanObjectPropertyElement(field);
    }

}
