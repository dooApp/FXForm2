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

    protected final ElementFactory elementFactory;
    protected final boolean bufferUserInput;
    protected final boolean bufferBeanChanges;

    public BufferedElementFactory(ElementFactory elementFactory) {
        this(elementFactory, true, true);
    }

    public BufferedElementFactory(ElementFactory elementFactory, boolean bufferUserInput, boolean bufferBeanChanges) {
        this.elementFactory = elementFactory;
        this.bufferUserInput = bufferUserInput;
        this.bufferBeanChanges = bufferBeanChanges;
    }

    @Override
    public Element create(Field field) throws FormException {
        Element element = elementFactory.create(field);

        if (element instanceof PropertyElement) {
            return new BufferedPropertyElement((PropertyElement) element, bufferUserInput, bufferBeanChanges);
        } else {
            return new BufferedElement(element, bufferBeanChanges);
        }
    }

}
