package com.dooapp.fxform.filter;

import org.junit.Assert;
import org.junit.Test;

/**
 * User: antoine
 * Date: 12/09/11
 * Time: 16:48
 */
public class NonVisualFilterTest extends AbstractFilterTest {
    @Override
    FieldFilter createFilter() {
        return new NonVisualFilter();
    }

    @Test
    public void testFilter() throws Exception {
        Assert.assertFalse(containsNamedField("integerProperty", filtered));
        Assert.assertTrue(containsNamedField("booleanProperty", filtered));
        Assert.assertTrue(containsNamedField("stringProperty", filtered));
        Assert.assertTrue(containsNamedField("doubleProperty", filtered));
    }
}
