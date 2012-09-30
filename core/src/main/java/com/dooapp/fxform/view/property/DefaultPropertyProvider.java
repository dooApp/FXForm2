package com.dooapp.fxform.view.property;

import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 30/09/12
 * Time: 09:50
 */
public class DefaultPropertyProvider implements PropertyProvider {

    private final Map<Class<? extends Node>, PropertyProvider> map = new HashMap<Class<? extends Node>, PropertyProvider>();

    @Override
    public Property getProperty(Node node) {
        if (node == null)
            return null;
        for (Class clazz : map.keySet()) {
            if (clazz.isAssignableFrom(node.getClass())) {
                return map.get(clazz).getProperty(node);
            }
        }
        return null;
    }

    public DefaultPropertyProvider() {
        map.put(Label.class, new PropertyProvider<Label>() {
            @Override
            public Property getProperty(Label node) {
                return node.textProperty();
            }
        });
        map.put(TextField.class, new PropertyProvider<TextField>() {
            @Override
            public Property getProperty(TextField node) {
                return node.textProperty();
            }
        });
        map.put(TextArea.class, new PropertyProvider<TextArea>() {
            @Override
            public Property getProperty(TextArea node) {
                return node.textProperty();
            }
        });
        map.put(ChoiceBox.class, new PropertyProvider<ChoiceBox>() {
            @Override
            public Property getProperty(ChoiceBox node) {
                return new ChoiceBoxDefaultProperty(node);
            }
        });
        map.put(CheckBox.class, new PropertyProvider<CheckBox>() {
            @Override
            public Property getProperty(CheckBox node) {
                return node.selectedProperty();
            }
        });
        map.put(ToggleButton.class, new PropertyProvider<ToggleButton>() {
            @Override
            public Property getProperty(ToggleButton node) {
                return node.selectedProperty();
            }
        });
    }
}
