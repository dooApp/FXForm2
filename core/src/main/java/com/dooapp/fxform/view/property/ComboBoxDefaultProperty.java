package com.dooapp.fxform.view.property;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;

/**
 * A virtual property to expose a Property for a ChoiceBox corresponding to the selected item.
 * <p/>
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 19/03/13
 * Time: 15:12
 */
public class ComboBoxDefaultProperty extends SimpleObjectProperty<Object> implements Property<Object> {

    private final ComboBox comboBox;

    public ComboBoxDefaultProperty(ComboBox comboBox) {
        this.comboBox = comboBox;
        addListener(new ChangeListener<Object>() {
            public void changed(ObservableValue<? extends Object> observableValue, Object t, Object t1) {
                ComboBoxDefaultProperty.this.comboBox.getSelectionModel().select(t1);
            }
        });
        this.comboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            public void changed(ObservableValue<? extends Object> observableValue, Object t, Object t1) {
                set(t1);
            }
        });
    }

}
