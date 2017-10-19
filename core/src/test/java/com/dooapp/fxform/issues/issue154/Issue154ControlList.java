package com.dooapp.fxform.issues.issue154;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;

public class Issue154ControlList extends Control {
    private ListProperty<Object> items = new SimpleListProperty(FXCollections.observableArrayList());

    public ObservableList<Object> getItems() {
        return items.get();
    }

    public ListProperty<Object> itemsProperty() {
        return items;
    }

    public void setItems(ObservableList<Object> items) {
        this.items.set(items);
    }
}
