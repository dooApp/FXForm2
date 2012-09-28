package com.dooapp.fxform.reflection.impl;

import com.dooapp.fxform.TestBean;
import com.dooapp.fxform.TestUtils;
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.model.impl.ReadOnlyPropertyFieldElement;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 12/09/11
 * Time: 16:51
 */
public class ReflectionFieldProviderTest {

    @Test
    public void testGetProperties() throws Exception {
        ReflectionFieldProvider reflectionFieldProvider = new ReflectionFieldProvider();
        TestBean testBean = new TestBean();
        List<Field> fields = reflectionFieldProvider.getProperties(testBean);
        List<Element> elements = new LinkedList<Element>();
        for (Field field: fields) {
            elements.add(new ReadOnlyPropertyFieldElement(field));
        }
        Assert.assertEquals(5, fields.size());
        Assert.assertTrue(TestUtils.containsNamedField("stringProperty", elements));
        Assert.assertTrue(TestUtils.containsNamedField("booleanProperty", elements));
        Assert.assertTrue(TestUtils.containsNamedField("integerProperty", elements));
        Assert.assertTrue(TestUtils.containsNamedField("doubleProperty", elements));
        Assert.assertTrue(TestUtils.containsNamedField("objectProperty", elements));
    }
}
