package com.dooapp.fxform.issues;

import com.dooapp.fxform.AbstractFXForm;
import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.JavaFXRule;
import com.dooapp.fxform.handler.NamedFieldHandler;
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.view.FXFormNode;
import com.dooapp.fxform.view.FXFormNodeWrapper;
import com.dooapp.fxform.view.factory.DefaultFactoryProvider;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class Issue161Test {

    @Rule
    public JavaFXRule javaFXRule = new JavaFXRule();

    public class TestBean {
        private StringProperty text = new SimpleStringProperty();

        public String getText() {
            return text.get();
        }

        public StringProperty textProperty() {
            return text;
        }

        public void setText(String text) {
            this.text.set(text);
        }
    }

    @Test
    public void test() {
        TextField textArea = new TextField();
        FXForm form = new FXForm();

        DefaultFactoryProvider factoryProvider = (DefaultFactoryProvider) form.getEditorFactoryProvider();
        factoryProvider.addFactory(new NamedFieldHandler("text"), new Callback<Void, FXFormNode>() {
            @Override
            public FXFormNode call(Void param) {
                return new FXFormNodeWrapper(textArea, textArea.textProperty()) {
                    @Override
                    public void init(Element element, AbstractFXForm fxForm) {
                        super.init(element, fxForm);
                        textArea.setPromptText("DOOAPP");
                    }
                };
            }
        });

        TestBean testBean = new TestBean();
        form.setSource(testBean);

        Assert.assertEquals("DOOAPP", textArea.getPromptText());
    }
}
