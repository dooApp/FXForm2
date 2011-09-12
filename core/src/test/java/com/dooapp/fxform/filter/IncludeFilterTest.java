package com.dooapp.fxform.filter;

import org.junit.Assert;
import org.junit.Test;

/**
 * User: antoine
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
        Assert.assertTrue(containsNamedField("integerProperty", filtered));
        Assert.assertTrue(containsNamedField("booleanProperty", filtered));
        Assert.assertFalse(containsNamedField("stringProperty", filtered));
        Assert.assertFalse(containsNamedField("doubleProperty", filtered));
    }

}
