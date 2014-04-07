package com.dooapp.fxform.model;

import com.dooapp.fxform.annotation.Accessor;
import javafx.beans.property.*;

/**
 * TODO write documentation<br>
 *<br>
 * Created at 04/04/14 14:03.<br>
 *
 * @author Bastien
 *
 */
@Accessor(value = Accessor.AccessType.METHOD)
public class User {

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

    public int getAge() {
        return age.get();
    }

    public IntegerProperty ageProperty() {
        return age;
    }

    public void setAge(int age) {
        this.age.set(age);
    }

    public boolean getCoolDeveloper() {
        return coolDeveloper.get();
    }

    public BooleanProperty coolDeveloperProperty() {
        return coolDeveloper;
    }

    public void setCoolDeveloper(boolean coolDeveloper) {
        this.coolDeveloper.set(coolDeveloper);
    }

    public Address getAddress() {
        return address.get();
    }

    public ObjectProperty<Address> addressProperty() {
        return address;
    }

    public void setAddress(Address address) {
        this.address.set(address);
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

