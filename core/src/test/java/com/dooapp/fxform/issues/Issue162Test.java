package com.dooapp.fxform.issues;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.JavaFXRule;
import com.dooapp.fxform.model.Element;
import javafx.beans.property.Property;
import org.hibernate.validator.constraints.Email;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 * Thanks to https://github.com/fblan (Blanc Frederic) for providing this test.
 * See https://github.com/dooApp/FXForm2/issues/162
 * <p>
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 19/01/2018
 * Time: 10:38
 */
public class Issue162Test {

    @Rule
    public JavaFXRule javaFXRule = new JavaFXRule();

    public class TestBean {
        @Email
        //initial value is a valid email
        private String text = "test@g";

        public String getText() {
            return text;
        }

        public void setText(String s) {
            this.text = s;
        }

    }

    @Test
    public void test() {
        TestBean test = new TestBean();
        FXForm form = new FXForm(test);
        int nbError = form.getConstraintViolations().size();
        Assert.assertEquals(0, nbError);
        Element text = form.getElements().get(0);
        Property textEditorProperty = form.getController(text).getEditorController().getNode().getProperty();
        //setting an invalid email, so one constraint violation expected
        textEditorProperty.setValue("test@");
        nbError = form.getConstraintViolations().size();
        Assert.assertEquals(1, nbError);
        //setting back previous valid email, so no constraint violation expected
        textEditorProperty.setValue("test@g");
        nbError = form.getConstraintViolations().size();
        Assert.assertEquals(0, nbError);
    }

}
