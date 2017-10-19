package com.dooapp.fxform.issues.issue154;

import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.control.Control;

public class Issue154ControlMap extends Control {
    private MapProperty<Object, Object> mapItems = new SimpleMapProperty<>(FXCollections.observableHashMap());

    public ObservableMap<Object, Object> getMapItems() {
        return mapItems.get();
    }

    public MapProperty<Object, Object> mapItemsProperty() {
        return mapItems;
    }

    public void setMapItems(ObservableMap<Object, Object> mapItems) {
        this.mapItems.set(mapItems);
    }
}
