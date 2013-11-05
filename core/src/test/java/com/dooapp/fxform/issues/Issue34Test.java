package com.dooapp.fxform.issues;

import com.dooapp.fxform.reflection.ReflectionUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import junit.framework.Assert;
import org.junit.Test;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 05/11/2013
 * Time: 11:42
 */
public class Issue34Test {

    private static class UpperBoundClass {

    }

    private static class Clazz extends UpperBoundClass {

    }

    private static class MyBean<T extends UpperBoundClass> {

        private final ObjectProperty<T> t = new SimpleObjectProperty<T>();

    }

    private static class SubBean extends MyBean<Clazz> {

    }

    @Test
    public void testUndeclared() throws NoSuchFieldException {
        MyBean myBean = new MyBean();
        Assert.assertEquals(UpperBoundClass.class, ReflectionUtils.getObjectPropertyGeneric(myBean, myBean.getClass().getDeclaredField("t")));
    }

    @Test
    public void testSubBean() throws NoSuchFieldException {
        SubBean myBean = new SubBean();
        Assert.assertEquals(Clazz.class, ReflectionUtils.getObjectPropertyGeneric(myBean, myBean.getClass().getSuperclass().getDeclaredField("t")));
    }

}
