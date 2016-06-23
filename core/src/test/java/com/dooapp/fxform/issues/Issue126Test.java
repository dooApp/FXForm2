package com.dooapp.fxform.issues;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.JavaFXRule;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;
import junit.framework.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 22/06/2016
 * Time: 11:58
 */
public class Issue126Test {

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
    public void test() throws InterruptedException {
        TestBean testBean = new TestBean();
        // create two forms for the same bean instance
        FXForm form1 = new FXForm(testBean);
        FXForm form2 = new FXForm(testBean);
        int numberOfUpdates = 10;
        final int[] updateCount = {0};
        testBean.nameProperty().addListener((observable, oldValue, newValue) -> {
            updateCount[0]++;
        });
        for (int i = 0; i < numberOfUpdates; i++) {
            TextField textField = ((TextField) form1.lookup("#name-form-editor"));
            int finalI = i;
            Platform.runLater(() -> textField.setText(textField.getText() + finalI));
        }
        // wait 1 second to let a chance to unexpected model updates to be executed
        Thread.sleep(1000);
        Assert.assertEquals(numberOfUpdates, updateCount[0]);
    }

}
