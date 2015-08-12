package com.dooapp.fxform.handler;

import com.dooapp.fxform.model.Element;

import java.lang.reflect.Modifier;

/**
 * A field handler used to match a field which wrapped type is abstract.
 * User: Kevin Senechal <kevin.senechal@dooapp.com>
 * Date: 12/08/2015
 * Time: 11:00
 */
public class AbstractWrappedTypeHandler implements ElementHandler {
    @Override
    public boolean handle(Element element) {
        return Modifier.isAbstract(element.getWrappedType().getModifiers());
    }
}
