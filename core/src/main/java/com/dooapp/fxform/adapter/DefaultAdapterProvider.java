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

package com.dooapp.fxform.adapter;

import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.view.FXFormNode;
import javafx.beans.property.*;
import javafx.util.converter.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 29/09/12
 * Time: 17:31
 */
public class DefaultAdapterProvider implements AdapterProvider {

    private final static Logger logger = Logger.getLogger(DefaultAdapterProvider.class.getName());

    private final static Map<AdapterMatcher, Adapter> DEFAULT_MAP = new HashMap();

    private final static Map<AdapterMatcher, Adapter> GLOBAL_MAP = new HashMap();

    private final Map<AdapterMatcher, Adapter> USER_MAP = new HashMap();

    public DefaultAdapterProvider() {
        DEFAULT_MAP.put(new AdapterMatcher() {
            @Override
            public boolean matches(Class fromClass, Class toClass, Element element, FXFormNode fxFormNode) {
                return fromClass.isAssignableFrom(toClass);
            }
        }, new DefaultAdapter());

        DEFAULT_MAP.put(new TypeAdapterMatcher(IntegerProperty.class, StringProperty.class),
                new ConverterWrapper(new IntegerStringConverter()));
        DEFAULT_MAP.put(new TypeAdapterMatcher(FloatProperty.class, StringProperty.class),
                new ConverterWrapper(new FloatStringConverter()));
        DEFAULT_MAP.put(new TypeAdapterMatcher(LongProperty.class, StringProperty.class),
                new ConverterWrapper(new LongStringConverter()));
        DEFAULT_MAP.put(new TypeAdapterMatcher(DoubleProperty.class, StringProperty.class),
                new ConverterWrapper(new DoubleStringConverter()));
        DEFAULT_MAP.put(new TypeAdapterMatcher(BooleanProperty.class, StringProperty.class),
                new ConverterWrapper(new BooleanStringConverter()));
        DEFAULT_MAP.put(new ObjectPropertyAdapterMatcher(BigDecimal.class, StringProperty.class),
                new ConverterWrapper(new BigDecimalStringConverter()));
        DEFAULT_MAP.put(new TypeAdapterMatcher(IntegerProperty.class, DoubleProperty.class),
                new Adapter<Integer, Double>() {

                    @Override
                    public Double adaptTo(Integer from) {
                        return from.doubleValue();
                    }

                    @Override
                    public Integer adaptFrom(Double to) {
                        return to.intValue();
                    }
                });
        DEFAULT_MAP.put(new TypeAdapterMatcher(FloatProperty.class, DoubleProperty.class),
                new Adapter<Float, Double>() {

                    @Override
                    public Double adaptTo(Float from) {
                        return from.doubleValue();
                    }

                    @Override
                    public Float adaptFrom(Double to) {
                        return to.floatValue();
                    }
                });

        DEFAULT_MAP.put(new PropertyTypeMatcher(StringProperty.class), new DefaultAdapter());
        DEFAULT_MAP.put(new PropertyTypeMatcher(IntegerProperty.class), new DefaultAdapter());
        DEFAULT_MAP.put(new PropertyTypeMatcher(FloatProperty.class), new DefaultAdapter());
        DEFAULT_MAP.put(new PropertyTypeMatcher(DoubleProperty.class), new DefaultAdapter());
        DEFAULT_MAP.put(new PropertyTypeMatcher(BooleanProperty.class), new DefaultAdapter());

        DEFAULT_MAP.put(new TypeAdapterMatcher(ObjectProperty.class, StringProperty.class),
                new ToStringConverter());
    }

    @Override
    public Adapter getAdapter(Class fromClass, Class toClass, Element element, FXFormNode fxFormNode) {
        // check user defined factories
        Adapter adapter = getAdapter(fromClass, toClass, element, fxFormNode, USER_MAP);
        // check user defined global factories
        if (adapter == null) {
            adapter = getAdapter(fromClass, toClass, element, fxFormNode, GLOBAL_MAP);
        }
        // check default map
        if (adapter == null) {
            adapter = getAdapter(fromClass, toClass, element, fxFormNode, DEFAULT_MAP);
        }
        if (adapter == null) {
            // we are converting to a String, use a generic toString converter
            if (StringProperty.class.isAssignableFrom(toClass)) {
                adapter = new ToStringConverter();
            } else {
                adapter = new DefaultAdapter();
                logger.log(java.util.logging.Level.WARNING, "No adapter between types " + fromClass + " and " + toClass + " was found (to adapt " + element + " and " + fxFormNode + ")" +
                        "\nMake sure to register the required adapter in DefaultAdapterProvider either in the global or in the user map. See FXForm#setAdapterProvider");

            }
        }
        return adapter;
    }

    private Adapter getAdapter(Class fromClass, Class toClass, Element element, FXFormNode fxFormNode, Map<AdapterMatcher, Adapter> map) {
        for (AdapterMatcher matcher : map.keySet()) {
            if (matcher.matches(fromClass, toClass, element, fxFormNode)) {
                return map.get(matcher);
            }
        }
        return null;
    }

    public static void addGlobalAdapter(AdapterMatcher adapterMatcher, Adapter adapter) {
        GLOBAL_MAP.put(adapterMatcher, adapter);
    }

    public void addFactory(AdapterMatcher adapterMatcher, Adapter adapter) {
        USER_MAP.put(adapterMatcher, adapter);
    }

}
