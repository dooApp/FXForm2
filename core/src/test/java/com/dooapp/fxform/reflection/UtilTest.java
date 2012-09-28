package com.dooapp.fxform.reflection;

import com.dooapp.fxform.TestEnum;
import com.dooapp.fxform.TestUtils;
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.model.impl.ReadOnlyPropertyFieldElement;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 12/09/11
 * Time: 16:57
 */
public class UtilTest {
    @Test
    public void testGetObjectPropertyGeneric() throws Exception {
        List<Element> fields = TestUtils.getTestFields();
        Element objectPropertyField = fields.get(4);
        Class clazz = Util.getObjectPropertyGeneric(((ReadOnlyPropertyFieldElement) objectPropertyField).getField());
        Assert.assertEquals(TestEnum.class, clazz);
    }
}
