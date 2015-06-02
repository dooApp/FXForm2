package com.dooapp.fxform.issues;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.JavaFXRule;
import com.dooapp.fxform.adapter.Adapter;
import com.dooapp.fxform.adapter.AdapterException;
import com.dooapp.fxform.adapter.FormAdapter;
import com.dooapp.fxform.annotation.FormFactory;
import com.dooapp.fxform.model.FormException;
import com.dooapp.fxform.view.factory.impl.CheckboxFactory;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import junit.framework.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 02/06/15
 * Time: 17:11
 */
public class Issue103Test {

    @Rule
    public JavaFXRule javaFXRule = new JavaFXRule();

    public static class TestAdapter implements Adapter<Double, Boolean> {


        @Override
        public Boolean adaptTo(Double from) throws AdapterException {
            return null;
        }

        @Override
        public Double adaptFrom(Boolean to) throws AdapterException {
            return null;
        }

    }

    public static class TestBean {

        @FormFactory(CheckboxFactory.class)
        @FormAdapter(TestAdapter.class)
        public ReadOnlyObjectProperty<Double> p = new SimpleObjectProperty<Double>(1.0);

    }

    @Test
    public void test() throws NoSuchFieldException, FormException {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> Assert.fail());
        new FXForm(new TestBean());

    }

}
