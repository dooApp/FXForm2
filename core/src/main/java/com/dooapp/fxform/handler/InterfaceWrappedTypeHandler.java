package com.dooapp.fxform.handler;

import com.dooapp.fxform.model.Element;

/**
 * A field handler used to match a field which wrapped type is an interface.
 * User: Kevin Senechal <kevin.senechal@dooapp.com>
 * Date: 12/08/2015
 * Time: 11:00
 */
public class InterfaceWrappedTypeHandler implements ElementHandler {
    @Override
    public boolean handle(Element element) {
        return element.getWrappedType().isInterface();
    }
}
