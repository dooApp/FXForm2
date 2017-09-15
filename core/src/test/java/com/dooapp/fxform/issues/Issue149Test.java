package com.dooapp.fxform.issues;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.JavaFXRule;
import com.dooapp.fxform.TestUtils;
import com.dooapp.fxform.validation.Warning;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import junit.framework.Assert;
import org.junit.Rule;
import org.junit.Test;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.ExecutionException;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 15/09/2017
 * Time: 12:12
 */

public class Issue149Test {

    @Rule
    public JavaFXRule javaFXRule = new JavaFXRule();

    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = Test149Validator.class)
    public @interface Test149 {

        String message() default "warning !";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};

    }

    public static class Test149Validator implements ConstraintValidator<Test149, TestBean> {

        @Override
        public void initialize(Test149 constraintAnnotation) {

        }

        @Override
        public boolean isValid(TestBean value, ConstraintValidatorContext context) {
            return false;
        }
    }

    @Test149(groups = Warning.class)
    class TestBean {

        StringProperty name = new SimpleStringProperty();

        public String getName() {
            return name.get();
        }

        public StringProperty nameProperty() {
            return name;
        }

        public void setName(String name) {
            this.name.set(name);
        }

    }

    @Test
    public void test149() throws ExecutionException, InterruptedException {
        TestBean testBean = new TestBean();
        FXForm fxForm = new FXForm(testBean);
        TestUtils.syncJavaFXThread();
        Assert.assertEquals(1, fxForm.getConstraintViolations().size());
    }

}
