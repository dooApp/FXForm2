package com.dooapp.fxform.view.skin;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.view.FXFormNode;
import com.dooapp.fxform.view.FXFormNodeWrapper;
import com.dooapp.fxform.view.FXFormSkin;
import com.dooapp.fxform.view.NodeCreationException;
import javafx.beans.property.Property;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created at 26/09/12 13:49.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class FXMLSkin extends FXFormSkin {

    private final Logger logger = Logger.getLogger(FXMLSkin.class.getName());

    private final URL url;

    private FXMLLoader fxmlLoader;

    public FXMLSkin(FXForm fxForm, URL url) {
        super(fxForm);
        this.url = url;
    }

    @Override
    protected Node createRootNode() throws NodeCreationException {
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(url);
        try {
            return (Node) fxmlLoader.load();
        } catch (IOException e) {
            throw new NodeCreationException(e.getMessage(), e);
        }
    }

    @Override
    protected ElementNodes createElementNodes(Element element) {
        FXFormNode label = lookupNode(element, FXForm.LABEL_ID_SUFFIX);
        FXFormNode editor = lookupNode(element, FXForm.EDITOR_ID_SUFFIX);
        FXFormNode tooltip = lookupNode(element, FXForm.TOOLTIP_ID_SUFFIX);
        FXFormNode constraint = lookupNode(element, FXForm.CONSTRAINT_ID_SUFFIX);
        return new ElementNodes(label, editor, tooltip, constraint);

    }

    @Override
    protected void deleteElementNodes(ElementNodes elementNodes) {
    }


    private FXFormNode lookupNode(Element element, String suffix) {
        Node node = getNode().lookup("#" + element.getName() + suffix);
        if (node != null) {
            Property property = fxForm.getPropertyProvider().getProperty(node);
            if (property != null) {
            return new FXFormNodeWrapper(node, property);
            } else {
                logger.log(Level.WARNING, "Unable to find the property to bind for " + node + "\n" +
                        "Check that you configured the AdapterProvider correctly. See FXForm#setAdapterProvider");
                return null;
            }
        }
        else
            return null;
    }

}