package com.dooapp.fxform.issues;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.JavaFXRule;
import com.dooapp.fxform.annotation.Accessor;
import com.dooapp.fxform.annotation.FormFactory;
import com.dooapp.fxform.view.factory.impl.TextAreaFactory;
import com.dooapp.fxform.view.skin.FXMLSkin;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.net.URL;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 16/11/2018
 * Time: 15:36
 */
public class Issue170Test {

    @Rule
    public JavaFXRule javaFXRule = new JavaFXRule();

    @Test
    public void testWithoutScrollPane() {
        FXForm<User> form = new FXForm<>();
        User user = new User();
        URL fxmlUrl = Issue170Test.class.getClassLoader().getResource("issues/issue170/issue170_no_scrollpane.fxml");
        form.setSkin(new FXMLSkin(form, fxmlUrl));
        form.setSource(user);
        Assert.assertNotNull(form.getController(form.getElements()
                .stream()
                .filter(element -> "firstName".equals(element.getName()))
                .findFirst().get())
                .getEditorController().getNode());
    }

    @Test
    public void testWithScrollPane() {
        FXForm<User> form = new FXForm<>();
        User user = new User();
        URL fxmlUrl = Issue170Test.class.getClassLoader().getResource("issues/issue170/issue170.fxml");
        form.setSkin(new FXMLSkin(form, fxmlUrl));
        form.setSource(user);
        Assert.assertNotNull(form.getController(form.getElements()
                .stream()
                .filter(element -> "firstName".equals(element.getName()))
                .findFirst().get())
                .getEditorController().getNode());
    }

    @Accessor(value = Accessor.AccessType.FIELD)
    public class User {

        public StringProperty firstName = new SimpleStringProperty("Jon");

        @FormFactory(TextAreaFactory.class)
        public StringProperty lastName = new SimpleStringProperty("Smith");

        public IntegerProperty age = new SimpleIntegerProperty(10);

    }

}
