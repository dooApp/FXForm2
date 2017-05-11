package com.dooapp.fxform.issues.issue142;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class Issue142Bean {
    ListProperty<String> values = new SimpleListProperty<>(FXCollections.observableArrayList());

    public Issue142Bean() {
        values.addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> c) {
                while (c.next()) {

                }
            }
        });
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
