package com.dooapp.fxform.issues;

import com.dooapp.fxform.annotation.Accessor;
import com.dooapp.fxform.model.FormException;
import com.dooapp.fxform.model.PropertyElementFactory;
import com.dooapp.fxform.model.impl.PropertyMethodElement;
import com.dooapp.fxform.model.impl.ReadOnlyPropertyMethodElement;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import junit.framework.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 16/01/15
 * Time: 10:03
 */
public class Issue95Test {

    @Accessor(Accessor.AccessType.METHOD)
    private static class TestBean {

        public DoubleProperty doubleProperty = new SimpleDoubleProperty();

        public DoubleProperty readOnlyDoubleProperty = new SimpleDoubleProperty();

        public double getDoubleProperty() {
            return doubleProperty.get();
        }

        public DoubleProperty doublePropertyProperty() {
            return doubleProperty;
        }

        public double getReadOnlyDoubleProperty() {
            return readOnlyDoubleProperty.get();
        }

        public ReadOnlyDoubleProperty readOnlyDoublePropertyProperty() {
            return readOnlyDoubleProperty;
        }

    }

    @Test
    public void testThatElementIsReadonly() throws NoSuchFieldException, FormException {
        PropertyElementFactory propertyElementFactory = new PropertyElementFactory();
        Field doublePropertyField = TestBean.class.getField("doubleProperty");
        Field readOnlyDoublePropertyField = TestBean.class.getField("readOnlyDoubleProperty");
        Assert.assertEquals(PropertyMethodElement.class, propertyElementFactory.create(doublePropertyField).getClass());
        Assert.assertEquals(ReadOnlyPropertyMethodElement.class, propertyElementFactory.create(readOnlyDoublePropertyField).getClass());
    }

}
