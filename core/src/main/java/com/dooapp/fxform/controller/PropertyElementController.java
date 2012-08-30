package com.dooapp.fxform.controller;

import com.dooapp.fxform.model.PropertyElement;
import com.dooapp.fxform.view.factory.NodeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created at 30/08/12 15:33.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class PropertyElementController<T> extends ElementController<T> {
    /**
     * The logger
     */
    private static final Logger logger = LoggerFactory.getLogger(PropertyElementController.class);

    public PropertyElementController(PropertyElement<T> element, NodeFactory editorFactory, NodeFactory tooltipFactory, NodeFactory labelFactory) {
        super(element, editorFactory, tooltipFactory, labelFactory);
    }

    @Override
    public PropertyElement<T> getElement() {
        return (PropertyElement<T>) super.getElement();
    }
}