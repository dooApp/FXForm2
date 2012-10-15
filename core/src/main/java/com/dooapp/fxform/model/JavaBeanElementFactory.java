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

import com.dooapp.fxform.model.impl.java.JavaBeanBooleanPropertyElement;
import com.dooapp.fxform.model.impl.java.JavaBeanIntegerPropertyElement;
import com.dooapp.fxform.model.impl.java.JavaBeanObjectPropertyElement;
import com.dooapp.fxform.model.impl.java.JavaBeanStringPropertyElement;

import java.lang.reflect.Field;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 14/10/12
 * Time: 12:05
 */
public class JavaBeanElementFactory implements ElementFactory {

    @Override
    public Element create(Field field) throws FormException {
        if (String.class.isAssignableFrom(field.getType())) {
            return new JavaBeanStringPropertyElement(field);
        } else if (Boolean.class.isAssignableFrom(field.getType()) || field.getType() == Boolean.TYPE) {
            return new JavaBeanBooleanPropertyElement(field);
        } else if (Integer.class.isAssignableFrom(field.getType()) || field.getType() == Integer.TYPE) {
            return new JavaBeanIntegerPropertyElement(field);
        } else if (Float.class.isAssignableFrom(field.getType()) || field.getType() == Float.TYPE) {
            return new JavaBeanIntegerPropertyElement(field);
        } else if (Long.class.isAssignableFrom(field.getType()) || field.getType() == Long.TYPE) {
            return new JavaBeanIntegerPropertyElement(field);
        } else if (Double.class.isAssignableFrom(field.getType()) || field.getType() == Double.TYPE) {
            return new JavaBeanIntegerPropertyElement(field);
        }
        return new JavaBeanObjectPropertyElement(field);
    }

}
