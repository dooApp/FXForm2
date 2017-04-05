package com.dooapp.fxform.model;

import com.dooapp.fxform.model.impl.BufferedElement;
import com.dooapp.fxform.model.impl.BufferedPropertyElement;

import java.lang.reflect.Field;

/**
 * An {@link ElementFactory} that creates {@link com.dooapp.fxform.model.impl.BufferedElement BufferedElements}.
 *
 * @author Stefan Endrullis (endrullis@iat.uni-leipzig.de)
 */
public class BufferedElementFactory implements ElementFactory {

    final ElementFactory elementFactory;

    public BufferedElementFactory(ElementFactory elementFactory) {
        this.elementFactory = elementFactory;
    }

    @Override
    public Element create(Field field) throws FormException {
        Element element = elementFactory.create(field);

        if (element instanceof PropertyElement) {
            return new BufferedPropertyElement((PropertyElement) element);
        } else {
            return new BufferedElement(element);
        }
    }

}
