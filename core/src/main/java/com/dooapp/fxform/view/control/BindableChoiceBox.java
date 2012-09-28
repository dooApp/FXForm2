package com.dooapp.fxform.view.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ChoiceBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A ChoiceBox that expose a bi-directionnal bindable selected item property.
 * <p/>
 * Created at 28/09/12 11:12.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class BindableChoiceBox<T> extends ChoiceBox<T> {
    /**
     * The logger
     */
    private static final Logger logger = LoggerFactory.getLogger(BindableChoiceBox.class);

    private final ObjectProperty<T> selectedItem = new SimpleObjectProperty<T>();

    public BindableChoiceBox() {
        selectedItem.addListener(new ChangeListener<T>() {
            public void changed(ObservableValue<? extends T> observableValue, T t, T t1) {
                getSelectionModel().select(t1);
            }
        });
        getSelectionModel().selectedItemProperty().addListener(new ChangeListener<T>() {
            public void changed(ObservableValue<? extends T> observableValue, T t, T t1) {
                selectedItemProperty().set(t1);
            }
        });
    }

    public ObjectProperty<T> selectedItemProperty() {
        return selectedItem;
    }

}