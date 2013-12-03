package com.dooapp.fxform.resource;

import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.view.NodeType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;

import java.util.ResourceBundle;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 03/12/2013
 * Time: 11:26
 */
public interface ResourceProvider {

    public StringProperty getString(Element element, NodeType nodeType);

    public ResourceBundle getResourceBundle();

    public ObjectProperty<ResourceBundle> resourceBundleProperty();

    public void setResourceBundle(ResourceBundle resourceBundle);

}
