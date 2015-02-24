package com.dooapp.fxform.issues;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.JavaFXRule;
import junit.framework.Assert;
import org.hibernate.validator.constraints.Length;
import org.junit.Rule;
import org.junit.Test;

import javax.validation.constraints.NotNull;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 24/02/15
 * Time: 09:50
 */
public class Issue96Test {

    @Rule
    public JavaFXRule javaFXRule = new JavaFXRule();

    public class LoginData {
        @NotNull
        @Length(min = 1, max = 8)
        private String name;

        @NotNull
        @Length(min = 1, max = 8)
        private String password;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

    }

    @Test
    public void test() {
        FXForm fxForm = new FXForm(new LoginData());
        Assert.assertEquals(2, fxForm.getConstraintViolations().size());
    }

}
