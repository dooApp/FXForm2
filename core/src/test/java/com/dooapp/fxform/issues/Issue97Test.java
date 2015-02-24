package com.dooapp.fxform.issues;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.JavaFXRule;
import com.dooapp.fxform.builder.FXFormBuilder;
import com.dooapp.fxform.filter.ExcludeFilter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.junit.Rule;
import org.junit.Test;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 24/02/15
 * Time: 09:22
 */
public class Issue97Test {

    @Rule
    public JavaFXRule javaFXRule = new JavaFXRule();

    public static class MyBean {

        StringProperty a = new SimpleStringProperty();

        StringProperty b = new SimpleStringProperty();

        public String getA() {
            return a.get();
        }

        public StringProperty aProperty() {
            return a;
        }

        public void setA(String a) {
            this.a.set(a);
        }

        public String getB() {
            return b.get();
        }

        public StringProperty bProperty() {
            return b;
        }

        public void setB(String b) {
            this.b.set(b);
        }
    }

    @Test
    public void test() {
        MyBean testedBean = new MyBean();
        FXForm fxForm = new FXFormBuilder().build();
        fxForm.getFilters().add(new ExcludeFilter("b"));
        fxForm.setSource(testedBean);
        fxForm.getClassLevelValidator().validate();
    }

}
