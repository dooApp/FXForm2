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

package com.dooapp.fxform;

import com.dooapp.fxform.annotation.FormFactory;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Created at 26/09/12 14:23.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class MyBean {

    public static enum Subject {
        CONTACT, QUESTION, BUG, FEEDBACK
    }


    private final StringProperty name = new SimpleStringProperty();

    private final StringProperty email = new SimpleStringProperty();

    @FormFactory(Demo.TextAreaFactory.class)
    private final StringProperty message = new SimpleStringProperty();

    private final StringProperty website = new SimpleStringProperty();

    private final BooleanProperty subscribe = new SimpleBooleanProperty();

    private final ObjectProperty<Subject> subject = new SimpleObjectProperty<Subject>();

    private final IntegerProperty age = new SimpleIntegerProperty();

    private final ObjectProperty<BigDecimal> bigDecimalProperty = new SimpleObjectProperty<BigDecimal>();

    private String javaString = "Java String";

    private int javaInteger = 2;

    private MapProperty<String, String> userMap = new SimpleMapProperty<String, String>(FXCollections.observableMap(new HashMap<String, String>()));

    protected MyBean(String name, String email, String message, String website, boolean subscribe, Subject subject) {
        this.name.set(name);
        this.email.set(email);
        this.message.set(message);
        this.website.set(website);
        this.subscribe.set(subscribe);
        this.subject.set(subject);
    }

    @Email
    public String getEmail() {
        return email.get();
    }

    @NotNull
    public BigDecimal getBigDecimalProperty() {
        return bigDecimalProperty.get();
    }

    public String getJavaString() {
        return javaString;
    }

    public void setJavaString(String javaString) {
        System.out.println("javaString: " + this.javaString + "->" + javaString);
        this.javaString = javaString;
    }

    public int getJavaInteger() {
        return javaInteger;
    }

    public void setJavaInteger(int javaInteger) {
        System.out.println("javaInteger: " + this.javaInteger + "->" + javaInteger);
        this.javaInteger = javaInteger;
    }
}