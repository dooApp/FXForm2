package com.dooapp.fxform.utils;


import javafx.collections.FXCollections;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class ObjectUtilsTest {

    private Object o1;
    private Object o2;
    private boolean areSame;

    public ObjectUtilsTest(Object o1, Object o2, boolean areSame) {
        this.o1 = o1;
        this.o2 = o2;
        this.areSame = areSame;
    }

    @Test
    public void testAreSameMethod() {
        Assert.assertEquals(areSame, ObjectUtils.areSame(o1, o2));
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[] test1 = {10d, 15d, false};
        Object[] test2 = {10d, 10d, true};
        Object[] test3 = {new Double(10), new Double(10), true};
        Object[] test4 = {new Object(), new Object(), false};
        Object[] test5 = {FXCollections.observableArrayList(), FXCollections.observableArrayList(), false};
        Object[] test6 = {FXCollections.observableArrayList(), FXCollections.observableArrayList(22, 50), false};
        Object[] test7 = {null, new Object(), false};
        Object[] test8 = {new Object(), null, false};
        Object[] test9 = {null, null, true};
        Object[][] data = new Object[][]{test1, test2, test3, test4, test5, test6, test7, test8, test9};
        return Arrays.asList(data);
    }
}
