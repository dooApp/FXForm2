package com.dooapp.fxform.issues;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.JavaFXRule;
import com.dooapp.fxform.view.NodeType;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.util.ResourceBundle;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 15/05/2017
 * Time: 17:12
 */
public class Issue144Test {

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
        FXForm fxForm = new FXForm();
        fxForm.setResourceBundle(ResourceBundle.getBundle("Issue144Test"));
        fxForm.setSource(new TestBean());
        Assert.assertEquals("Nom",
                fxForm.getResourceProvider().getString(fxForm.getElements().get(0), NodeType.LABEL).get());
    }

}
