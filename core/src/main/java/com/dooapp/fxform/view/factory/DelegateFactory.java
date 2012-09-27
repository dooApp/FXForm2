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

import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.controller.ElementController;
import com.dooapp.fxform.view.NodeCreationException;
import com.dooapp.fxform.view.factory.delegate.*;
import com.dooapp.fxform.view.handler.EnumHandler;
import com.dooapp.fxform.view.handler.FieldHandler;
import com.dooapp.fxform.view.handler.TypeFieldHandler;
import javafx.beans.property.*;
import javafx.scene.control.Label;
import javafx.util.Callback;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 11/04/11
 * Time: 22:59
 * <p/>
 * Factory implementation based on delegates mapped by FieldHandler.
 */
public class DelegateFactory implements Callback<Void, FXFormNode> {

    private final FormatProvider formatProvider;

    private final static Callback<Void, FXFormNode> DEFAULT_FACTORY = new Callback<Void, FXFormNode>() {

        public FXFormNode call(Void aVoid) {
            Label label = new Label("No factory available");
            return new FXFormNodeWrapper(label);
        }
    };

    private final static Map<FieldHandler, Callback<Void, FXFormNode>> DEFAULT_MAP = new HashMap();

    private final static Map<FieldHandler, Callback<Void, FXFormNode>> GLOBAL_MAP = new HashMap();

    private final Map<FieldHandler, Callback<Void, FXFormNode>> USER_MAP = new HashMap();

    public DelegateFactory() {
        this(new FormatProviderImpl());
    }

    public DelegateFactory(FormatProvider formatProvider) {
        this.formatProvider = formatProvider;
        // register default delegates
        DEFAULT_MAP.put(new TypeFieldHandler(StringProperty.class), new StringPropertyDelegate());
        DEFAULT_MAP.put(new TypeFieldHandler(BooleanProperty.class), new BooleanPropertyDelegate());
        DEFAULT_MAP.put(new EnumHandler(), new EnumPropertyDelegate());
        DEFAULT_MAP.put(new TypeFieldHandler(IntegerProperty.class), new IntegerPropertyDelegate(formatProvider));
        DEFAULT_MAP.put(new TypeFieldHandler(LongProperty.class), new LongPropertyDelegate(formatProvider));
        DEFAULT_MAP.put(new TypeFieldHandler(DoubleProperty.class), new DoublePropertyDelegate(formatProvider));
    }


    public FXFormNode createNode(ElementController controller) throws NodeCreationException {

    }

    private Callback<Void, FXFormNode> getDelegate(Element element, Map<FieldHandler, Callback<Void, FXFormNode>> map) {
        for (FieldHandler handler : map.keySet()) {
            if (handler.handle(element.getField())) {
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
    public FXFormNode call(Void aVoid) {
        // check user defined factories
        Callback<Void, FXFormNode> delegate = getDelegate(controller.getElement(), USER_MAP);
        // check user defined global factories
        if (delegate == null) {
            delegate = getDelegate(controller.getElement(), GLOBAL_MAP);
        }
        // check default map
        if (delegate == null) {
            delegate = getDelegate(controller.getElement(), DEFAULT_MAP);
        }
        // use default factory
        if (delegate == null) {
            delegate = DEFAULT_FACTORY;
        }
        return delegate.call(aVoid);
    }
}
