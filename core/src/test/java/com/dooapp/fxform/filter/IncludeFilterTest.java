package com.dooapp.fxform.filter;

import com.dooapp.fxform.TestUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 12/09/11
 * Time: 16:44
 */
public class IncludeFilterTest extends AbstractFilterTest {
    @Override
    FieldFilter createFilter() {
        return new IncludeFilter(new String[]{"integerProperty", "booleanProperty"});
    }

    @Test
    public void testFilter() throws Exception {
        Assert.assertTrue(TestUtils.containsNamedField("integerProperty", filtered));
        Assert.assertTrue(TestUtils.containsNamedField("booleanProperty", filtered));
        Assert.assertFalse(TestUtils.containsNamedField("stringProperty", filtered));
        Assert.assertFalse(TestUtils.containsNamedField("doubleProperty", filtered));
        Assert.assertFalse(TestUtils.containsNamedField("objectProperty", filtered));
    }

}
