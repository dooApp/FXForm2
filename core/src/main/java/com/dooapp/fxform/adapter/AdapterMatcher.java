package com.dooapp.fxform.adapter;

import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.view.FXFormNode;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 29/09/12
 * Time: 18:54
 */
public interface AdapterMatcher {

    public boolean matches(Class fromClass, Class toClass, Element element, FXFormNode fxFormNode);

}
