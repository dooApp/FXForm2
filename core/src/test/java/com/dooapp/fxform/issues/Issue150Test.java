package com.dooapp.fxform.issues;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.FailOnUncaughtExceptionRule;
import com.dooapp.fxform.JavaFXRule;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 22/09/2017
 * Time: 10:33
 */
public class Issue150Test {

    @Rule
    public JavaFXRule javaFXRule = new JavaFXRule();

    @Rule
    public FailOnUncaughtExceptionRule failOnUncaughtExceptionRule = new FailOnUncaughtExceptionRule();

    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = Test150Validator.class)
    public @interface Test150 {

        String message() default "warning !";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};

    }

    public static class Test150Validator implements ConstraintValidator<Test150, TestBean> {

        @Override
        public void initialize(Test150 constraintAnnotation) {

        }

        @Override
        public boolean isValid(TestBean value, ConstraintValidatorContext context) {
            return false;
        }
    }

    @Test150
    class TestBean {

        StringProperty name = new SimpleStringProperty();

        IntegerProperty age = new SimpleIntegerProperty();

        public String getName() {
            return name.get();
        }

        public StringProperty nameProperty() {
            return name;
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public int getAge() {
            return age.get();
        }

        public IntegerProperty ageProperty() {
            return age;
        }

        public void setAge(int age) {
            this.age.set(age);
        }

    }

    @Test
    public void test150() throws ExecutionException, InterruptedException {
        AtomicBoolean running = new AtomicBoolean(true);
        TestBean testBean = new TestBean();
        FXForm fxForm = new FXForm(testBean);
        Thread thread1 = new Thread(() -> {
            while (running.get()) {
                try {
                    testBean.setName("" + Math.random());
                    Thread.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "Thread 1");
        Thread thread2 = new Thread(() -> {
            while (running.get()) {
                try {
                    testBean.setAge((int) (Math.random() * 100));
                    Thread.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "Thread 2");
        thread1.start();
        thread2.start();
        Thread.sleep(1000);
        running.set(false);
    }


}
