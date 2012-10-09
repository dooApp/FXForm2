package com.dooapp.fxform.controller;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.view.FXFormNode;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.StringProperty;

/**
 * Created at 27/09/12 17:15.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class ReadOnlyPropertyEditorController extends NodeController {

    public ReadOnlyPropertyEditorController(FXForm fxForm, Element element) {
        super(fxForm, element);
    }

    @Override
    protected void bind(FXFormNode fxFormNode) {
        fxFormNode.getProperty().bind(new ObjectBinding() {
            {
                bind(getElement());
            }

            @Override
            protected Object computeValue() {
                return getFxForm().getAdapterProvider().getAdapter(StringProperty.class, getNode().getProperty().getClass(), getElement(), getNode()).adaptTo(getElement());
            }
        });
    }
}