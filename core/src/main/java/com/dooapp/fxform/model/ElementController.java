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

package com.dooapp.fxform.model;

import com.dooapp.fxform.i18n.ResourceBundleHelper;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;

import javax.validation.ConstraintViolation;
import java.util.MissingResourceException;

/**
 * User: Antoine Mischler
 * Date: 26/04/11
 * Time: 11:14
 * <p/>
 * The controller of a Element.
 */
public abstract class ElementController<T extends Element> {

    public static String LABEL_SUFFIX = "-label";

    public static String TOOLTIP_SUFFIX = "-tooltip";

    protected final T formField;

    private final BooleanProperty dirty = new SimpleBooleanProperty();

    public ElementController(T formField) {
        this.formField = formField;
    }

    public T getFormField() {
        return formField;
    }

    /**
     * Get the label for this field.
     *
     * @return
     */
    public String getLabel() {
        try {
            return ResourceBundleHelper.$(formField.getField().getName() + LABEL_SUFFIX);
        } catch (MissingResourceException e) {
            // label is undefined
            return formField.getField().getName();
        }
    }

    /**
     * Get the tooltip for this field. Might return null if no tooltip has been defined.
     *
     * @return
     */
    public String getTooltip() {
        try {
            return ResourceBundleHelper.$(formField.getField().getName() + TOOLTIP_SUFFIX);
        } catch (MissingResourceException e) {
            // tooltip is undefined
            return null;
        }
    }

    @Override
    public String toString() {
        return "AbstractFormFieldController{" +
                "formField=" + formField +
                '}';
    }

    /**
     * Returns an observable collection of violated constraints.
     *
     * @return
     */
    public abstract ObservableList<ConstraintViolation> getConstraintViolations();

    public BooleanProperty dirty() {
        return dirty;
    }

    public boolean isDirty() {
        return dirty.get();
    }

    public void setDirty(boolean dirty) {
        this.dirty().set(dirty);
    }

}
