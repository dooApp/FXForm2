package com.dooapp.fxform.reflection.impl;

import com.dooapp.fxform.TestBean;
import com.dooapp.fxform.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

/**
 * User: antoine
 * Date: 12/09/11
 * Time: 16:51
 */
public class ReflectionFieldProviderTest {

    @Test
    public void testGetProperties() throws Exception {
        ReflectionFieldProvider reflectionFieldProvider = new ReflectionFieldProvider();
        TestBean testBean = new TestBean();
        List<Field> fields = reflectionFieldProvider.getProperties(testBean);
        Assert.assertEquals(5, fields.size());
        Assert.assertTrue(TestUtils.containsNamedField("stringProperty", fields));
        Assert.assertTrue(TestUtils.containsNamedField("booleanProperty", fields));
        Assert.assertTrue(TestUtils.containsNamedField("integerProperty", fields));
        Assert.assertTrue(TestUtils.containsNamedField("doubleProperty", fields));
        Assert.assertTrue(TestUtils.containsNamedField("objectProperty", fields));
    }
}
