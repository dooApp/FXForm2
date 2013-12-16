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

package com.dooapp.fxform.reflection.impl;

import com.dooapp.fxform.reflection.FieldProvider;
import com.dooapp.fxform.reflection.MultipleBeanSource;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Abstract base class for field providers that add support for {@link com.dooapp.fxform.reflection.MultipleBeanSource}
 * <p/>
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 16/12/2013
 * Time: 15:00
 */
public abstract class MultipleSourceFieldProvider implements FieldProvider {

    @Override
    public List<Field> getProperties(Object source) {
        List<Field> result = new LinkedList<Field>();
        if (source != null) {
            if (source instanceof MultipleBeanSource) {
                MultipleBeanSource multipleBeanSource = (MultipleBeanSource) source;
                for (Object s : multipleBeanSource.getSources()) {
                    getFields(result, s.getClass());
                }
            } else {
                getFields(result, source.getClass());
            }
        }
        return result;
    }

    @Override
    public List<Field> getProperties(Object source, List<String> fields) {
        List<Field> result = new LinkedList<Field>();
        if (source != null) {
            if (source instanceof MultipleBeanSource) {
                MultipleBeanSource multipleBeanSource = (MultipleBeanSource) source;
                getMultipleFields(result, multipleBeanSource, fields);
            } else {
                getFields(result, source.getClass(), fields);
            }
        }
        return result;
    }

    /**
     * This method tries to extract the fields from the right source in the multiple source bean.
     *
     * @param result
     * @param source
     * @param fields
     */
    protected void getMultipleFields(List<Field> result, MultipleBeanSource source, List<String> fields) {
        List<String> notSpecificFields = new ArrayList<String>();
        List<SpecificField> specificFields = new ArrayList<SpecificField>();
        for (String field : fields) {
            String[] fieldValues = field.split("-");
            if (fieldValues.length == 2) {
                specificFields.add(new SpecificField(fieldValues[0], fieldValues[1]));
            } else {
                notSpecificFields.add(field);
            }
        }
        for (SpecificField specificField : specificFields) {
            for (Object s : source.getSources()) {
                if (s.getClass().getName().equals(specificField.getClassName())) {
                    List<String> includeList = new ArrayList<String>();
                    includeList.add(specificField.getName());
                    getFields(result, s.getClass(), includeList);
                }
            }
        }
        if (!notSpecificFields.isEmpty()) {
            for (Object s : source.getSources()) {
                getFields(result, s.getClass(), notSpecificFields);
            }
        }
    }

    private class SpecificField {

        private String className;

        private String name;

        private SpecificField(String className, String name) {
            this.className = className;
            this.name = name;
        }

        public String getClassName() {
            return className;
        }

        public String getName() {
            return name;
        }
    }

    protected abstract void getFields(List<Field> result, Class aClass);

    protected abstract void getFields(List<Field> result, Class aClass, List<String> includes);


}
