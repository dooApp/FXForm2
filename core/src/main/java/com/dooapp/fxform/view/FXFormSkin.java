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

package com.dooapp.fxform.view;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.model.FormFieldController;
import com.dooapp.fxform.view.impl.DelegateFactoryImpl;
import com.dooapp.fxform.view.utils.ConfigurationStore;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;


/**
 * User: Antoine Mischler
 * Date: 09/04/11
 * Time: 21:36
 * Skin of the FXForm control.
 */
public abstract class FXFormSkin implements Skin<FXForm> {

    public static final String LABEL_ID_SUFFIX = "-form-label";

    public static final String EDITOR_ID_SUFFIX = "-form-editor";

    public static final String EDITOR_STYLE = "form-editor";

    public static final String LABEL_STYLE = "form-label";

    public static final String TOOLTIP_ID_SUFFIX = "-form-tooltip";

    public static final String TOOLTIP_STYLE = "form-tooltip";

    private final EditorFactory editorFactory = new DelegateFactoryImpl();

    protected FXForm fxForm;
    private Node rootNode;
    protected ConfigurationStore<Node> labels = new ConfigurationStore<Node>();
    protected ConfigurationStore<Node> tooltips = new ConfigurationStore<Node>();
    protected ConfigurationStore<Node> editors = new ConfigurationStore<Node>();

    public FXFormSkin(FXForm fxForm) {
        this.fxForm = fxForm;
        labels.getConfigurers().add(new StyleConfigurer(LABEL_STYLE));
        labels.getConfigurers().add(new IdConfigurer(LABEL_ID_SUFFIX));
        tooltips.getConfigurers().add(new StyleConfigurer(TOOLTIP_STYLE));
        tooltips.getConfigurers().add(new IdConfigurer(TOOLTIP_ID_SUFFIX));
        editors.getConfigurers().add(new StyleConfigurer(EDITOR_STYLE));
        editors.getConfigurers().add(new IdConfigurer(EDITOR_ID_SUFFIX));
    }

    protected abstract Node createRootNode() throws NodeCreationException;

    public Node getNode() {
        if (rootNode == null) {
            try {
                rootNode = createRootNode();
            } catch (NodeCreationException e) {
                e.printStackTrace();
            }
        }
        return rootNode;
    }

    public void dispose() {
        fxForm = null;
    }

    public FXForm getSkinnable() {
        return fxForm;
    }

    protected Node createLabel(FormFieldController controller) {
        Node label = new Label(controller.getLabel());
        label.getProperties().put(IdConfigurer.FXFORM_ID, controller.getFormField().getField().getName());
        labels.getStore().add(label);
        return label;
    }

    protected Node createEditor(FormFieldController controller) throws NodeCreationException {
        Node editor = editorFactory.createNode(controller);
        editor.getProperties().put(IdConfigurer.FXFORM_ID, controller.getFormField().getField().getName());
        editors.getStore().add(editor);
        return editor;
    }

    protected Node createTooltip(FormFieldController controller) {
        Node tooltip = new Label(controller.getTooltip());
        tooltip.getProperties().put(IdConfigurer.FXFORM_ID, controller.getFormField().getField().getName());
        tooltips.getStore().add(tooltip);
        return tooltip;
    }

}
