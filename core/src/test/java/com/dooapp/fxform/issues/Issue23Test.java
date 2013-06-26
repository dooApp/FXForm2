package com.dooapp.fxform.issues;

import com.dooapp.fxform.FXForm;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Created at 26/06/13 17:49.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class Issue23Test {

    public static class Bean {

        private String test = "test";

        public String getTest() {
            return test;
        }

        public void setTest(String test) {
            this.test = test;
        }
    }

    @Test
    public void testIssue23() {
        Bean bean = new Bean();
        FXForm fxForm = new FXForm();
        fxForm.setSource(bean);
        Assert.assertNotNull(bean.getTest());
    }

}