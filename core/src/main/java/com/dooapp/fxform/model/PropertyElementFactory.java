package com.dooapp.fxform.model;

import com.dooapp.fxform.model.impl.PropertyFieldElement;
import com.dooapp.fxform.model.impl.ReadOnlyPropertyFieldElement;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyProperty;

import java.lang.reflect.Field;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 14/10/12
 * Time: 12:05
 */
public class PropertyElementFactory implements ElementFactory {

    @Override
    public Element create(Field field) throws FormException {
        Element element = null;
        if (Property.class.isAssignableFrom(field.getType())) {
            element = new PropertyFieldElement(field);
        } else if (ReadOnlyProperty.class.isAssignableFrom(field.getType())) {
            element = new ReadOnlyPropertyFieldElement(field);
        }
        return element;
    }

}
