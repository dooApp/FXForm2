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

package com.dooapp.fxform.view.factory;

import com.dooapp.fxform.annotation.FormFactory;
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.model.FormException;
import com.dooapp.fxform.model.impl.ReadOnlyPropertyFieldElement;
import com.dooapp.fxform.reflection.Util;
import com.dooapp.fxform.view.FXFormNode;
import javafx.beans.property.ObjectProperty;
import javafx.util.Callback;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Factory provider based on the @FormFactory annotation.
 * <p/>
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 07/09/11
 * Time: 16:20
 */
public class AnnotationFactoryProvider implements FactoryProvider {

    private final static Logger logger = Logger.getLogger(AnnotationFactoryProvider.class.getName());

    /**
     * Might return null if no FormFactory annotation if defined neither on the field nor on the field type.
     *
     * @param element
     * @return
     */
    public Callback<Void, FXFormNode> getFactory(Element element) {
        ReadOnlyPropertyFieldElement property = ((ReadOnlyPropertyFieldElement) element);
        // check field annotation
        if (property.getField().getAnnotation(FormFactory.class) != null) {
            // use factory provided by the annotation
            try {
                return property.getField().getAnnotation(FormFactory.class).value().newInstance();
            } catch (Exception e) {
               logger.log(Level.WARNING, "Unable to get new instance for " + property.getField().getAnnotation(FormFactory.class), e);
            }
        }
        // check FormFactory annotation
        if (ObjectProperty.class.isAssignableFrom(property.getField().getType())) {
            try {
                Class genericClass = Util.getObjectPropertyGeneric(property.getField());
                if (genericClass.getAnnotation(FormFactory.class) != null) {
                    return ((FormFactory) genericClass.getAnnotation(FormFactory.class)).value().newInstance();
                }
            } catch (Exception e) {
                // ignore
            }
        }
        return null;
    }
}
