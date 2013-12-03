package com.dooapp.fxform.resource;

import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.view.NodeType;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ResourceBundle;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 03/12/2013
 * Time: 11:28
 */
public class DefaultResourceProvider implements ResourceProvider {

    private final ObjectProperty<ResourceBundle> resourceBundle = new SimpleObjectProperty<ResourceBundle>();

    @Override
    public StringProperty getString(final Element element, final NodeType nodeType) {
        SimpleStringProperty string = new SimpleStringProperty();
        string.bind(new StringBinding() {
            {
                bind(resourceBundle);
            }

            @Override
            protected String computeValue() {
                String label;
                try {
                    label = getResourceBundle().getString(element.getBean().getClass().getSimpleName()
                            + "-" + element.getName()
                            + "-" + nodeType.name().toLowerCase());
                } catch (Exception e) {
                    // Look for a generic label without the bean name
                    try {
                        label = getResourceBundle().getString(element.getName()
                                + "-" + nodeType.name().toLowerCase());
                    } catch (Exception e1) {
                        // Label is undefined in the resource bundle
                        label = handleDefaultValue(element.getName(), nodeType);
                    }
                }
                return label;
            }
        });
        return string;
    }

    private String handleDefaultValue(String name, NodeType nodeType) {
        if (nodeType == NodeType.LABEL) {
            return name;
        } else {
            return null;
        }
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle.get();
    }

    public ObjectProperty<ResourceBundle> resourceBundleProperty() {
        return resourceBundle;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle.set(resourceBundle);
    }
}
