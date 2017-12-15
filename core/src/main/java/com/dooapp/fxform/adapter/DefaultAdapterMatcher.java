package com.dooapp.fxform.adapter;

import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.view.FXFormNode;

public class DefaultAdapterMatcher implements AdapterMatcher {

    @Override
    public boolean matches(Class fromClass, Class toClass, Element element, FXFormNode fxFormNode) {
        return fromClass.isAssignableFrom(toClass);
    }
}
