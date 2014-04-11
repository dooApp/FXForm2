package com.dooapp.fxform.model;

import com.dooapp.fxform.annotation.Accessor;
import com.dooapp.fxform.annotation.FormFactory;
import com.dooapp.fxform.view.factory.impl.TextAreaFactory;
import javafx.beans.property.*;

/**
 * TODO write documentation<br>
 *<br>
 * Created at 11/04/14 15:02.<br>
 *
 * @author Bastien
 *
 */
@Accessor(value = Accessor.AccessType.METHOD)
public class UserWithCustomFactory {
    private StringProperty firstName = new SimpleStringProperty();
    private StringProperty lastName = new SimpleStringProperty();
    private IntegerProperty age = new SimpleIntegerProperty(10);
    private ObjectProperty<Movies> favoriteMovie = new SimpleObjectProperty<>();
    private BooleanProperty coolDeveloper = new SimpleBooleanProperty();
    private ObjectProperty<Address> address = new SimpleObjectProperty<>(new Address());

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    @FormFactory(TextAreaFactory.class)
    public StringProperty lastNameProperty() {
        return lastName;
    }


    public IntegerProperty ageProperty() {
        return age;
    }

    public BooleanProperty coolDeveloperProperty() {
        return coolDeveloper;
    }


    public ObjectProperty<Address> addressProperty() {
        return address;
    }

    public ObjectProperty<Movies> favoriteMovieProperty() {
        return favoriteMovie;
    }


}
