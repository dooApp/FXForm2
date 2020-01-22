package com.dooapp.fxform.model.impl;

import javafx.beans.binding.ListBinding;
import javafx.collections.ObservableList;

import java.lang.ref.WeakReference;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 16/01/2020
 * Time: 11:55
 */
public class SourceListBinding extends ListBinding {

    private final WeakReference<AbstractFieldElement> element;

    public SourceListBinding(AbstractFieldElement element) {
        super.bind(element.sourceProperty());
        this.element = new WeakReference<>(element);
    }

    @Override
    protected ObservableList computeValue() {
        if (element.get() == null || element.get().getSource() == null) {
            return null;
        }
        return (ObservableList) element.get().computeValue();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (element.get() != null) {
            unbind(element.get().sourceProperty());
        }
    }

}
