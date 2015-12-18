package com.dooapp.fxform.issues;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.JavaFXRule;
import com.dooapp.fxform.TestBean;
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.view.skin.FXMLSkin;
import junit.framework.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 18/12/15
 * Time: 16:18
 */
public class Issue116Test {

    @Rule
    public JavaFXRule javaFXRule = new JavaFXRule();

    @Test
    public void testIssue116() {
        FXForm fxForm = new FXForm();
        fxForm.setSource(new TestBean());
        FXMLSkin fxmlSkin = new FXMLSkin(fxForm, this.getClass().getClassLoader().getResource("issue116.fxml"));
        fxForm.setSkin(fxmlSkin);
        Element element = fxForm.getFilteredElements().
                stream()
                .filter(e -> e.getName().equals("stringProperty"))
                .findFirst().get();
        Assert.assertNotNull(fxmlSkin.getEditor(element));
        Assert.assertNotNull(fxmlSkin.getLabel(element));
        Assert.assertNotNull(fxmlSkin.getTooltip(element));
        Assert.assertNotNull(fxmlSkin.getConstraint(element));

    }

}
