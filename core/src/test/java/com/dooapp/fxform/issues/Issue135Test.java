package com.dooapp.fxform.issues;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.JavaFXRule;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 10/04/2017
 * Time: 15:09
 */
public class Issue135Test {

    @Rule
    public JavaFXRule javaFXRule = new JavaFXRule();

    class TestBean {

        StringProperty name = new SimpleStringProperty();

        public String getName() {
            return name.get();
        }

        public StringProperty nameProperty() {
            return name;
        }

        public void setName(String name) {
            this.name.set(name);
        }

    }

    @Test
    public void test() {
        TestBean testBean1 = new TestBean();
        TestBean testBean2 = new TestBean();
        testBean1.setName("a");
        testBean2.setName("b");
        FXForm fxForm = new FXForm();
        for (int i = 0; i < 10000; i++) {
            fxForm.setSource(i % 2 == 0 ? testBean1 : testBean2);
        }
        Assert.assertEquals("a", testBean1.getName());
        Assert.assertEquals("b", testBean2.getName());
    }

}
