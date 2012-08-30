package com.dooapp.fxform.view.factory;

import com.dooapp.fxform.controller.ElementController;
import com.dooapp.fxform.view.NodeCreationException;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;

/**
 * Created at 30/08/12 15:53.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class ConstraintNodeFactory implements NodeFactory {
    /**
     * The logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ConstraintNodeFactory.class);

    private final static Image WARNING = new Image(ConstraintNodeFactory.class.getResource("warning.png").toExternalForm());

    public DisposableNode createNode(final ElementController controller) throws NodeCreationException {
        final VBox constraintsBox = new VBox();
        constraintsBox.setAlignment(Pos.CENTER_LEFT);
        controller.getConstraintViolations().addListener(new ListChangeListener() {
            public void onChanged(Change change) {
                constraintsBox.getChildren().clear();
                for (Object o : controller.getConstraintViolations()) {
                    ConstraintViolation constraintViolation = (ConstraintViolation) o;
                    Label errorLabel = new Label(constraintViolation.getMessage());
                    ImageView warningView = new ImageView(WARNING);
                    warningView.setFitHeight(15);
                    warningView.setPreserveRatio(true);
                    warningView.setSmooth(true);
                    errorLabel.setGraphic(warningView);
                    constraintsBox.getChildren().add(errorLabel);
                }
            }
        });
        return new DisposableNodeWrapper(constraintsBox, new Callback<Node, Void>() {
            public Void call(Node node) {
                // nothing to dispose
                return null;
            }
        });
    }
}