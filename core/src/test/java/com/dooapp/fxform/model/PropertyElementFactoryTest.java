/*
 * Copyright (c) 2012, dooApp <contact@dooapp.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of dooApp nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.dooapp.fxform.model;

import com.dooapp.fxform.annotation.Accessor;
import com.dooapp.fxform.model.impl.PropertyFieldElement;
import com.dooapp.fxform.model.impl.PropertyMethodElement;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import junit.framework.Assert;
import org.junit.Test;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 16/10/13
 * Time: 15:48
 */
public class PropertyElementFactoryTest {

    @Accessor(Accessor.AccessType.METHOD)
    public static class MethodTestBean {

        private StringProperty name;

        public StringProperty nameProperty() {
            if (name == null) {
                name = new SimpleStringProperty();
            }
            return name;
        }

    }

    @Accessor(Accessor.AccessType.FIELD)
    public static class FieldTestBean {

        private StringProperty name = new SimpleStringProperty();

    }

    @Test
    public void testElementFactory() throws NoSuchFieldException, FormException {
        PropertyElementFactory tested = new PropertyElementFactory();
        Element methodElement = tested.create(MethodTestBean.class.getDeclaredField("name"));
        Element fieldElement = tested.create(FieldTestBean.class.getDeclaredField("name"));
        Assert.assertEquals(PropertyMethodElement.class, methodElement.getClass());
        Assert.assertEquals(PropertyFieldElement.class, fieldElement.getClass());
    }


}
