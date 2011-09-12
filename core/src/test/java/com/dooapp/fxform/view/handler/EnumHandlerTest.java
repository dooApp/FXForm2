package com.dooapp.fxform.view.handler;

import com.dooapp.fxform.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

/**
 * User: antoine
 * Date: 12/09/11
 * Time: 17:07
 */
public class EnumHandlerTest {
    @Test
    public void testHandle() throws Exception {
        List<Field> fields = TestUtils.getTestFields();
        FieldHandler handler = new EnumHandler();
        Assert.assertFalse(handler.handle(fields.get(0)));
        Assert.assertFalse(handler.handle(fields.get(1)));
        Assert.assertFalse(handler.handle(fields.get(2)));
        Assert.assertFalse(handler.handle(fields.get(3)));
        Assert.assertTrue(handler.handle(fields.get(4)));
    }
}
