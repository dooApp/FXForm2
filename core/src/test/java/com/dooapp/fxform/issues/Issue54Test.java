/*
 * Copyright (c) 2013, dooApp <contact@dooapp.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of dooApp nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.dooapp.fxform.issues;

import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.model.FormException;
import com.dooapp.fxform.model.impl.PropertyFieldElement;
import com.dooapp.fxform.resource.DefaultResourceProvider;
import com.dooapp.fxform.resource.ResourceProvider;
import com.dooapp.fxform.view.NodeType;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ResourceBundle;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 03/12/2013
 * Time: 13:08
 */
public class Issue54Test {

    public static class MyBean {

        public StringProperty property1 = new SimpleStringProperty();

        public StringProperty property2 = new SimpleStringProperty();

        public StringProperty property3 = new SimpleStringProperty();

    }

    private ResourceProvider tested;

    private Element element1;

    private Element element2;

    private Element element3;

    @Before
    public void setup() throws NoSuchFieldException, FormException {
        MyBean myBean = new MyBean();
        tested = new DefaultResourceProvider();
        tested.setResourceBundle(ResourceBundle.getBundle("Issue56Test"));
        element1 = new PropertyFieldElement(MyBean.class.getDeclaredField("property1"));
        element2 = new PropertyFieldElement(MyBean.class.getDeclaredField("property2"));
        element3 = new PropertyFieldElement(MyBean.class.getDeclaredField("property3"));
        element1.sourceProperty().setValue(myBean);
        element2.sourceProperty().setValue(myBean);
        element3.sourceProperty().setValue(myBean);
    }

    @Test
    public void testGetString() throws NoSuchFieldException, FormException {
        // Test resource defined with bean name
        Assert.assertEquals("Property1 label", tested.getString(element1, NodeType.LABEL).get());
        Assert.assertEquals("Property1 tooltip", tested.getString(element1, NodeType.TOOLTIP).get());
        // Test resource define without bean name
        Assert.assertEquals("Property2 label", tested.getString(element2, NodeType.LABEL).get());
        Assert.assertEquals("Property2 tooltip", tested.getString(element2, NodeType.TOOLTIP).get());
        // Test default values
        Assert.assertEquals("property3", tested.getString(element3, NodeType.LABEL).get());
        Assert.assertEquals(null, tested.getString(element3, NodeType.TOOLTIP).get());
    }


    @Test
    public void testUpdateResourceBundle() {
        tested.setResourceBundle(ResourceBundle.getBundle("Issue56Test2"));
        Assert.assertEquals("Property1 label2", tested.getString(element1, NodeType.LABEL).get());
        Assert.assertEquals("Property1 tooltip2", tested.getString(element1, NodeType.TOOLTIP).get());
        Assert.assertEquals("property2", tested.getString(element2, NodeType.LABEL).get());
        Assert.assertEquals(null, tested.getString(element2, NodeType.TOOLTIP).get());
        Assert.assertEquals("Property3 label2", tested.getString(element3, NodeType.LABEL).get());
        Assert.assertEquals("Property3 tooltip2", tested.getString(element3, NodeType.TOOLTIP).get());
    }

}
