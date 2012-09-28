package com.dooapp.fxform.handler;

import com.dooapp.fxform.TestUtils;
import com.dooapp.fxform.model.Element;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 12/09/11
 * Time: 17:11
 */
public class NamedFieldHandlerTest {
    @Test
    public void testHandle() throws Exception {
        List<Element> fields = TestUtils.getTestFields();
        ElementHandler handler = new NamedFieldHandler("booleanProperty");
        Assert.assertFalse(handler.handle(fields.get(0)));
        Assert.assertTrue(handler.handle(fields.get(1)));
        Assert.assertFalse(handler.handle(fields.get(2)));
        Assert.assertFalse(handler.handle(fields.get(3)));
        Assert.assertFalse(handler.handle(fields.get(4)));
    }
}
