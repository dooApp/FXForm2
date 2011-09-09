package com.dooapp.fxform.view.factory;

import com.dooapp.fxform.annotation.FormFactory;
import com.dooapp.fxform.model.ElementController;
import com.dooapp.fxform.view.NodeCreationException;
import javafx.scene.Node;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This factory adds @FormFactory annotation support to a wrapped factory.
 * <p/>
 * User: antoine
 * Date: 07/09/11
 * Time: 16:20
 */
public class AnnotationFactoryWrapper implements NodeFactory {

    private final NodeFactory delegate;

    public AnnotationFactoryWrapper(NodeFactory delegate) {
        this.delegate = delegate;
    }

    public Node createNode(ElementController controller) throws NodeCreationException {
        // check field annotation
        if (controller.getElement().getField().getAnnotation(FormFactory.class) != null) {
            // use factory provided by the annotation
            try {
                return controller.getElement().getField().getAnnotation(FormFactory.class).value().newInstance().createNode(controller);
            } catch (Exception e) {
                // ignore
            }
        }
        // check field type annotation
        try {
            // TODO: retrieve ObjectProperty generic and check for factory annotation
            //return controller.getElement().getField().getType().getAnnotation(FormFactory.class).value().newInstance().createNode(controller);
        } catch (Exception e) {
            // ignore
        }
        return delegate.createNode(controller);
    }

}
