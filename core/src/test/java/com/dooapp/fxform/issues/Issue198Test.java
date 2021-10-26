package com.dooapp.fxform.issues;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.JavaFXRule;
import com.dooapp.fxform.adapter.Adapter;
import com.dooapp.fxform.adapter.AdapterMatcher;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Map;

public class Issue198Test {

    @Rule
    public JavaFXRule javaFXRule = new JavaFXRule();

    @Test
    public void testToInstantiateMultipleDefaultAdapterProvider() throws NoSuchFieldException, IllegalAccessException {
        // Create DefaultAdapterProvider for the first time and get "DEFAULT_MAP" map size
        FXForm firstForm = new FXForm<>();
        firstForm.setSource(new Bean());
        Field defaultMapField = firstForm.getAdapterProvider().getClass().getDeclaredField("DEFAULT_MAP");
        defaultMapField.setAccessible(true);
        int size1 = ((Map<AdapterMatcher, Adapter>) defaultMapField.get(firstForm.getAdapterProvider())).size();

        // Create DefaultAdapterProvider for the second time and get "DEFAULT_MAP" map size
        FXForm form2 = new FXForm<>();
        form2.setSource(new Bean());
        Field defaultMapField2 = form2.getAdapterProvider().getClass().getDeclaredField("DEFAULT_MAP");
        defaultMapField2.setAccessible(true);
        int size2 = ((Map<AdapterMatcher, Adapter>) defaultMapField2.get(form2.getAdapterProvider())).size();

        // Because DEFAULT_MAP contains generic static adapter matchers it must not have more elements the second
        // time an instance is created
        Assert.assertEquals(size1, size2);
    }

    public class Bean {
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
}
