package com.dooapp.fxform.issues;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.ReadOnlyFXForm;
import com.dooapp.fxform.TestBean;
import org.junit.Test;

/**
 * Test issue 6 fix<br>
 * <br>
 * Created at 09/11/11 18:08.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class Issue6Test {

    @Test
    public void testIssue6() {
        FXForm readOnlyFXForm = new ReadOnlyFXForm();
        readOnlyFXForm.setSource(null);
        readOnlyFXForm.setSource(new TestBean());
        readOnlyFXForm.setSource(null);
        readOnlyFXForm.setSource(new TestBean());
    }

}