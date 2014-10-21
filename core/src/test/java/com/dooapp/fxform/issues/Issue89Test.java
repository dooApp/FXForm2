package com.dooapp.fxform.issues;

import com.dooapp.fxform.AbstractFXForm;
import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.JavaFXRule;
import com.dooapp.fxform.controller.ElementController;
import com.dooapp.fxform.filter.ExcludeFilter;
import com.dooapp.fxform.model.Element;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * TODO write documentation<br>
 * <br>
 * Created at 21/10/14 11:41.<br>
 *
 * @author Vianney
 */
public class Issue89Test {
    @Rule
    public JavaFXRule javaFXRule = new JavaFXRule();

    private class TestBean {
        private StringProperty age = new SimpleStringProperty(this, "age");
        private StringProperty mail = new SimpleStringProperty(this, "mail");

        private String getAge() {
            return age.get();
        }

        private StringProperty ageProperty() {
            return age;
        }

        private void setAge(String age) {
            this.age.set(age);
        }

        private String getMail() {
            return mail.get();
        }

        private StringProperty mailProperty() {
            return mail;
        }

        private void setMail(String mail) {
            this.mail.set(mail);
        }
    }

    @Test
    public void testToAddAFilter() throws NoSuchFieldException, IllegalAccessException {
        TestBean testTestBean = new TestBean();
        FXForm fxForm = new FXForm(testTestBean);

        Field field = AbstractFXForm.class.getDeclaredField("controllers");
        field.setAccessible(true);
        MapProperty<Element, ElementController> controllers = (MapProperty<Element, ElementController>) field.get(fxForm);

        Assert.assertEquals(2, controllers.size());
        fxForm.addFilters(new ExcludeFilter("age"));
        Assert.assertEquals(1, controllers.size());
    }
}
