package com.dooapp.fxform.adapter;

import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.view.FXFormNode;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 29/09/12
 * Time: 17:30
 */
public interface AdapterProvider {

    /**
     * Provide the adapter from fromClass to toClass for the given element and node.
     *
     * @param fromClass
     * @param toClass
     * @param element
     * @param fxFormNode
     * @return
     */
    public Adapter getAdapter(Class fromClass, Class toClass, Element element, FXFormNode fxFormNode);

}
