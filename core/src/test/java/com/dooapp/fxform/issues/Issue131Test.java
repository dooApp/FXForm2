package com.dooapp.fxform.issues;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.JavaFXRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * A test for issue <a href="https://github.com/dooApp/FXForm2/issues/131">#131</a>.
 *
 * @author Stefan Endrullis
 */
public class Issue131Test {

    @Rule
    public JavaFXRule javaFXRule = new JavaFXRule();

    public static class TestBean {
        Integer age;

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }

    @Test
    public void test() {
        new FXForm<>(new TestBean());
    }

}
