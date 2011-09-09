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
import com.dooapp.fxform.model.ElementController;
import com.dooapp.fxform.type.EnumProperty;
import com.dooapp.fxform.view.NodeCreationException;
import com.dooapp.fxform.view.factory.delegate.*;
import com.dooapp.fxform.view.handler.FieldHandler;
import com.dooapp.fxform.view.handler.TypeFieldHandler;
import javafx.beans.property.*;
import javafx.scene.Node;
import javafx.scene.control.Label;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Antoine Mischler
 * Date: 11/04/11
 * Time: 22:59
 * <p/>
 * Factory implementation based on delegates mapped by FieldHandler.
 */
public class DelegateFactory implements NodeFactory {

    private final static NodeFactory DEFAULT_FACTORY = new NodeFactory() {

        public Node createNode(ElementController elementController) throws NodeCreationException {
            return new Label(elementController.getElement().getField().getType() + " not supported");
        }
    };

    private final static Map<FieldHandler, NodeFactory> DEFAULT_MAP = new HashMap();

    private final static Map<FieldHandler, NodeFactory> GLOBAL_MAP = new HashMap();

    private final Map<FieldHandler, NodeFactory> USER_MAP = new HashMap();

    public DelegateFactory() {
        // register default delegates
        DEFAULT_MAP.put(new TypeFieldHandler(StringProperty.class), new StringPropertyDelegate());
        DEFAULT_MAP.put(new TypeFieldHandler(BooleanProperty.class), new BooleanPropertyDelegate());
        DEFAULT_MAP.put(new TypeFieldHandler(EnumProperty.class), new EnumPropertyDelegate());
        DEFAULT_MAP.put(new TypeFieldHandler(IntegerProperty.class), new IntegerPropertyDelegate());
        DEFAULT_MAP.put(new TypeFieldHandler(LongProperty.class), new LongPropertyDelegate());
        DEFAULT_MAP.put(new TypeFieldHandler(DoubleProperty.class), new DoublePropertyDelegate());
    }

    /**
     * Create the node by trying to find a delegate factory.
     * This method will lookup in the user map, the global map and finally in the default map.
     *
     * @param controller
     * @return the created node
     * @throws NodeCreationException
     */
    public Node createNode(ElementController controller) throws NodeCreationException {
        // check user defined factories
        NodeFactory delegate = getDelegate(controller.getElement(), USER_MAP);
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
        return delegate.createNode(controller);
    }

    private NodeFactory getDelegate(Element element, Map<FieldHandler, NodeFactory> map) {
        for (FieldHandler handler : map.keySet()) {
            if (handler.handle(element.getField())) {
                return map.get(handler);
            }
        }
        return null;
    }

    public static void addGlobalFactory(FieldHandler handler, NodeFactory factory) {
        GLOBAL_MAP.put(handler, factory);
    }

    public void addFactory(FieldHandler handler, NodeFactory factory) {
        USER_MAP.put(handler, factory);
    }

}
