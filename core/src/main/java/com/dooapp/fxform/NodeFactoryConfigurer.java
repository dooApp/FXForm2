/*
 * Copyright (c) 2011, dooApp <contact@dooapp.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of dooApp nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.dooapp.fxform;

import com.dooapp.fxform.model.ElementController;
import com.dooapp.fxform.utils.Configurer;
import com.dooapp.fxform.view.LabelFactory;
import com.dooapp.fxform.view.NodeCreationException;
import javafx.scene.control.Label;

/**
 * User: antoine
 * Date: 25/08/11
 * Time: 18:31
 */
public class NodeFactoryConfigurer implements Configurer<ElementController> {

    public static final String LABEL_ID_SUFFIX = "-form-label";

    public static final String LABEL_STYLE = "form-label";

    private final LabelFactory labelFactory;

    public NodeFactoryConfigurer(LabelFactory labelFactory) {
        this.labelFactory = labelFactory;
    }

    public void configure(final ElementController toConfigure) {
        toConfigure.setLabelFactory(new LabelFactory<ElementController>() {
            public Label createNode(ElementController controller) throws NodeCreationException {
                Label label = labelFactory.createNode(controller);
                label.setId(controller.getFormField().getField().getName() + LABEL_ID_SUFFIX);
                label.getStyleClass().add(LABEL_STYLE);
                return label;
            }
        });
    }

    public void unconfigure(ElementController toUnconfigure) {
        toUnconfigure.setLabelFactory(null);
    }
}
