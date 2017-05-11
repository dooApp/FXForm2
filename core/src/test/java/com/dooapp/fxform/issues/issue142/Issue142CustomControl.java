package com.dooapp.fxform.issues.issue142;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;

/**
 * Created by KEVIN on 09/05/2017.
 */
public class Issue142CustomControl extends Control {

    private ListProperty<String> values = new SimpleListProperty<>();

    public Issue142CustomControl() {

    }

    public ObservableList<String> getValues() {
        return values.get();
    }

    public ListProperty<String> valuesProperty() {
        return values;
    }

    public void setValues(ObservableList<String> values) {
        this.values.set(values);
    }
}
