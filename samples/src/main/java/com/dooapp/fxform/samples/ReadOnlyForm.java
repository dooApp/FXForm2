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
public class ReadOnlyForm extends FXFormSample {

    @Accessor(value = Accessor.AccessType.FIELD)
    public class User {

        public StringProperty firstName = new SimpleStringProperty();
        public StringProperty lastName = new SimpleStringProperty();
        public IntegerProperty age = new SimpleIntegerProperty(10);
        public ObjectProperty<Movies> favoriteMovie = new SimpleObjectProperty<>();
        public BooleanProperty coolDeveloper = new SimpleBooleanProperty();
    }

    @Override
    public String getSampleName() {
        return "Read only form";
    }

    @Override
    public Node getPanel(Stage stage) {
        Pane root = new Pane();
        FXForm form = new FXFormBuilder<>()
                .includeAndReorder("firstName", "lastName", "age", "favoriteMovie")
                .resourceBundle(Utils.SAMPLE)
                .readOnly(true)
                .build();

        User user = new User();
        user.firstName.set("Robert");
        user.lastName.set("Shepard");
        user.age.setValue(42);
        user.favoriteMovie.setValue(Movies.LOTR);
        form.setSource(user);

        root.getChildren().add(form);
        return root;
    }

    @Override
    public String getSampleDescription() {
        return "This is how you can do a read only form";
    }
}
