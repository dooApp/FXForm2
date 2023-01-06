package com.dooapp.fxform.issues;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.JavaFXRule;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.hamcrest.core.IsNot;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.junit.internal.matchers.IsCollectionContaining.hasItems;

public class Issue207Test {

    @Rule
    public JavaFXRule javaFXRule = new JavaFXRule();

    public class Bean {

        private IntegerProperty year = new SimpleIntegerProperty();

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
    public void testThatElementBecomeInvalidWhenInputCannotBeAdapted() {
        Bean bean = new Bean();
        FXForm<Bean> form = new FXForm<>();
        form.setSource(bean);

        Label yearLabel = (Label) form.lookup("#year-form-label");
        assertThat(yearLabel.getStyleClass(), IsNot.not(hasItems("form-label-invalid")));

        TextField yearEditor = (TextField) form.lookup("#year-form-editor");
        yearEditor.setText("azerty");

        assertThat(yearLabel.getStyleClass(), hasItems("form-label-invalid"));
    }
}
