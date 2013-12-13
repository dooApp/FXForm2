package com.dooapp.fxform.reflection.classcast;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.JavaFXRule;
import com.dooapp.fxform.filter.IncludeFilter;
import org.junit.Rule;
import org.junit.Test;

/**
 * TODO write documentation<br>
 * <br>
 * Created at 13/12/13 17:01.<br>
 *
 * @author Bastien
 */
public class ClassCastTest {

    @Rule
    public JavaFXRule javaFXRule = new JavaFXRule();

    @Test
    public void classCastTest() {
        FXForm fxForm = new FXForm();
        fxForm.addFilters(new IncludeFilter("model"));
        Device device = new Device();
        device.setModel(new DeviceModel());
        fxForm.setSource(device);
    }

}
