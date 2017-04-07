package com.dooapp.fxform.model;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.JavaFXRule;
import com.dooapp.fxform.builder.FXFormBuilder;
import javafx.scene.control.TextField;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.validation.constraints.Size;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Tests for {@link com.dooapp.fxform.model.BufferedElementFactory}.
 *
 * @author Stefan Endrullis (endrullis@iat.uni-leipzig.de)
 */
public class BufferedElementFactoryTest {

    public static class TestBean {

        @Size(min = 1, max = 5, message = "name must not be longer than 5 characters")
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    private TestBean bean;
    private FXForm form;

    @Rule
    public JavaFXRule javaFXRule = new JavaFXRule();

    @Before
    public void setup() {
        bean = new TestBean();
        bean.setName("1");

        form = new FXFormBuilder()
                .buffered(true, true)
                .build();
    }

    @Test
    public void testForm() throws InterruptedException {
        // assign bean -> buffered value has to get updated
        form.sourceProperty().setValue(bean);
        TextField nameField = ((TextField) form.lookup("#name-form-editor"));
        assertEquals("1", bean.getName());
        assertEquals("1", nameField.getText());

        // set buffered value, but don't commit value -> bean value must not change
        nameField.setText("2");
        assertEquals("1", bean.getName());

        // commit value to bean -> bean value must change
        form.commit();
        assertEquals("2", bean.getName());

        // change bean value -> buffered value must not change
        bean.setName("3");
        assertEquals("2", nameField.getText());

        // refresh buffered value -> value must change
        form.reload();
        assertEquals("3", nameField.getText());

        // set an invalid value -> commit has to fail and bean must not get updated
        nameField.setText("123456");
        assertFalse(form.commit());
        assertEquals("3", bean.getName());
    }

}