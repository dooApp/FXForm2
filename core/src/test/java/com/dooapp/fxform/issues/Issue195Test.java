package com.dooapp.fxform.issues;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.JavaFXRule;
import com.dooapp.fxform.controller.PropertyEditorController;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.TextInputControl;
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

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

public class Issue195Test {

    @Rule
    public JavaFXRule javaFXRule = new JavaFXRule();

    @IsMoreThan5
    public class Bean {

        private DoubleProperty value = new SimpleDoubleProperty();

        public double getValue() {
            return value.get();
        }

        public DoubleProperty valueProperty() {
            return value;
        }

        public void setValue(double value) {
            this.value.set(value);
        }

    }

    @Target({TYPE})
    @Retention(RUNTIME)
    @Constraint(validatedBy = IsMoreThan5Validator.class)
    @Documented
    public @interface IsMoreThan5 {
        String message() default "";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    public static class IsMoreThan5Validator implements ConstraintValidator<IsMoreThan5, Bean> {

        @Override
        public void initialize(IsMoreThan5 constraintAnnotation) {
        }

        @Override
        public boolean isValid(Bean value, ConstraintValidatorContext context) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Must be more than 5").addPropertyNode("value").addConstraintViolation();
            return value.getValue() > 5;
        }
    }

    @Test
    public void test() {
        Bean bean = new Bean();
        FXForm form = new FXForm();
        form.setSource(bean);
        ((TextInputControl) form.lookup("#value-form-editor")).setText("4");
        Assert.assertEquals(1, ((PropertyEditorController) form
                .getController(form.getFilteredElements().get(0)).getEditorController())
                .getPropertyElementValidator()
                .constraintViolationsProperty().get().size());
        ((TextInputControl) form.lookup("#value-form-editor")).setText("4.");
        Assert.assertEquals(1, ((PropertyEditorController) form
                .getController(form.getFilteredElements().get(0)).getEditorController())
                .getPropertyElementValidator()
                .constraintViolationsProperty().get().size());
        ((TextInputControl) form.lookup("#value-form-editor")).setText("4");
        Assert.assertEquals(1, ((PropertyEditorController) form
                .getController(form.getFilteredElements().get(0)).getEditorController())
                .getPropertyElementValidator()
                .constraintViolationsProperty().get().size());
    }

}
