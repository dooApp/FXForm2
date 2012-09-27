package com.dooapp.fxform.view.skin;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.controller.ElementController;
import com.dooapp.fxform.controller.PropertyElementController;
import com.dooapp.fxform.utils.ConfigurationStore;
import com.dooapp.fxform.utils.Configurer;
import com.dooapp.fxform.utils.property.DefaultPropertyProvider;
import com.dooapp.fxform.view.FXFormSkin;
import com.dooapp.fxform.view.NodeCreationException;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created at 26/09/12 13:49.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class FXMLSkin extends FXFormSkin {
    /**
     * The logger
     */
    private static final Logger logger = LoggerFactory.getLogger(FXMLSkin.class);

    private final URL url;

    private FXMLLoader fxmlLoader;

    private Map<ElementController, ChangeListener> editorListenerMap = new HashMap<ElementController, ChangeListener>();

    private Map<ElementController, ChangeListener> propertyElementListenerMap = new HashMap<ElementController, ChangeListener>();

    private DefaultPropertyProvider defaultPropertyProvider;

    private final ConfigurationStore<ElementController> configurationStore = new ConfigurationStore<ElementController>();

    public FXMLSkin(FXForm fxForm, URL url) {
        super(fxForm);
        this.url = url;
        configurationStore.addConfigurer(new Configurer<ElementController>() {

            public void configure(ElementController elementController) {
                Node node = lookupEditor(elementController);
                if (elementController instanceof PropertyElementController) {
                    final PropertyElementController propertyElementController = (PropertyElementController) elementController;
                    final Property defaultProperty = getDefaultProperty(node);
                    if (!editorListenerMap.containsKey(elementController)) {
                        editorListenerMap.put(elementController, new ChangeListener() {
                            public void changed(ObservableValue observableValue, Object o, Object o1) {
                                propertyElementController.setValue(o1);
                            }
                        });
                    }
                    defaultProperty.addListener(editorListenerMap.get(elementController));
                    propertyElementController.setValue(elementController.getValue());
                    if (!propertyElementListenerMap.containsKey(elementController)) {
                        propertyElementListenerMap.put(elementController, new ChangeListener() {
                            public void changed(ObservableValue observableValue, Object o, Object o1) {
                                defaultProperty.setValue(o1);
                            }
                        });
                    }
                    propertyElementController.addListener(propertyElementListenerMap.get(elementController));
                }
            }

            public void unconfigure(ElementController elementController) {
                Node node = lookupEditor(elementController);
                if (elementController instanceof PropertyElementController) {
                    PropertyElementController propertyElementController = (PropertyElementController) elementController;
                    Property defaultProperty = getDefaultProperty(node);
                    defaultProperty.removeListener(editorListenerMap.get(elementController));
                    propertyElementController.removeListener(propertyElementListenerMap.get(elementController));
                }
            }

        });
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
    protected void removeControllers(List<ElementController> removed) {
        configurationStore.removeAll(configurationStore);
    }

    @Override
    protected void addControllers(List<ElementController> addedSubList) {
        configurationStore.addAll(addedSubList);
    }

    private Node lookupEditor(ElementController elementController) {
        return getNode().lookup(elementController.getElement().getField().getName() + FXForm.EDITOR_ID_SUFFIX);
    }

    private Property getDefaultProperty(Node node) {
        return defaultPropertyProvider.getDefaultProperty(node);
    }

}