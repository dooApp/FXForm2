package com.dooapp.fxform.view;

import com.dooapp.fxform.view.factory.FXFormNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created at 27/09/12 17:21.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class ElementNodes {
    /**
     * The logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ElementNodes.class);

    private final FXFormNode editor;

    private final FXFormNode label;

    private final FXFormNode tooltip;

    private final FXFormNode constraint;

    public ElementNodes(FXFormNode editor, FXFormNode label, FXFormNode tooltip, FXFormNode constraint) {
        this.editor = editor;
        this.label = label;
        this.tooltip = tooltip;
        this.constraint = constraint;
    }

    public FXFormNode getEditor() {
        return editor;
    }

    public FXFormNode getLabel() {
        return label;
    }

    public FXFormNode getTooltip() {
        return tooltip;
    }

    public FXFormNode getConstraint() {
        return constraint;
    }
}