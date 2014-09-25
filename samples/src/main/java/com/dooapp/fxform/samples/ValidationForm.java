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

import javax.validation.constraints.Min;

/**
 * @author Bastien
 */
public class ValidationForm extends FXFormSample {

    @Accessor(value = Accessor.AccessType.FIELD)
    public class UserWithConstraintsValidation {

        private StringProperty firstName = new SimpleStringProperty();
        private StringProperty lastName = new SimpleStringProperty();
        private IntegerProperty age = new SimpleIntegerProperty(10);
        private ObjectProperty<Movies> favoriteMovie = new SimpleObjectProperty<>();
        private BooleanProperty coolDeveloper = new SimpleBooleanProperty();
        @Min(value = 5)
        public int getAge() {
            return age.get();
        }

        public IntegerProperty ageProperty() {
            return age;
        }
    }

    @Override
    public String getSampleName() {
        return "Simple validation form";
    }

    @Override
    public Node getPanel(Stage stage) {
        Pane root = new Pane();

        FXForm form = new FXFormBuilder<>()
                .includeAndReorder("firstName", "lastName", "age", "favoriteMovie", "coolDeveloper")
                .resourceBundle(Utils.SAMPLE)
                .build();
        UserWithConstraintsValidation user = new UserWithConstraintsValidation();
        form.setSource(user);

        root.getChildren().add(form);
        return root;
    }

    @Override
    public String getSampleDescription() {
        return "A form with field validation, age must be greater than 5";
    }
}
