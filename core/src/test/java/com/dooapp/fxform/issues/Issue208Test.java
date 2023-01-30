package com.dooapp.fxform.issues;

import com.dooapp.fxform.AbstractFXForm;
import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.JavaFXRule;
import com.dooapp.fxform.handler.NamedFieldHandler;
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.validation.Warning;
import com.dooapp.fxform.view.FXFormNode;
import com.dooapp.fxform.view.FXFormNodeWrapper;
import com.dooapp.fxform.view.factory.DefaultFactoryProvider;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import javax.validation.constraints.Max;

public class Issue208Test {

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
        FXForm form = new FXForm<>();

        TextField textField = new TextField();
        DefaultFactoryProvider factoryProvider = (DefaultFactoryProvider) form.getEditorFactoryProvider();
        factoryProvider.addFactory(new NamedFieldHandler("year"), new Callback<Void, FXFormNode>() {
            @Override
            public FXFormNode call(Void param) {
                return new FXFormNodeWrapper(textField, textField.textProperty()) {
                    @Override
                    public void init(Element element, AbstractFXForm fxForm) {
                        super.init(element, fxForm);
                        textField.setPromptText("YEAR");
                    }
                };
            }
        });

        Bean bean = new Bean();
        form.setSource(bean);

        textField.setText("2030");
        Assert.assertEquals(textField.getText(), String.valueOf(bean.getYear()));
    }
}
