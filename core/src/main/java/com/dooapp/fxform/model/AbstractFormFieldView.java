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

package com.dooapp.fxform.model;

import com.dooapp.fxform.view.FormFieldLabelFactory;
import javafx.scene.Node;

/**
 * User: Antoine Mischler
 * Date: 26/04/11
 * Time: 12:42
 */
public abstract class AbstractFormFieldView<T extends FormField> implements FormFieldView {

    public static final String LABEL_ID_SUFFIX = "-form-label";

    public static final String EDITOR_ID_SUFFIX = "-form-editor";

    public static final String EDITOR_STYLE = "form-editor";

    public static final String LABEL_STYLE = "form-label";

    public static final String TOOLTIP_ID_SUFFIX = "-form-tooltip";

    public static final String TOOLTIP_STYLE = "form-tooltip";

    public static String LABEL_SUFFIX = "-label";

    public static String TOOLTIP_SUFFIX = "-tooltip";

    private final FormFieldLabelFactory labelFactory = new FormFieldLabelFactory(LABEL_SUFFIX, false);

    private final FormFieldLabelFactory tooltipFactory = new FormFieldLabelFactory(TOOLTIP_SUFFIX, true);

    protected final T formField;

    private Node label;

    private Node editor;

    private Node tooltip;

    public AbstractFormFieldView(T formField) {
        this.formField = formField;
    }

    public Node getLabelNode() {
        if (label == null) {
            label = createLabel();
            label.getStyleClass().add(LABEL_STYLE);
            label.setId(formField.getField().getName() + LABEL_ID_SUFFIX);

        }
        return label;
    }

    protected Node createLabel() {
        return labelFactory.createNode(formField.getField());
    }

    public Node getEditorNode() {
        if (editor == null) {
            editor = createEditor();
            editor.getStyleClass().add(EDITOR_STYLE);
            editor.setId(formField.getField().getName() + EDITOR_ID_SUFFIX);
        }
        return editor;
    }

    protected abstract Node createEditor();

    public Node getTooltipNode() {
        if (tooltip == null) {
            tooltip = createTooltip();
            if (tooltip != null) {
                tooltip.getStyleClass().add(TOOLTIP_STYLE);
                tooltip.setId(formField.getField().getName() + TOOLTIP_ID_SUFFIX);
            }
        }
        return tooltip;
    }

    protected Node createTooltip() {
        return tooltipFactory.createNode(formField.getField());
    }
}
