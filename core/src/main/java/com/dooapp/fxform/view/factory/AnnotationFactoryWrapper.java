package com.dooapp.fxform.view.factory;

import com.dooapp.fxform.annotation.FormFactory;
import com.dooapp.fxform.model.ElementController;
import com.dooapp.fxform.reflection.Util;
import com.dooapp.fxform.view.NodeCreationException;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This factory adds @FormFactory annotation support to a wrapped factory.
 * <p/>
 * User: antoine
 * Date: 07/09/11
 * Time: 16:20
 */
public class AnnotationFactoryWrapper implements NodeFactory {

    private Logger logger = LoggerFactory.getLogger(AnnotationFactoryWrapper.class);

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
                logger.warn("The factory provided by the annotation @FormFactory on field " + controller.getElement() + " failed creating node", e);
            }
        }
        // check FormFactory annotation
        if (ObjectProperty.class.isAssignableFrom(controller.getElement().getField().getType())) {
            try {
                Class genericClass = Util.getObjectPropertyGeneric(controller.getElement().getField());
                if (genericClass.getAnnotation(FormFactory.class) != null) {
                    return ((FormFactory) genericClass.getAnnotation(FormFactory.class)).value().newInstance().createNode(controller);
                }
            } catch (Exception e) {
                // ignore
            }
        }
        return delegate.createNode(controller);
    }

}
