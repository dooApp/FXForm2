package com.dooapp.fxform.view.property;

import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ChoiceBox;

/**
 * A virtual property to expose a Property for a ChoiceBox corresponding to the selected item.
 * <p/>
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 30/09/12
 * Time: 11:25
 */
public class ChoiceBoxDefaultProperty implements Property<Object> {

    private final ChoiceBox choiceBox;

    private final ObjectProperty property = new SimpleObjectProperty();

    public ChoiceBoxDefaultProperty(ChoiceBox choiceBox) {
        this.choiceBox = choiceBox;
        property.addListener(new ChangeListener<Object>() {
            public void changed(ObservableValue<? extends Object> observableValue, Object t, Object t1) {
                ChoiceBoxDefaultProperty.this.choiceBox.getSelectionModel().select(t1);
            }
        });
        this.choiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            public void changed(ObservableValue<? extends Object> observableValue, Object t, Object t1) {
                property.set(t1);
            }
        });
    }

    @Override
    public void bind(ObservableValue<? extends Object> observableValue) {
        property.bind(observableValue);
    }

    @Override
    public void unbind() {
        property.unbind();
    }

    @Override
    public boolean isBound() {
        return property.isBound();
    }

    @Override
    public void bindBidirectional(Property<Object> objectProperty) {
        property.bindBidirectional(objectProperty);
    }

    @Override
    public void unbindBidirectional(Property<Object> objectProperty) {
        objectProperty.unbindBidirectional(objectProperty);
    }

    @Override
    public Object getBean() {
        return property.getBean();
    }

    @Override
    public String getName() {
        return property.getName();
    }

    @Override
    public void addListener(ChangeListener<? super Object> changeListener) {
        property.addListener(changeListener);
    }

    @Override
    public void removeListener(ChangeListener<? super Object> changeListener) {
        property.removeListener(changeListener);
    }

    @Override
    public Object getValue() {
        return property.getValue();
    }

    @Override
    public void setValue(Object o) {
        property.setValue(o);
    }

    @Override
    public void addListener(InvalidationListener invalidationListener) {
        property.addListener(invalidationListener);
    }

    @Override
    public void removeListener(InvalidationListener invalidationListener) {
        property.removeListener(invalidationListener);
    }
}
