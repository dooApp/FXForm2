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

import com.dooapp.fxform.annotation.Accessor;
import com.dooapp.fxform.annotation.NonVisual;
import com.dooapp.fxform.model.FormException;
import com.dooapp.fxform.model.impl.PropertyMethodElement;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import junit.framework.Assert;
import org.junit.Test;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 03/12/2013
 * Time: 10:49
 */
public class Issue56Test {

    @Accessor(Accessor.AccessType.METHOD)
    private final static class TestBean {

        private StringProperty test;

        private String getTest() {
            return test.get();
        }

        @NonVisual
        public StringProperty testProperty() {
            if (test == null) {
                test = new SimpleStringProperty();
            }
            return test;
        }
    }

    @Accessor(Accessor.AccessType.METHOD)
    private final static class TestBean2 {

        @NonVisual
        private StringProperty test;

        private String getTest() {
            return test.get();
        }

        public StringProperty testProperty() {
            if (test == null) {
                test = new SimpleStringProperty();
            }
            return test;
        }
    }

    @Test
    public void testGetAnnotationFormMethod() throws NoSuchFieldException, NoSuchMethodException, FormException {
        PropertyMethodElement propertyMethodElement = new PropertyMethodElement(TestBean.class.getDeclaredField("test"));
        Assert.assertNotNull(propertyMethodElement.getAnnotation(NonVisual.class));
    }

    @Test
    public void testFallbackOnField() throws NoSuchFieldException, NoSuchMethodException, FormException {
        PropertyMethodElement propertyMethodElement = new PropertyMethodElement(TestBean2.class.getDeclaredField("test"));
        Assert.assertNotNull(propertyMethodElement.getAnnotation(NonVisual.class));
    }

}
