package com.dooapp.fxform.sampler.samples;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.sampler.FXFormSample;
import com.dooapp.fxform.sampler.Utils;
import com.dooapp.fxform.annotation.Accessor;
import com.dooapp.fxform.builder.FXFormBuilder;
import com.dooapp.fxform.sampler.model.Movies;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * @author Bastien
 */
public class ListPropertyForm extends FXFormSample {

    @Override
    public String getSampleName() {
        return "ListProperty form support";
    }

    @Accessor(value = Accessor.AccessType.FIELD)
    public class User {
        public User(String firstName, String lastName, Movies favoriteMovie) {
            this.firstName.setValue(firstName);
            this.lastName.setValue(lastName);
            this.favoriteMovie.setValue(favoriteMovie);
        }

        public StringProperty firstName = new SimpleStringProperty();
        public StringProperty lastName = new SimpleStringProperty();
        public ObjectProperty<Movies> favoriteMovie = new SimpleObjectProperty<>();

        public String getFirstName() {
            return firstName.get();
        }

        public StringProperty firstNameProperty() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName.set(firstName);
        }

        public String getLastName() {
            return lastName.get();
        }

        public StringProperty lastNameProperty() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName.set(lastName);
        }

        public Movies getFavoriteMovie() {
            return favoriteMovie.get();
        }

        public ObjectProperty<Movies> favoriteMovieProperty() {
            return favoriteMovie;
        }

        public void setFavoriteMovie(Movies favoriteMovie) {
            this.favoriteMovie.set(favoriteMovie);
        }
    }

    @Accessor(value = Accessor.AccessType.FIELD)
    public class MyBean {
        public ListProperty<User> users = new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    @Override
    public Node getPanel(Stage stage) {
        Pane root = new Pane();
        MyBean bean = new MyBean();

        bean.users.add(new User("Baggins", "Bilbo", Movies.LOTR));
        bean.users.add(new User("Reeves", "Keanu ", Movies.MATRIX));
        bean.users.add(new User("Robert      ", "Paul          ", Movies.MATRIX));

        FXForm form = new FXFormBuilder<>().source(bean).resourceBundle(Utils.SAMPLE).build();
        stage.getScene().getStylesheets().add("list.css");
        form.setMinWidth(500);
        root.getChildren().add(form);
        return root;
    }

    @Override
    public String getControlStylesheetURL() {
        return null;
    }

    @Override
    public String getSampleDescription() {
        return "This is an example of how to build a TableView from a ListProperty using FXForm";
    }
}
