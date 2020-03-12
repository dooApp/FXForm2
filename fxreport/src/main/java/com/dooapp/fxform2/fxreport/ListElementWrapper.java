package com.dooapp.fxform2.fxreport;

import com.dooapp.fxform.model.impl.PropertyElementWrapper;
import javafx.beans.property.MapProperty;
import javafx.beans.property.Property;

import java.util.List;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 11/03/2020
 * Time: 11:38
 */
public class ListElementWrapper extends PropertyElementWrapper<MapProperty> {

    private final List<String> fields;

    public ListElementWrapper(Property<MapProperty> property, List<String> fields) {
        super(property, MapProperty.class);
        this.fields = fields;
    }

    public List<String> getFields() {
        return fields;
    }

}
