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

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.builder.FXFormBuilder;
import com.dooapp.fxform.filter.FilterException;
import com.dooapp.fxform.filter.ReorderFilter;
import com.dooapp.fxform.reflection.MultipleBeanSource;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.junit.Before;
import org.junit.Test;

/**
 * User: Kevin Senechal <kevin.senechal@dooapp.com>
 * Date: 09/12/2013
 * Time: 15:14
 */
public class Issue82Test {

    public static class Address {

        private StringProperty address = new SimpleStringProperty();

        public String getAddress() {
            return address.get();
        }
    }

    public static class MyAddress extends Address {

        @NotEmpty
        public String getAddress() {
            return super.getAddress();
        }
    }

    public static class Bean1 {

        private StringProperty name = new SimpleStringProperty();

        private ObjectProperty<Address> address = new SimpleObjectProperty(new Address());
    }

    public static class Bean2 extends Bean1 {

        private ObjectProperty<MyAddress> address = new SimpleObjectProperty(new MyAddress());

        public MyAddress getAddress() {
            return address.get();
        }
    }

    @Before
    public void setup() {
    }

    @Test
    public void testThatReorderWorksWellWithSpecificFieldFilter() throws FilterException {
        Bean2 bean2 = new Bean2();
        FXForm fxForm = new FXFormBuilder().include("name", MyAddress.class.getName() + "-address").build
                ();
        fxForm.setSource(new MultipleBeanSource(bean2, bean2.getAddress()));
        ReorderFilter reorderFilter = new ReorderFilter("name", MyAddress.class.getName() + "-address");
        reorderFilter.filter(fxForm.getElements());
    }
}
