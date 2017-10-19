package com.dooapp.fxform.model.impl;

import com.dooapp.fxform.model.FormException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link com.dooapp.fxform.model.impl.BufferedPropertyElement}.
 *
 * @author Stefan Endrullis (endrullis@iat.uni-leipzig.de)
 */
public class BufferedPropertyElementTest {

    public static class TestBean {

        private StringProperty name;

        public StringProperty nameProperty() {
            if (name == null) {
                name = new SimpleStringProperty("1");
            }
            return name;
        }

    }

    @Test
    public void testBufferingOfUserInputOnly() throws NoSuchFieldException, NoSuchMethodException, FormException {
        TestBean testBean = new TestBean();
        Field field = TestBean.class.getDeclaredField("name");
        BufferedPropertyElement<String> tested = new BufferedPropertyElement<>(new PropertyMethodElement<String,String>(field), true, false);

        // assign bean -> buffered value has to get updated
        tested.sourceProperty().setValue(testBean);
        assertEquals("1", tested.getValue());

        // set buffered value, but don't commit value -> bean value must not change
        tested.setValue("2");
        assertEquals("1", testBean.name.get());

        // commit value to bean -> bean value must change
        tested.commit();
        assertEquals("2", testBean.name.get());

        // change bean value (not buffered) -> value must change
        testBean.name.set("3");
        assertEquals("3", tested.getValue());

        // reload should not change anything
        tested.reload();
        assertEquals("3", tested.getValue());
    }

    @Test
    public void testBufferingOfBeanChangeOnly() throws NoSuchFieldException, NoSuchMethodException, FormException {
        TestBean testBean = new TestBean();
        Field field = TestBean.class.getDeclaredField("name");
        BufferedPropertyElement<String> tested = new BufferedPropertyElement<>(new PropertyMethodElement<String,String>(field), false, true);

        // assign bean -> buffered value has to get updated
        tested.sourceProperty().setValue(testBean);
        assertEquals("1", tested.getValue());

        // set value (not buffered) -> bean value must change
        tested.setValue("2");
        assertEquals("2", testBean.name.get());

        // commit should not change anything
        tested.commit();
        assertEquals("2", testBean.name.get());

        // change bean value -> buffered value must not change
        testBean.name.set("3");
        assertEquals("2", tested.getValue());

        // refresh buffered value -> value must change
        tested.reload();
        assertEquals("3", tested.getValue());
    }

    @Test
    public void testBufferingOfUserInputAndBeanChange() throws NoSuchFieldException, NoSuchMethodException, FormException {
        TestBean testBean = new TestBean();
        Field field = TestBean.class.getDeclaredField("name");
        BufferedPropertyElement<String> tested = new BufferedPropertyElement<>(new PropertyMethodElement<String,String>(field), true, true);

        // assign bean -> buffered value has to get updated
        tested.sourceProperty().setValue(testBean);
        assertEquals("1", tested.getValue());

        // set buffered value, but don't commit value -> bean value must not change
        tested.setValue("2");
        assertEquals("1", testBean.name.get());

        // commit should not change anything
        tested.commit();
        assertEquals("2", testBean.name.get());

        // change bean value -> buffered value must not change
        testBean.name.set("3");
        assertEquals("2", tested.getValue());

        // refresh buffered value -> value must change
        tested.reload();
        assertEquals("3", tested.getValue());
    }

}