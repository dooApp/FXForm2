package com.dooapp.fxform.samples;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.FXFormSample;
import com.dooapp.fxform.Utils;
import com.dooapp.fxform.annotation.Accessor;
import com.dooapp.fxform.builder.FXFormBuilder;
import com.dooapp.fxform.model.Movies;
import javafx.beans.property.*;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * @author Bastien
 */
public class CuteCssForm extends FXFormSample {

    @Override
    public String getSampleName() {
        return "Form with css";
    }

    @Override
    public Node getPanel(Stage stage) {
        Pane root = new Pane();
        FXForm form = new FXFormBuilder<>()
                .includeAndReorder("firstName", "lastName", "age", "favoriteMovie", "coolDeveloper")
                .resourceBundle(Utils.SAMPLE)
                .build();
        form.getStylesheets().add("form.css");
        User user = new User();
        form.setSource(user);

        root.getChildren().add(form);
        return root;
    }

    @Accessor(value = Accessor.AccessType.FIELD)
    public class User {

        public StringProperty firstName = new SimpleStringProperty();
        public StringProperty lastName = new SimpleStringProperty();
        public IntegerProperty age = new SimpleIntegerProperty(10);
        public ObjectProperty<Movies> favoriteMovie = new SimpleObjectProperty<>();
        public BooleanProperty coolDeveloper = new SimpleBooleanProperty();
    }

    @Override
    public String getSampleDescription() {
        return "A form with really cool colors";
    }
}
