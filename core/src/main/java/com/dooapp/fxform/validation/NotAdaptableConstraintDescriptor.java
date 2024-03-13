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

package com.dooapp.fxform.validation;

import javax.validation.ConstraintTarget;
import javax.validation.ConstraintValidator;
import javax.validation.Payload;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.ValidateUnwrappedValue;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 21/11/2013
 * Time: 14:16
 */
public class NotAdaptableConstraintDescriptor implements ConstraintDescriptor {

    @Override
    public Annotation getAnnotation() {
        return null;
    }

    @Override
    public String getMessageTemplate() {
        return null;
    }

    @Override
    public Set<Class<?>> getGroups() {
        return Collections.emptySet();
    }

    @Override
    public Set<Class<? extends Payload>> getPayload() {
        return Collections.emptySet();
    }

    @Override
    public ConstraintTarget getValidationAppliesTo() {
        return null;
    }

    @Override
    public List<Class<? extends ConstraintValidator>> getConstraintValidatorClasses() {
        return Collections.emptyList();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Collections.emptyMap();
    }

    @Override
    public Set<ConstraintDescriptor<?>> getComposingConstraints() {
        return Collections.emptySet();
    }

    @Override
    public boolean isReportAsSingleViolation() {
        return false;
    }

    @Override
    public ValidateUnwrappedValue getValueUnwrapping() {
        return null;
    }

    @Override
    public Object unwrap(Class aClass) {
        return null;
    }
}
