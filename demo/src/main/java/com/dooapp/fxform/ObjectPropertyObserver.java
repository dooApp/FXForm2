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

import com.dooapp.fxform.reflection.impl.ReflectionFieldProvider;
import javafx.beans.property.ListProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;

import java.lang.reflect.Field;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 17/04/11
 * Time: 00:21
 */
public class ObjectPropertyObserver implements ChangeListener {

    public ObjectPropertyObserver(Object source) {
        for (Field field : new ReflectionFieldProvider().getProperties(source)) {
            if (MapProperty.class.isAssignableFrom(field.getType())) {
                ((MapProperty) get(field, source)).addListener(new MapChangeListener() {
                    @Override
                    public void onChanged(Change change) {
                        System.out.println(change.toString());
                    }
                });
            } else if (ListProperty.class.isAssignableFrom(field.getType())) {
                ((ListProperty) get(field, source)).addListener(new ListChangeListener() {
                    @Override
                    public void onChanged(Change c) {
                        System.out.println(c.toString());
                    }
                });
            } else if (ObservableValue.class.isAssignableFrom(field.getType())) {
                ((ObservableValue) get(field, source)).addListener(this);
            }
        }
    }

    private Object get(Field field, Object source) {
        field.setAccessible(true);
        try {
            return field.get(source);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void changed(ObservableValue observableValue, Object o, Object o1) {
        System.out.println(this + ": " + observableValue + ": " + o + "->" + o1);
    }
}
