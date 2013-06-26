package com.dooapp.fxform.adapter;

import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.view.FXFormNode;

/**
 * This matcher matches when both classes have a common Property superclass.
 * Created at 26/06/13 16:42.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class PropertyTypeMatcher<T > implements AdapterMatcher {

    private final Class propertyClass;

    public PropertyTypeMatcher(Class propertyClass) {
        this.propertyClass = propertyClass;
    }

    @Override
    public boolean matches(Class fromClass, Class toClass, Element element, FXFormNode fxFormNode) {
        return propertyClass.isAssignableFrom(fromClass) && propertyClass.isAssignableFrom(toClass);
    }
}