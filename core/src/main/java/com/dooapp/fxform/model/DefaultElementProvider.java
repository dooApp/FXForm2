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

import com.dooapp.fxform.filter.field.FieldFilter;
import com.dooapp.fxform.filter.field.SyntheticFieldFilter;
import com.dooapp.fxform.reflection.FieldProvider;
import com.dooapp.fxform.reflection.MultipleBeanSource;
import com.dooapp.fxform.reflection.impl.ReflectionFieldProvider;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Default implementation used to create elements for a bean.
 * <p/>
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 03/12/2013
 * Time: 14:51
 */
public class DefaultElementProvider implements ElementProvider {

    private final static Logger logger = Logger.getLogger(DefaultElementProvider.class.getName());

    FieldProvider fieldProvider = new ReflectionFieldProvider();

    ElementFactory elementFactory = new DefaultElementFactory();

    private final String[] includes;

    private List<FieldFilter> filters;

    public DefaultElementProvider(String... includes) {
        this(new ReflectionFieldProvider(), includes);
    }

    public DefaultElementProvider(FieldProvider fieldProvider, String... includes) {
        this(new DefaultElementFactory(), fieldProvider, includes);
    }

    public DefaultElementProvider(ElementFactory elementFactory, FieldProvider fieldProvider, String... includes) {
        this.elementFactory = elementFactory;
        this.fieldProvider = fieldProvider;
        this.includes = includes;
        this.filters = new LinkedList<FieldFilter>();
        this.filters.add(new SyntheticFieldFilter());
    }

    @Override
    public <T> ListProperty<Element> getElements(final ObjectProperty<T> source) {
        final ListProperty<Element> elements = new SimpleListProperty<Element>(FXCollections.<Element>observableArrayList());
        elements.setAll(createElements(source));
        return elements;
    }

    protected <T> ListProperty<Element> createElements(final ObjectProperty<T> source) {
        ListProperty<Element> elements = new SimpleListProperty<Element>(FXCollections.<Element>observableArrayList());
        List<Field> fields = applyFilters(extractFields(source.get()));
        // Create elements for the resulting field list
        for (Field field : fields) {
            try {
                final Element element = elementFactory.create(field);
                element.sourceProperty().bind(new ObjectBinding() {

                    {
                        bind(source);
                    }

                    @Override
                    protected Object computeValue() {
                        if (source.get() != null && source.get() instanceof MultipleBeanSource) {
                            MultipleBeanSource multipleBeanSource = (MultipleBeanSource) source.get();
                            return multipleBeanSource.getSource(element);
                        }
                        return source.get();
                    }
                });
                if (element.getType() != null) {
                    elements.add(element);
                }
            } catch (FormException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
        }
        return elements;
    }

    private List<Field> applyFilters(List<Field> fields) {
        List<Field> filtered = new ArrayList<Field>();
        for (Field field : fields) {
            boolean accept = true;
            for (FieldFilter filter : filters) {
                accept = accept && filter.accept(field);
            }
            if (accept) {
                filtered.add(field);
            }
        }
        return filtered;
    }

    private <T> List<Field> extractFields(T source) {
        if (includes != null && includes.length > 0) {
            return fieldProvider.getProperties(source, Arrays.asList(includes));
        } else {
            return fieldProvider.getProperties(source);
        }
    }

    public List<FieldFilter> getFilters() {
        return filters;
    }
}
