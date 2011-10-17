package com.dooapp.fxform;

import org.junit.Assert;
import org.junit.Test;

/**
 * TODO write documentation<br>
 * <br>
 * Created at 17/10/11 11:42.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 * @since 2.2
 */
public class FXFormTest {

    @Test
    public void testIssue2() {
        TestBean testBean = new TestBean();
        FXForm fxForm = new FXForm(testBean);
        Assert.assertEquals(4, fxForm.getControllers().size());
        fxForm.setSource(null);
        Assert.assertEquals(0, fxForm.getControllers().size());
    }

    @Test
    public void testSetSource() {
        TestBean testBean = new TestBean();
        FXForm fxForm = new FXForm();
        Assert.assertEquals(0, fxForm.getControllers().size());
        fxForm.setSource(testBean);
        Assert.assertEquals(4, fxForm.getControllers().size());
    }

}