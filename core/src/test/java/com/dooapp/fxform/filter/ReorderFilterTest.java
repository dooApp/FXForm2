package com.dooapp.fxform.filter;

import org.junit.Assert;
import org.junit.Test;

/**
 * User: antoine
 * Date: 12/09/11
 * Time: 16:45
 */
public class ReorderFilterTest extends AbstractFilterTest {

    @Override
    FieldFilter createFilter() {
        return new ReorderFilter(new String[]{"doubleProperty", "booleanProperty"});
    }

    @Test
    public void testFilter() throws Exception {
        Assert.assertEquals("doubleProperty", filtered.get(0).getName());
        Assert.assertEquals("booleanProperty", filtered.get(1).getName());
        Assert.assertEquals("stringProperty", filtered.get(2).getName());
        Assert.assertEquals("integerProperty", filtered.get(3).getName());
        Assert.assertEquals("objectProperty", filtered.get(4).getName());
    }
}
