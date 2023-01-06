package com.dooapp.fxform.issues;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.JavaFXRule;
import com.dooapp.fxform.validation.Warning;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import org.hamcrest.core.IsNot;
import org.junit.Rule;
import org.junit.Test;

import javax.validation.constraints.Max;

import static org.junit.Assert.assertThat;
import static org.junit.internal.matchers.IsCollectionContaining.hasItems;

public class Issue206Test {

    @Rule
    public JavaFXRule javaFXRule = new JavaFXRule();

    public class Bean {

        private IntegerProperty year = new SimpleIntegerProperty();

        @Max(value = 2025, groups = Warning.class)
        public int getYear() {
            return year.get();
        }

        public IntegerProperty yearProperty() {
            return year;
        }

        public void setYear(int year) {
            this.year.set(year);
        }
    }

    @Test
    public void testThatWarningStyleClassIsWellAddedAtFormInitialization() {
        FXForm<Bean> form = new FXForm<>();

        Bean bean = new Bean();
        bean.setYear(2030);
        form.setSource(bean);

        Label yearLabel = (Label) form.lookup("#year-form-label");
        assertThat(yearLabel.getStyleClass(), hasItems("form-label-warning"));

        bean.setYear(2010);
        assertThat(yearLabel.getStyleClass(), IsNot.not(hasItems("form-label-warning")));

        bean.setYear(2030);
        assertThat(yearLabel.getStyleClass(), hasItems("form-label-warning"));
    }
}
