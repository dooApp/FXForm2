package com.dooapp.fxform.adapter;

import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.view.FXFormNode;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 29/09/12
 * Time: 18:54
 */
public class TypeAdapterMatcher implements AdapterMatcher {

    private final Class fromType;

    private final Class toType;

    public TypeAdapterMatcher(Class fromType, Class toType) {
        this.fromType = fromType;
        this.toType = toType;
    }

    @Override
    public boolean matches(Class fromClass, Class toClass, Element element, FXFormNode fxFormNode) {
        return fromType.isAssignableFrom(fromClass) && toType.isAssignableFrom(toClass);
    }

}
