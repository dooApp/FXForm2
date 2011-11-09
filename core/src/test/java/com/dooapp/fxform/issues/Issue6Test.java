package com.dooapp.fxform.issues;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.ReadOnlyFXForm;
import com.dooapp.fxform.TestBean;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test issue 6 fix<br>
 * <br>
 * Created at 09/11/11 18:08.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class Issue6Test {
    /**
     * The logger
     */
    private static final Logger logger = LoggerFactory.getLogger(Issue6Test.class);

    @Test
    public void testIssue6() {
        FXForm readOnlyFXForm = new ReadOnlyFXForm();
        readOnlyFXForm.setSource(null);
        readOnlyFXForm.setSource(new TestBean());
        readOnlyFXForm.setSource(null);
        readOnlyFXForm.setSource(new TestBean());
    }

}