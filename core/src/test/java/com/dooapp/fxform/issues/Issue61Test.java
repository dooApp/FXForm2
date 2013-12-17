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
package com.dooapp.fxform.issues;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.filter.ExcludeFilter;
import com.dooapp.fxform.filter.IncludeFilter;
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.model.FormException;
import com.dooapp.fxform.reflection.MultipleBeanSource;
import com.dooapp.fxform.reflection.ReflectionUtils;
import com.dooapp.fxform.reflection.impl.ReflectionFieldProvider;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import junit.framework.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Kevin Senechal <kevin.senechal@dooapp.com>
 * Date: 09/12/2013
 * Time: 15:14
 */
public class Issue61Test {


    public static class Bean1 {

        private StringProperty property = new SimpleStringProperty();
    }

    public static class Bean2 {

        private StringProperty property = new SimpleStringProperty();

        private StringProperty property2 = new SimpleStringProperty();
    }

    @Test
    public void testExtractExpectedFields()
            throws
            FormException, IllegalArgumentException {
        Bean1 bean1 = new Bean1();
        Bean2 bean2 = new Bean2();
        ReflectionFieldProvider reflectionFieldProvider = new ReflectionFieldProvider();
        List<String> include = new ArrayList<String>();
        include.add(Bean1.class.getName() + "-property");
        List<Field> fields = reflectionFieldProvider.getProperties(new MultipleBeanSource(bean1, bean2), include);
        Assert.assertEquals(1, fields.size());
        Assert.assertEquals(fields.get(0), ReflectionUtils.getFieldByName(Bean1.class, "property"));
    }

    @Test
    public void testMultipleBeanSource() throws IllegalArgumentException {
        FXForm fxForm = new FXForm();
        Bean1 bean1 = new Bean1();
        Bean2 bean2 = new Bean2();
        fxForm.setSource(new MultipleBeanSource(bean1, bean2));
        Assert.assertEquals(3, fxForm.getElements().size());
    }

    @Test
    public void testMultipleBeanSourceWithIncludeFilter() throws IllegalArgumentException {
        FXForm fxForm = new FXForm();
        Bean1 bean1 = new Bean1();
        Bean2 bean2 = new Bean2();
        fxForm.getFilters().add(new IncludeFilter(bean1.getClass().getName() + "-property"));
        fxForm.setSource(new MultipleBeanSource(bean1, bean2));
        Assert.assertEquals(1, fxForm.getFilteredElements().size());
        Assert.assertEquals(fxForm.getFilteredElements().get(0).getDeclaringClass(), Bean1.class);
        fxForm.getFilters().clear();
        fxForm.getFilters().add(new IncludeFilter(bean2.getClass().getName() + "-property"));
        Assert.assertEquals(1, fxForm.getFilteredElements().size());
        Assert.assertEquals(fxForm.getFilteredElements().get(0).getDeclaringClass(), Bean2.class);
    }

    @Test
    public void testMultipleBeanSourceWithExcludeFilter() throws IllegalArgumentException {
        FXForm fxForm = new FXForm();
        Bean1 bean1 = new Bean1();
        Bean2 bean2 = new Bean2();
        fxForm.getFilters().add(new ExcludeFilter(bean1.getClass().getName() + "-property"));
        fxForm.setSource(new MultipleBeanSource(bean1, bean2));
        Assert.assertEquals(2, fxForm.getFilteredElements().size());
        Assert.assertEquals(fxForm.getFilteredElements().get(0).getDeclaringClass(), Bean2.class);
        Assert.assertEquals(fxForm.getFilteredElements().get(1).getDeclaringClass(), Bean2.class);
    }

    @Test
    public void testMultipleBeanSourceWithIncludeAndExcludeFilter() throws IllegalArgumentException {
        FXForm fxForm = new FXForm();
        Bean1 bean1 = new Bean1();
        Bean2 bean2 = new Bean2();
        fxForm.getFilters().addAll(new ExcludeFilter(bean1.getClass().getName() + "-property"),
                new IncludeFilter(bean2.getClass().getName() + "-property"));
        fxForm.setSource(new MultipleBeanSource(bean1, bean2));
        Assert.assertEquals(1, fxForm.getFilteredElements().size());
        Assert.assertEquals(fxForm.getFilteredElements().get(0).getDeclaringClass(), Bean2.class);
    }

    protected boolean hasElement(List<Element> elementList, String name) {
        for (Element element : elementList) {
            if (name.equals(element.getName())) {
                return true;
            }
        }
        return false;
    }
}
