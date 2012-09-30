package com.dooapp.fxform.view.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;

/**
 * Created at 28/09/12 17:52.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class ConstraintLabel extends VBox {
    /**
     * The logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ConstraintLabel.class);

    private final static Image WARNING = new Image(ConstraintLabel.class.getResource("warning.png").toExternalForm());

    private final ListChangeListener<ConstraintViolation> listChangeListener = new ListChangeListener<ConstraintViolation>() {

        @Override
        public void onChanged(Change<? extends ConstraintViolation> change) {
            getChildren().clear();
            for (Object o : constraint.get()) {
                ConstraintViolation constraintViolation = (ConstraintViolation) o;
                Label errorLabel = new Label(constraintViolation.getMessage());
                ImageView warningView = new ImageView(WARNING);
                warningView.setFitHeight(15);
                warningView.setPreserveRatio(true);
                warningView.setSmooth(true);
                errorLabel.setGraphic(warningView);
                getChildren().add(errorLabel);
            }
        }
    };

    private ObjectProperty<ObservableList<ConstraintViolation>> constraint = new SimpleObjectProperty<ObservableList<ConstraintViolation>>();

    public ObjectProperty<ObservableList<ConstraintViolation>> constraintProperty() {
        return constraint;
    }

    public ConstraintLabel() {
        setAlignment(Pos.CENTER_LEFT);
        constraint.addListener(new ChangeListener<ObservableList<ConstraintViolation>>() {
            @Override
            public void changed(ObservableValue<? extends ObservableList<ConstraintViolation>> observableValue, ObservableList<ConstraintViolation> constraintViolations, ObservableList<ConstraintViolation> constraintViolations1) {
                if (constraintViolations != null) {
                    constraintViolations.removeListener(listChangeListener);
                }
                if (constraintViolations1 != null) {
                    constraintViolations1.addListener(listChangeListener);
                }
            }

        });
    }
}