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

package com.dooapp.fxform.view.factory.delegate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.metadata.ConstraintDescriptor;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * A virtual constraint violation that is used when the content of a field can not be parsed.<br>
 * <br>
 * Created at 19/03/12 17:31.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 *
 */
public class ParseErrorConstraintViolation implements ConstraintViolation<Number> {
    /**
     * The logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ParseErrorConstraintViolation.class);

    private final String unparsed;

    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("FXForm");

    public ParseErrorConstraintViolation(String unparsed) {
        this.unparsed = unparsed;
    }

    public String getMessage() {
        return MessageFormat.format(resourceBundle.getString("parse-error"), unparsed);
    }

    public String getMessageTemplate() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public Number getRootBean() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public Class<Number> getRootBeanClass() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public Object getLeafBean() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public Path getPropertyPath() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public Object getInvalidValue() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public ConstraintDescriptor<?> getConstraintDescriptor() {
        throw new UnsupportedOperationException("Not implemented");
    }
}