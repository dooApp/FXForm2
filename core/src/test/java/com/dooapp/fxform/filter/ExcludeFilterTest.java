package com.dooapp.fxform.filter;

import org.junit.Assert;
import org.junit.Test;

/**
 * User: antoine
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
        Assert.assertFalse(containsNamedField("integerProperty", filtered));
        Assert.assertFalse(containsNamedField("booleanProperty", filtered));
        Assert.assertTrue(containsNamedField("stringProperty", filtered));
        Assert.assertTrue(containsNamedField("doubleProperty", filtered));
    }

}
