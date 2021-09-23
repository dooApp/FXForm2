package com.dooapp.fxform.issues;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.JavaFXRule;
import com.dooapp.fxform.filter.ExcludeFilter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class Issue197Test {

    @Rule
    public JavaFXRule javaFXRule = new JavaFXRule();

    public class Bean {
        private StringProperty a = new SimpleStringProperty();
        private StringProperty b = new SimpleStringProperty();

        public String getA() { return a.get(); }
        public StringProperty aProperty() { return a; }
        public void setA(String a) { this.a.set(a); }

        public String getB() { return b.get(); }
        public StringProperty bProperty() { return b; }
        public void setB(String b) { this.b.set(b); }
    }

    @Test
    public void test() {
        Bean bean = new Bean();
        FXForm<Bean> form = new FXForm<>();
        form.setSource(bean);
        Assert.assertEquals(2, form.getFilteredElements().size());
        Assert.assertEquals("a", form.getFilteredElements().get(0).getName());
        Assert.assertEquals("b", form.getFilteredElements().get(1).getName());

        ExcludeFilter excludeFilter = new ExcludeFilter("a");
        form.getFilters().setAll(excludeFilter);
        Assert.assertEquals(1, form.getFilteredElements().size());
        Assert.assertEquals("b", form.getFilteredElements().get(0).getName());

        excludeFilter = new ExcludeFilter("a", "c");
        form.getFilters().setAll(excludeFilter);
        Assert.assertEquals(1, form.getFilteredElements().size());
        Assert.assertEquals("b", form.getFilteredElements().get(0).getName());

        form.getFilters().clear();
        Assert.assertEquals(2, form.getFilteredElements().size());
        Assert.assertEquals("a", form.getFilteredElements().get(0).getName());
        Assert.assertEquals("b", form.getFilteredElements().get(1).getName());
    }
}
