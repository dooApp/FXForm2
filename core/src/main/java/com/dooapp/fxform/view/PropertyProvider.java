package com.dooapp.fxform.view;

import javafx.beans.property.Property;
import javafx.scene.Node;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 30/09/12
 * Time: 09:49
 */
public interface PropertyProvider<N extends Node> {

    /**
     * Provides the default property for this node.
     *
     * @param node
     * @return
     */
    public Property getProperty(N node);

}
