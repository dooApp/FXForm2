package com.dooapp.fxform.issues;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.JavaFXRule;
import com.dooapp.fxform.TestUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.time.DayOfWeek;
import java.util.concurrent.ExecutionException;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

public class Issue186Test {

    @Rule
    public JavaFXRule javaFXRule = new JavaFXRule();

    @IsFriday
    public class Bean {
        private ObjectProperty<DayOfWeek> dayOfWeek = new SimpleObjectProperty<>();

        public DayOfWeek getDayOfWeek() {
            return dayOfWeek.get();
        }

        public ObjectProperty<DayOfWeek> dayOfWeekProperty() {
            return dayOfWeek;
        }

        public void setDayOfWeek(DayOfWeek dayOfWeek) {
            this.dayOfWeek.set(dayOfWeek);
        }
    }

    @Target({TYPE})
    @Retention(RUNTIME)
    @Constraint(validatedBy = IsFridayValidator.class)
    @Documented
    public @interface IsFriday {
        String message() default "";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    public static class IsFridayValidator implements ConstraintValidator<IsFriday, Bean> {

        @Override
        public void initialize(IsFriday constraintAnnotation) {
        }

        @Override
        public boolean isValid(Bean value, ConstraintValidatorContext context) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("The dayOfWeek must be FRIDAY").addPropertyNode("dayOfWeek").addConstraintViolation();
            return value.getDayOfWeek() == DayOfWeek.FRIDAY;
        }
    }

    @Test
    public void testWithValidBean() throws ExecutionException, InterruptedException {
        Bean bean = new Bean();
        bean.setDayOfWeek(DayOfWeek.FRIDAY);
        FXForm<Bean> form = new FXForm<>();
        form.setSource(bean);

        TestUtils.syncJavaFXThread();
        Assert.assertEquals(0, form.getConstraintViolations().size());
        Assert.assertEquals(0, form.getClassLevelValidator().constraintViolationsProperty().size());
    }

    @Test
    public void testWithNoValidBeanWherePropertyIsSetAfterTheFormIsCreated() throws ExecutionException, InterruptedException {
        Bean bean = new Bean();
        FXForm<Bean> form = new FXForm<>();
        form.setSource(bean);
        bean.setDayOfWeek(DayOfWeek.MONDAY);

        TestUtils.syncJavaFXThread();
        Assert.assertEquals(1, form.getConstraintViolations().size());
        Assert.assertEquals(0, form.getClassLevelValidator().constraintViolationsProperty().size());
    }

    @Test
    public void testWithNoValidBeanWherePropertyIsSetBeforeTheFormIsCreated() throws ExecutionException, InterruptedException {
        Bean bean = new Bean();
        bean.setDayOfWeek(DayOfWeek.MONDAY);
        FXForm<Bean> form = new FXForm<>();
        form.setSource(bean);

        TestUtils.syncJavaFXThread();
        Assert.assertEquals(1, form.getConstraintViolations().size());
        Assert.assertEquals(0, form.getClassLevelValidator().constraintViolationsProperty().size());
    }

    @Test
    public void verifyThatClassLevelValidatorInitialisationIsThreadSafety() throws ExecutionException, InterruptedException {
        for (int i = 0; i < 1000; i++) {
            Bean bean = new Bean();
            bean.setDayOfWeek(DayOfWeek.MONDAY);
            FXForm<Bean> form = new FXForm<>();
            form.setSource(bean);

            TestUtils.syncJavaFXThread();
            Assert.assertEquals(1, form.getConstraintViolations().size());
            Assert.assertEquals(0, form.getClassLevelValidator().constraintViolationsProperty().size());
        }
    }
}
