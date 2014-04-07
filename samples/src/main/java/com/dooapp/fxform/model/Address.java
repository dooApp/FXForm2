package com.dooapp.fxform.model;

import com.dooapp.fxform.annotation.Accessor;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * TODO write documentation<br>
 * <br>
 * Created at 27/03/14 17:21.<br>
 *
 * @author Bastien
 */
@Accessor(value = Accessor.AccessType.METHOD)
public class Address {

    StringProperty street = new SimpleStringProperty();
    StringProperty city = new SimpleStringProperty();
    StringProperty zip = new SimpleStringProperty();

    public String getStreet() {
        return street.get();
    }

    public StringProperty streetProperty() {
        return street;
    }

    public String getCity() {
        return city.get();
    }

    public StringProperty cityProperty() {
        return city;
    }

    public String getZip() {
        return zip.get();
    }

    public StringProperty zipProperty() {
        return zip;
    }
}
