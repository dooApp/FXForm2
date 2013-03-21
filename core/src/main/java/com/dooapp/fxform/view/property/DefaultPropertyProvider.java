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

package com.dooapp.fxform.view.property;

import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 30/09/12
 * Time: 09:50
 */
public class DefaultPropertyProvider implements PropertyProvider {

    protected final Map<Class<? extends Node>, PropertyProvider> map = new HashMap<Class<? extends Node>, PropertyProvider>();

    @Override
    public Property getProperty(Node node) {
        if (node == null)
            return null;
        for (Class clazz : map.keySet()) {
            if (clazz.isAssignableFrom(node.getClass())) {
                return map.get(clazz).getProperty(node);
            }
        }
        return null;
    }

    public DefaultPropertyProvider() {
        map.put(Label.class, new PropertyProvider<Label>() {
            @Override
            public Property getProperty(Label node) {
                return node.textProperty();
            }
        });
        map.put(TextField.class, new PropertyProvider<TextField>() {
            @Override
            public Property getProperty(TextField node) {
                return node.textProperty();
            }
        });
        map.put(TextArea.class, new PropertyProvider<TextArea>() {
            @Override
            public Property getProperty(TextArea node) {
                return node.textProperty();
            }
        });
        map.put(ChoiceBox.class, new PropertyProvider<ChoiceBox>() {
            @Override
            public Property getProperty(ChoiceBox node) {
                return new ChoiceBoxDefaultProperty(node);
            }
        });
        map.put(ComboBox.class, new PropertyProvider<ComboBox>() {
            @Override
            public Property getProperty(ComboBox node) {
                return new ComboBoxDefaultProperty(node);
            }
        });
        map.put(CheckBox.class, new PropertyProvider<CheckBox>() {
            @Override
            public Property getProperty(CheckBox node) {
                return node.selectedProperty();
            }
        });
        map.put(ToggleButton.class, new PropertyProvider<ToggleButton>() {
            @Override
            public Property getProperty(ToggleButton node) {
                return node.selectedProperty();
            }
        });
        map.put(Slider.class, new PropertyProvider<Slider>() {
            @Override
            public Property getProperty(Slider node) {
                return node.valueProperty();
            }
        });
    }
}
