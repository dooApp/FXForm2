package com.dooapp.fxform.reflection;

import com.dooapp.fxform.TestEnum;
import com.dooapp.fxform.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 12/09/11
 * Time: 16:57
 */
public class UtilTest {
    @Test
    public void testGetObjectPropertyGeneric() throws Exception {
        List<Field> fields = TestUtils.getTestFields();
        Field objectPropertyField = fields.get(4);
        Class clazz = Util.getObjectPropertyGeneric(objectPropertyField);
        Assert.assertEquals(TestEnum.class, clazz);
    }
}
