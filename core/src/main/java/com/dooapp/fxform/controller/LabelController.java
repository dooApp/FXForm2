package com.dooapp.fxform.controller;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.view.FXFormNode;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.StringProperty;

/**
 * Created at 27/09/12 15:45.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class LabelController extends NodeController {

    public static String LABEL_SUFFIX = "-label";

    public LabelController(FXForm fxForm, Element element) {
        super(fxForm, element);
    }

    @Override
    protected void bind(FXFormNode fxFormNode) {
        fxFormNode.getProperty().bind(new ObjectBinding() {
            {
                bind(getFxForm().resourceBundleProperty());
            }

            @Override
            protected Object computeValue() {
                String label;
                try {
                    label = getFxForm().getResourceBundle().getString(getElement().getName() + LABEL_SUFFIX);
                } catch (Exception e) {
                    // label is undefined
                    label = (getElement().getName());
                }
                return getFxForm().getAdapterProvider().getAdapter(StringProperty.class, getNode().getProperty().getClass(), getElement(), getNode()).adaptTo(label);
            }
        });
    }
}