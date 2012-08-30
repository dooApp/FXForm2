package com.dooapp.fxform.view.handler;

import com.dooapp.fxform.TestUtils;
import com.dooapp.fxform.model.impl.FieldObservableElement;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 12/09/11
 * Time: 17:07
 */
public class EnumHandlerTest {
    @Test
    public void testHandle() throws Exception {
        List<Field> fields = TestUtils.getTestFields();
        ElementHandler handler = new EnumHandler();
        Assert.assertFalse(handler.handle(new FieldObservableElement(fields.get(0))));
        Assert.assertFalse(handler.handle(new FieldObservableElement(fields.get(1))));
        Assert.assertFalse(handler.handle(new FieldObservableElement(fields.get(2))));
        Assert.assertFalse(handler.handle(new FieldObservableElement(fields.get(3))));
        Assert.assertTrue(handler.handle(new FieldObservableElement(fields.get(4))));
    }
}
