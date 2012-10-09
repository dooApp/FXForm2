package com.dooapp.fxform.issues;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.TestBean;
import com.dooapp.fxform.filter.ExcludeFilter;
import com.dooapp.fxform.view.FXFormSkinFactory;
import org.junit.Test;

/**
 * Unit test for issue #8<br>
 * <br>
 * Created at 21/11/11 18:24.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class Issue8Test {

    @Test
    public void testIssue8() {
        FXForm fxForm = new FXForm();
        fxForm.setSkin(FXFormSkinFactory.INLINE_FACTORY.createSkin(fxForm));
        fxForm.setSource(new TestBean());
        fxForm.getFilters().add(new ExcludeFilter("stringProperty", "objectProperty"));
        fxForm.setSource(null);
        org.junit.Assert.assertEquals(0, fxForm.getControllers().size());
    }

}