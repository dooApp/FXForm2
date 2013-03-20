package com.dooapp.fxform.adapter;

import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.view.FXFormNode;
import javafx.beans.property.ObjectProperty;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 20/03/13
 * Time: 14:03
 */
public class ObjectPropertyAdapterMatcher implements AdapterMatcher {

    private final Class fromPropertyType;

    private final Class toType;

    public ObjectPropertyAdapterMatcher(Class fromPropertyType, Class toType) {
        this.fromPropertyType = fromPropertyType;
        this.toType = toType;
    }

    @Override
    public boolean matches(Class fromClass, Class toClass, Element element, FXFormNode fxFormNode) {
        return ObjectProperty.class.isAssignableFrom(fromClass)
                && fromPropertyType.isAssignableFrom(element.getWrappedType())
                && toType.isAssignableFrom(toClass);
    }
}
