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

package com.dooapp.fxform.view.factory;

import com.dooapp.fxform.handler.ElementHandler;
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.view.FXFormNode;
import com.dooapp.fxform.view.FXFormNodeWrapper;
import com.dooapp.fxform.view.NodeCreationException;
import com.dooapp.fxform.view.factory.impl.*;
import com.dooapp.fxform.handler.EnumHandler;
import com.dooapp.fxform.handler.FieldHandler;
import com.dooapp.fxform.handler.TypeFieldHandler;
import javafx.beans.property.*;
import javafx.scene.control.Label;
import javafx.util.Callback;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 11/04/11
 * Time: 22:59
 * <p/>
 * Factory implementation based on delegates mapped by FieldHandler.
 */
public class DefaultFactoryProvider implements FactoryProvider {

    private final static Map<ElementHandler, Callback<Void, FXFormNode>> DEFAULT_MAP = new HashMap();

    private final static Map<ElementHandler, Callback<Void, FXFormNode>> GLOBAL_MAP = new HashMap();

    private final Map<ElementHandler, Callback<Void, FXFormNode>> USER_MAP = new HashMap();

    public DefaultFactoryProvider() {
        // register default delegates
        DEFAULT_MAP.put(new TypeFieldHandler(StringProperty.class), new TextFieldFactory());
        DEFAULT_MAP.put(new TypeFieldHandler(BooleanProperty.class), new CheckboxFactory());
        DEFAULT_MAP.put(new EnumHandler(), new ChoiceBoxFactory());
        DEFAULT_MAP.put(new TypeFieldHandler(IntegerProperty.class), new TextFieldFactory());
        DEFAULT_MAP.put(new TypeFieldHandler(LongProperty.class), new TextFieldFactory());
        DEFAULT_MAP.put(new TypeFieldHandler(DoubleProperty.class), new TextFieldFactory());
    }

    private Callback<Void, FXFormNode> getDelegate(Element element, Map<ElementHandler, Callback<Void, FXFormNode>> map) {
        for (ElementHandler handler : map.keySet()) {
            if (handler.handle(element)) {
                return map.get(handler);
            }
        }
        return null;
    }

    public static void addGlobalFactory(FieldHandler handler, Callback<Void, FXFormNode> factory) {
        GLOBAL_MAP.put(handler, factory);
    }

    public void addFactory(FieldHandler handler, Callback<Void, FXFormNode> factory) {
        USER_MAP.put(handler, factory);
    }

    /**
     * Create the node by trying to find a delegate factory.
     * This method will lookup in the user map, the global map and finally in the default map.
     *
     * @return the created node
     * @throws NodeCreationException
     */
    public Callback<Void, FXFormNode> getFactory(Element element) {
        // check user defined factories
        Callback<Void, FXFormNode> delegate = getDelegate(element, USER_MAP);
        // check user defined global factories
        if (delegate == null) {
            delegate = getDelegate(element, GLOBAL_MAP);
        }
        // check default map
        if (delegate == null) {
            delegate = getDelegate(element, DEFAULT_MAP);
        }
        return delegate;
    }

    public String toString() {
        return "[DefaultFactoryProvider\n"
                + "GLOBAL_MAP:\n" + dumpMap(GLOBAL_MAP)
                + "\nUSER_MAP:\n" + dumpMap(USER_MAP)
                + "]";
    }

    private String dumpMap(Map map) {
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = iter.next();
            sb.append(entry.getKey());
            sb.append('=').append('"');
            sb.append(entry.getValue());
            sb.append('"');
            if (iter.hasNext()) {
                sb.append(',').append('\n');
            }
        }
        return sb.toString();
    }

}
