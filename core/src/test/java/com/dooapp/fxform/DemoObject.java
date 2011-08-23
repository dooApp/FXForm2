/*
 * Copyright (c) 2011, dooApp <contact@dooapp.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of dooApp nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.dooapp.fxform;

import com.dooapp.fxform.annotation.NonVisual;
import com.dooapp.fxform.model.EnumProperty;
import javafx.beans.property.*;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Min;


/**
 * User: Antoine Mischler
 * Date: 09/04/11
 * Time: 22:53
 */
public class DemoObject {

    private StringProperty name = new SimpleStringProperty();

    private StringProperty mail = new SimpleStringProperty();

    @NonVisual
    private StringProperty shouldNoBeInTheForm = new SimpleStringProperty();

    private BooleanProperty lucky = new SimpleBooleanProperty();

    private EnumProperty<TestEnum> letter = new EnumProperty(TestEnum.class);

    private IntegerProperty age = new SimpleIntegerProperty();

    private DoubleProperty height = new SimpleDoubleProperty();

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    @Email
    public String getMail() {
        return mail.get();
    }

    @Min(value = 5)
    public int getAge() {
        return age.get();
    }

    public TestEnum getLetter() {
        return letter.get();
    }

    public void setLetter(TestEnum testEnum) {
        letter.set(testEnum);
    }


}
