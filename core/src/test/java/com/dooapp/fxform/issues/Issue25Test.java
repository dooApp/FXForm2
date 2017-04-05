package com.dooapp.fxform.issues;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.JavaFXRule;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 * Created at 26/06/13 17:33.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class Issue25Test {

    @Rule
    public JavaFXRule javaFXRule = new JavaFXRule();

    public static class Content {

    }

    public static class Bean {

        private final ObjectProperty<Content> content = new SimpleObjectProperty<Content>();

        public Content getContent() {
            return content.get();
        }

        public ObjectProperty<Content> contentProperty() {
            return content;
        }

        public void setContent(Content content) {
            this.content.set(content);
        }
    }

    @Test
    public void testIssue25() {
        FXForm fxForm = new FXForm();
        Bean bean = new Bean();
        bean.setContent(new Content());
        fxForm.setSource(bean);
        Assert.assertTrue(bean.getContent() instanceof Content);
    }

}