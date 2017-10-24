package com.dooapp.fxform.issues;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.JavaFXRule;
import org.junit.Rule;
import org.junit.Test;

public class Issue155Test {

    @Rule
    public JavaFXRule javaFXRule = new JavaFXRule();

    public class TestBean {
        private Object object = new Object();

        public Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }
    }

    @Test
    public void testIssue155() {
        TestBean testBean = new TestBean();
        FXForm form = new FXForm(testBean);
    }
}
