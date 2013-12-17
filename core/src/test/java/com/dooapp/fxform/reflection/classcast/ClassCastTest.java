package com.dooapp.fxform.reflection.classcast;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.filter.IncludeFilter;
import org.junit.Test;

/**
 * This class test if the ClassCastException is not reproduced.
 * The classCast come when you add this to the Device class "extends SuperDevice<Device<?>>"
 * <br>
 * Created at 13/12/13 17:01.<br>
 *
 * @author Bastien
 */
public class ClassCastTest {

    @Test
    public void classCastTest() {
        FXForm fxForm = new FXForm();
        fxForm.addFilters(new IncludeFilter("model"));
        Device device = new Device();
        device.setModel(new DeviceModel());
        fxForm.setSource(device);
    }

}
