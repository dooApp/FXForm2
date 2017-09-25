package com.dooapp.fxform.sampler.samples;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.sampler.FXFormSample;
import com.dooapp.fxform.sampler.Utils;
import com.dooapp.fxform.annotation.Accessor;
import com.dooapp.fxform.builder.FXFormBuilder;
import com.dooapp.fxform.sampler.model.Movies;
import com.dooapp.fxform.reflection.MultipleBeanSource;
import javafx.beans.property.*;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * @author Bastien
 */
public class MultiBeanForm extends FXFormSample {

    @Override
    public String getSampleName() {
        return "Multiple bean form";
    }

    @Accessor(value = Accessor.AccessType.FIELD)
    public class User {

        public StringProperty firstName = new SimpleStringProperty();
        public StringProperty lastName = new SimpleStringProperty();
        public IntegerProperty age = new SimpleIntegerProperty(10);
        public ObjectProperty<Movies> favoriteMovie = new SimpleObjectProperty<>();
        public BooleanProperty coolDeveloper = new SimpleBooleanProperty();
        public ObjectProperty<Address> address = new SimpleObjectProperty<>(new Address());


    }

    @Accessor(value = Accessor.AccessType.FIELD)
    public class Address {

        StringProperty street = new SimpleStringProperty();
        StringProperty city = new SimpleStringProperty();
        StringProperty zip = new SimpleStringProperty();
    }

    @Override
    public Node getPanel(Stage stage) {
        Pane root = new Pane();

        FXForm form = new FXFormBuilder<>()
                .includeAndReorder("firstName", "lastName", "age", "favoriteMovie", "coolDeveloper", "street", "city", "zip")
                .resourceBundle(Utils.SAMPLE)
                .build();
        User user = new User();
        form.setSource(new MultipleBeanSource(user, user.address.get()));

        root.getChildren().add(form);
        return root;
    }

    @Override
    public String getControlStylesheetURL() {
        return null;
    }

    @Override
    public String getSampleDescription() {
        return "This is an example of how to do a very basic form with more than one bean";
    }
}
