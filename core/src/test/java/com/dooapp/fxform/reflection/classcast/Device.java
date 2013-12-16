package com.dooapp.fxform.reflection.classcast;

import com.dooapp.fxform.annotation.Accessor;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * A Device use for testing.
 * <br>
 * Created at 13/12/13 16:58.<br>
 *
 * @author Bastien
 */
@Accessor(Accessor.AccessType.METHOD)
public class Device<MODEL extends DeviceModel> extends SuperDevice<Device<?>> {

    private ObjectProperty<MODEL> model;

    public ObjectProperty<MODEL> modelProperty() {
        if (this.model == null) {
            this.model = createModelProperty();
        }
        return this.model;
    }

    protected ObjectProperty<MODEL> createModelProperty() {
        return new SimpleObjectProperty<MODEL>();
    }

    public MODEL getModel() {
        return modelProperty().get();
    }

    public void setModel(MODEL myModel) {
        this.modelProperty().set(myModel);
    }
}
