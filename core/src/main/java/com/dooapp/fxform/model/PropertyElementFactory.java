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

package com.dooapp.fxform.model;

import com.dooapp.fxform.annotation.Accessor;
import com.dooapp.fxform.model.impl.PropertyFieldElement;
import com.dooapp.fxform.model.impl.PropertyMethodElement;
import com.dooapp.fxform.model.impl.ReadOnlyPropertyFieldElement;
import com.dooapp.fxform.model.impl.ReadOnlyPropertyMethodElement;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyProperty;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 14/10/12
 * Time: 12:05
 */
public class PropertyElementFactory implements ElementFactory {

    private final Logger logger = Logger.getLogger(PropertyElementFactory.class.getName());

    @Override
    public Element create(Field field) throws FormException {
        Element element = null;
        if (field.getDeclaringClass().isAnnotationPresent(Accessor.class)
                && (Accessor.AccessType.METHOD == field.getDeclaringClass().getAnnotation(Accessor.class).value())) {
            if (Property.class.isAssignableFrom(field.getType())) {
                try {
                    element = new PropertyMethodElement(field);
                } catch (NoSuchMethodException e) {
                    logger.log(Level.INFO, "No property getter found for " + field);
                    throw new FormException(e);
                }
            } else {
                try {
                    element = new ReadOnlyPropertyMethodElement(field);
                } catch (NoSuchMethodException e) {
                    logger.log(Level.INFO, "No property getter found for " + field);
                    throw new FormException(e);
                }
            }
        } else {
            if (Property.class.isAssignableFrom(field.getType())) {
                element = new PropertyFieldElement(field);
            } else if (ReadOnlyProperty.class.isAssignableFrom(field.getType())) {
                element = new ReadOnlyPropertyFieldElement(field);
            }
        }
        return element;
    }

}
