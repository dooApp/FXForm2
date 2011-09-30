package com.dooapp.fxform.filter;

import com.dooapp.fxform.TestUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 12/09/11
 * Time: 15:35
 */
public class ExcludeFilterTest extends AbstractFilterTest {

    @Override
    FieldFilter createFilter() {
        return new ExcludeFilter(new String[]{"integerProperty", "booleanProperty"});
    }

    @Test
    public void testFilter() throws Exception {
        Assert.assertFalse(TestUtils.containsNamedField("integerProperty", filtered));
        Assert.assertFalse(TestUtils.containsNamedField("booleanProperty", filtered));
        Assert.assertTrue(TestUtils.containsNamedField("stringProperty", filtered));
        Assert.assertTrue(TestUtils.containsNamedField("doubleProperty", filtered));
        Assert.assertTrue(TestUtils.containsNamedField("objectProperty", filtered));
    }

}
