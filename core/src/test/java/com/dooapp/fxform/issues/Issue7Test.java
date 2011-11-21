package com.dooapp.fxform.issues;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.TestBean;
import com.dooapp.fxform.view.FXFormSkinFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test for issue #7<br>
 * <br>
 * Created at 21/11/11 17:57.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class Issue7Test {
    /**
     * The logger
     */
    private static final Logger logger = LoggerFactory.getLogger(Issue7Test.class);

    @Test
    public void testIssue7() {
        FXForm fxForm = new FXForm();
        fxForm.setSource(new TestBean());
        fxForm.setSkin(FXFormSkinFactory.INLINE_FACTORY.createSkin(fxForm));
        fxForm.setSource(null);
        org.junit.Assert.assertEquals(0, fxForm.getControllers().size());
    }

}