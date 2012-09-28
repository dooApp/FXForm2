package com.dooapp.fxform.view.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;

/**
 * Created at 28/09/12 17:52.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class ConstraintLabel extends Label {
    /**
     * The logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ConstraintLabel.class);

    private ObjectProperty<ObservableList<ConstraintViolation>> constraint = new SimpleObjectProperty<ObservableList<ConstraintViolation>>();

    public ObjectProperty<ObservableList<ConstraintViolation>> constraintProperty() {
        return constraint;
    }

}